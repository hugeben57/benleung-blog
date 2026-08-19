// ===== 公共请求封装 + Auth 工具 =====

const API = {
  // 基础请求：自动带 token、JSON 解析、非 2xx 抛带状态码的错误
  // formData=true 时按 multipart 上传（不手动设置 Content-Type，让浏览器带上 boundary）
  async request(url, { method = 'GET', body, auth = false, formData = false, raw = false } = {}) {
    const headers = {};
    if (auth) {
      const token = getToken();
      if (token) headers['Authorization'] = token;
    }
    const options = { method, headers };
    if (body !== undefined) {
      if (formData) {
        options.body = body;
      } else {
        headers['Content-Type'] = 'application/json';
        options.body = JSON.stringify(body);
      }
    }

    let res;
    try {
      res = await fetch(url, options);
    } catch (e) {
      throw { status: 0, message: '网络请求失败' };
    }

    // 拦截器返回的 401/403 是纯文本，按状态码判断
    if (!res.ok) {
      if (res.status === 401) throw { status: 401, message: '登录已过期，请重新登录' };
      if (res.status === 403) throw { status: 403, message: '没有操作权限' };
      throw { status: res.status, message: '请求失败(' + res.status + ')' };
    }

    // 滑动续期：后端在 token 临近过期时通过响应头下发新 token，这里覆盖本地存储
    const renewedToken = res.headers.get('Authorization');
    if (renewedToken) {
      saveLogin(renewedToken, getRole());
    }

    const data = await res.json();
    // raw 模式：接口直接返回裸数据（非 Result 包装），跳过 code 校验
    if (raw) return data;
    if (data.code !== 200) {
      throw { status: data.code, message: data.message || '操作失败' };
    }
    return data.data;
  },

  // ---- 用户 ----
  signUp(userName, password) {
    return this.request('/user/signUp', { method: 'POST', body: { userName, password } });
  },
  signIn(userName, password) {
    return this.request('/user/signIn', { method: 'POST', body: { userName, password } });
  },
  // 校验当前登录状态：token 有效返回用户信息，过期/无效返回 401
  getUserInfo() {
    return this.request('/user/info', { auth: true });
  },

  // ---- 博客（公开）----
  getBlogById(id) {
    return this.request('/blog/getBlogById/' + id);
  },
  getLatestBlogId() {
    return this.request('/blog/getLatestBlogId', { raw: true });
  },
  getBlogTypes() {
    return this.request('/blog/getBlogTypes');
  },
  getBlogByTypes(type) {
    return this.request('/blog/getBlogByTypes?type=' + encodeURIComponent(type));
  },
  getBlogPages(currentPage, pageSize) {
    return this.request('/blog/getBlogPages?currentPage=' + currentPage + '&pageSize=' + pageSize);
  },

  // ---- 博客（仅管理员，带 token）----
  getAdminBlogList() {
    return this.request('/blog/getAdminBlogList', { auth: true });
  },
  published(id) {
    return this.request('/blog/published/' + id, { method: 'POST', auth: true });
  },
  addBlog(payload) {
    return this.request('/blog/addBlog', { method: 'POST', body: payload, auth: true });
  },
  updateBlog(id, payload) {
    return this.request('/blog/updateBlog/' + id, { method: 'POST', body: payload, auth: true });
  },
  deleteBlog(id) {
    return this.request('/blog/deleteBlog/' + id, { method: 'DELETE', auth: true });
  },

  // ---- 图片（照片墙）----
  getPictures() {
    return this.request('/Picture/getPictures');
  },
  uploadPicture(file, pictureName) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('pictureName', pictureName || file.name || '');
    return this.request('/Picture/uploadPicture', { method: 'POST', body: formData, formData: true, auth: true });
  },
  deletePictureById(id) {
    return this.request('/Picture/deletePictureById/' + id, { method: 'DELETE', auth: true });
  },
  updatePicture(id, pictureName) {
    return this.request('/Picture/updatePicture/' + id + '?pictureName=' + encodeURIComponent(pictureName || ''), { method: 'POST', auth: true });
  },
  setCoverPicture(id) {
    return this.request('/Picture/setCoverPicture/' + id, { method: 'POST', auth: true });
  },

  // ---- 音乐（单曲播放器）----
  getMusic() {
    return this.request('/music/get');
  },
  addMusic(file, musicName) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('musicName', musicName || '');
    return this.request('/music/add', { method: 'POST', body: formData, formData: true, auth: true });
  },
  updateMusic(file, musicName) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('musicName', musicName || '');
    return this.request('/music/update', { method: 'POST', body: formData, formData: true, auth: true });
  },
};

// ===== Auth（localStorage）=====
function getToken() {
  return localStorage.getItem('token');
}
function getRole() {
  const v = localStorage.getItem('role');
  return v === null ? null : Number(v);
}
function saveLogin(token, role) {
  localStorage.setItem('token', token);
  localStorage.setItem('role', String(role));
}
function clearAuth() {
  localStorage.removeItem('token');
  localStorage.removeItem('role');
}
function logout() {
  clearAuth();
  location.href = 'login.html';
}

// ===== 通用错误处理：401 清登录跳登录页，403 提示 =====
function handleError(e) {
  if (e && e.status === 401) {
    clearAuth();
    location.href = 'login.html?msg=' + encodeURIComponent('登录已过期，请重新登录');
    return;
  }
  if (e && e.status === 403) {
    alert('没有操作权限');
    return;
  }
  alert((e && e.message) ? e.message : '操作失败');
}

// ===== 工具函数 =====
function escapeHtml(s) {
  return String(s == null ? '' : s).replace(/[&<>"']/g, c => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;',
  }[c]));
}
function formatTime(str) {
  if (!str) return '';
  return String(str).replace('T', ' ').slice(0, 16);
}
function toast(msg) {
  let el = document.getElementById('toast');
  if (!el) {
    el = document.createElement('div');
    el.id = 'toast';
    el.className = 'toast';
    document.body.appendChild(el);
  }
  el.textContent = msg;
  el.classList.add('show');
  setTimeout(() => el.classList.remove('show'), 2000);
}

// ===== 通用导航（index / article 共用）=====
function renderNav() {
  const nav = document.getElementById('nav');
  if (!nav) return;
  const token = getToken();
  const role = getRole();
  let html = '';
  if (token) {
    if (role === 1) html += '<a class="btn btn-primary" href="admin.html">管理面板</a>';
    html += '<button class="btn btn-ghost" onclick="logout()">退出登录</button>';
  } else {
    html += '<a class="btn btn-primary" href="login.html">登录 / 注册</a>';
  }
  nav.innerHTML = html;
}
