# 基础镜像
FROM openjdk:8-jdk-alpine
# 作者信息
LABEL maintainer="xiaoqb@foxmail.com"

RUN mkdir /app
WORKDIR /app
ADD ./target/*.jar eureka-server.jar

ENV SPRING_PROFILES_ACTIVE=dev

# 容器开放端口
EXPOSE 8761
EXPOSE 7761

# 容器启动执行命令
# -XX:+UseContainerSupport 让JVM在容器环境里分配合理的堆内存
# -XX:MaxRAMPercentage 取值范围0.0到100.0，默认值25.0
ENTRYPOINT java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -jar eureka-server.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE
