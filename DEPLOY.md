# 部署到 Linux 服务器(jar 裸跑 + Docker 中间件)

方案:**应用 jar 直接在服务器上 `java -jar` 跑**,MySQL / Redis / MinIO 用 docker 容器跑并映射端口。简单、常用,中间件好升级,应用就是个普通 java 进程。

```
本机:  mvn package          → 上传 app.jar
服务器: docker compose 起 mysql/redis/minio(只拉镜像)
        java -jar app.jar   → 应用监听 8080
```

## 服务器目录规划(以 /opt/app 为例)

```
/opt/app/
├── app.jar                      # 应用(上传,不进 git)
├── docker-compose.yml           # 中间件编排
├── docker/mysql/init/01-init.sql# 首次建库建表 + admin 种子
├── .env                         # 全部配置(由 .env.example 复制)
├── run.sh / stop.sh             # 启停应用
└── app.log / app.pid            # 运行时生成
```

## 前置条件

- 服务器有 **JDK/JRE 17**(Ubuntu: `sudo apt install -y openjdk-17-jre-headless`,`java -version` 确认)
- 服务器有 **Docker Engine + Compose v2**,能访问外网(拉镜像)
- 网络:中间件里只有 **MinIO 需要浏览器直连**,媒体 URL 是把 `MINIO_ENDPOINT` 拼出来存进数据库的绝对地址。所以服务器要用**固定局域网 IP** 并在 `.env` 里填它(NAT/换 IP 会让历史图片失效)。放行 `8080`(应用)、`9000`(MinIO 媒体)、`9001`(MinIO 控制台)

## 第一步:本机打包并准备文件

```bash
# 打包
mvn -q -DskipTests package        # 产物 target/benleung-blog-1.0-SNAPSHOT.jar

# 把 jar + 部署文件传到服务器(两种任选)
# 方式 A:整个目录用 git clone(需要先把本仓库部署改动提交推送)
# 方式 B:只传需要的文件
scp target/benleung-blog-1.0-SNAPSHOT.jar <user>@<SERVER_IP>:/opt/app/app.jar
scp docker-compose.yml .env.example run.sh stop.sh <user>@<SERVER_IP>:/opt/app/
scp -r docker <user>@<SERVER_IP>:/opt/app/
```

## 第二步:服务器上配置

```bash
cd /opt/app
cp .env.example .env
vi .env    # 关键:MINIO_ENDPOINT 改成 http://<SERVER_LAN_IP>:9000,其余按需改
```

## 第三步:启动中间件(只拉镜像,无 Maven 构建)

```bash
docker compose up -d          # 拉起 mysql/redis/minio
docker compose ps             # 三个都 healthy 后再起应用
```

说明:
- mysql/redis 端口只绑定 `127.0.0.1`,只给本机应用连(安全);MinIO 绑定 `0.0.0.0` 供浏览器访问。
- MySQL 首次启动会用 `docker/mysql/init/01-init.sql` 建好 `benblog` 库、4 张表和 `admin` 账号(**只执行一次**)。

## 第四步:启动应用

```bash
bash run.sh            # nohup 后台运行,pid 存 app.pid
tail -f app.log        # 出现 Started ... 即就绪
```

## 验证

```bash
# 管理员是否种入
docker compose exec mysql mysql -ubenblog -p13579@Lym -e "select id,username,role from benblog.user;"
```

浏览器打开 `http://<SERVER_IP>:8080/login.html` → `admin / 123456` 登录;上传一张图,返回 URL 应为 `http://<SERVER_IP>:9000/ben-blog-data/picture/<uuid>.jpg`,新标签页能打开即媒体链路通。MinIO 控制台 `http://<SERVER_IP>:9001`。

## 日常运维

```bash
bash stop.sh                          # 停应用
bash run.sh                           # 启应用
docker compose ps                     # 中间件状态
docker compose logs -f mysql          # 某中间件日志
docker compose down                   # 停中间件(留数据)
docker compose down -v                # 停并删数据卷(清库,重部署才用)
```

## 改代码后重新部署

```bash
# 本机打包 → 覆盖上传 app.jar
mvn -q -DskipTests package
scp target/benleung-blog-1.0-SNAPSHOT.jar <user>@<SERVER_IP>:/opt/app/app.jar

# 服务器上重启应用即可(中间件不动)
bash stop.sh && bash run.sh
```

## 常见问题

- **8080 端口被占**:应用默认 8080,改 jar 启动参数或换端口(应用侧在 `application.yml`,暂以打包前配置为准)。
- **3306 已被占用**:本机若已有 MySQL 占了 3306,docker 映射改成 `127.0.0.1:3307:3306`,同时 `.env` 的 `DB_PORT=3307`。
- **登录不上**:先确认 `docker compose exec mysql ... select` 能查到 admin;查到但登录失败,多半是 `.env` 与容器初始化时密码不一致。
- **图片打不开**:检查 `MINIO_ENDPOINT` 是否填了浏览器可达的地址;改后需重新上传(旧 URL 记录的是旧地址)。
