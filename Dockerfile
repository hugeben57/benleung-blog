# 应用镜像:只把本地打好的 jar 拷进 JRE 运行
# 在 VM 上构建时不再跑 Maven,先把 jar 以 app.jar 放到本仓库根目录(见 DEPLOY.md)
FROM eclipse-temurin:17-jre-jammy
RUN addgroup --system --gid 10001 app && adduser --system --uid 10001 --ingroup app app
WORKDIR /app
COPY app.jar app.jar
EXPOSE 8080
USER app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
