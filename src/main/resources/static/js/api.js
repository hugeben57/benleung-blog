// ===== 公共请求封装 + Auth 工具 =====

const API = {
  // 基础请求：自动带 token、JSON 解析、非 2xx 抛带状态码的错误
  async request(url, { method = 'GET', body, auth = false } = {}) {
    const headers = { 'Content-Type': 'application/json' };
    if (auth) {
      const token = getToken();
      if (token) headers['Authorization'] = token;
    }
    const options = { method, headers };
    if (body !== undefined) options.body = JSON.stringify(body);

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

    const data = await res.json();
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

  // ---- 博客（公开）----
  getBlogList() {
    return this.request('/blog/getBlogList');
  },
  getBlogById(id) {
    return this.request('/blog/getBlogById/' + id);
  },

  // ---- 博客（仅管理员，带 token）----
  addBlog(payload) {
    return this.request('/blog/addBlog', { method: 'POST', body: payload, auth: true });
  },
  updateBlog(id, payload) {
    return this.request('/blog/updateBlog/' + id, { method: 'POST', body: payload, auth: true });
  },
  deleteBlog(id) {
    return this.request('/blog/deleteBlog/' + id, { method: 'DELETE', auth: true });
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
    location.href = 'login.html';
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
