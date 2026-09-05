# 部署到本地虚拟机(本地打包 + Docker 中间件)

方案:**代码在本机用 Maven 打包成 jar,上传到虚拟机**;虚拟机用 Docker 拉取并运行中间件(MySQL / Redis / MinIO)和应用。VM 上**不做 Maven 构建**,只把 jar 拷进一个极小的 JRE 镜像。

```
本机(mvn package) ──scp 上传 jar──▶ 虚拟机仓库根目录 app.jar
                                        │
                              docker compose up(只拉镜像,不构建 Maven)
                                        ▼
                         容器: mysql + redis + minio + app
```

## 前置条件

- 本机:JDK 17 + Maven(打包用)
- 虚拟机:**Docker Engine + Compose v2 插件**;能访问外网(拉镜像);**不需要装 Java**
- 网络建议:**桥接 / 仅主机网络 + 固定局域网 IP**(见下"为什么")

### 为什么必须是固定 IP(重要)

图片/音乐上传后,后端把 `MINIO_ENDPOINT` 拼成**绝对 URL 存进数据库**,前端 `<img>/<audio>` 原样加载。所以这个地址必须是**浏览器能访问的虚拟机 IP**。NAT 模式下容器和浏览器无法共用同一个地址,此方案不成立;DHCP 换 IP 会让旧媒体 URL 失效。建议静态 IP,防火墙放行 `8080`、`9000`、`9001`。

## 第一步:本机打包

```bash
mvn -q -DskipTests package
# 产物:target/benleung-blog-1.0-SNAPSHOT.jar
```

## 第二步:把代码 + jar 弄到虚拟机

```bash
# (A) 代码用 git:先在仓库提交并推送本次的 Dockerfile/compose/init.sql 等改动,
#     再到虚拟机 clone
git clone <你的仓库地址> && cd benleung-blog

# (B) jar 用 scp 上传,放到仓库根目录并命名为 app.jar(已被 .gitignore 忽略)
scp target/benleung-blog-1.0-SNAPSHOT.jar  <user>@<VM_IP>:benleung-blog/app.jar
```

## 第三步:配置并启动

```bash
# 1. 生成环境变量文件,把 MINIO_ENDPOINT 改成 VM 实际 IP,例如
#    MINIO_ENDPOINT=http://192.168.1.50:9000
cp .env.example .env
vi .env

# 2. 构建应用镜像(只拷 app.jar,很快)并启动全部容器
#    首次会拉 mysql/redis/minio/JRE 镜像,较久
docker compose up -d --build

# 3. 状态:mysql/redis/minio healthy、app Up
docker compose ps

# 4. 应用日志出现 Started ... 即就绪
docker compose logs -f app
```

## 验证

```bash
# 管理员账号已由 init SQL 种入
docker compose exec mysql mysql -ubenblog -p13579@Lym -e "select id,username,role from benblog.user;"
```

浏览器(宿主机或 VM)打开:

1. `http://<VM_IP>:8080/login.html` → `admin / 123456` 登录
2. 上传一张图 → 返回 URL 应为 `http://<VM_IP>:9000/ben-blog-data/picture/<uuid>.jpg`,新标签页能打开即媒体链路通
3. MinIO 控制台 `http://<VM_IP>:9001`(凭据见 `.env`)

## 改代码后重新部署

```bash
# 本机
mvn -q -DskipTests package
scp target/benleung-blog-1.0-SNAPSHOT.jar <user>@<VM_IP>:benleung-blog/app.jar

# 虚拟机(重建应用镜像即可,中间件不动)
docker compose up -d --build app
```

## 常用运维

```bash
docker compose ps                 # 状态
docker compose logs -f app        # 应用日志
docker compose down               # 停(保留数据卷)
docker compose down -v            # 停并删数据卷(清库,重建空库时才用)
docker compose up -d --build app  # 更新应用
```

## 注意事项

- MySQL 的初始化 SQL(`docker/mysql/init/01-init.sql`)**只在数据卷为空的首启执行一次**;改 schema 需 `docker compose down -v` 或手工 SQL。
- 凭据全在 `.env`(已 gitignore),仓库只提交 `.env.example`。改 admin 密码需同步 init SQL(仅首启生效)与登录。
- 应用镜像从仓库根的 `app.jar` 构建,**必须先把 jar 传上去再 `up --build`**,否则镜像构建会因缺文件失败。
- 本机开发不受影响:`application.yml` 读环境变量、缺失时回落原默认值。
