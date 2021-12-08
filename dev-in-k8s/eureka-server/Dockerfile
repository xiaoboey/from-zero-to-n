# 基础镜像
FROM openjdk:8-jdk-alpine
# 作者信息
LABEL maintainer="xiaoqb@foxmail.com"

# 修改alpine的源为阿里云的源
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
# 更新
RUN apk upgrade --available && sync

# 创建文件夹
RUN mkdir /app
# 设置当前的工作路径，后续的操作在这个路径下进行
WORKDIR /app
# 把编译后的jar复制到容器里
ADD ./target/*.jar eureka-server.jar

# 设置环境变量（这类变量可以在启动容器时设置，对应docker run的-e参数）
ENV SPRING_PROFILES_ACTIVE=dev
ENV HTTP_PORT=8761
ENV ACTUATOR_PORT=7761

# 容器开放端口
EXPOSE $HTTP_PORT
EXPOSE $ACTUATOR_PORT

# 容器启动执行命令
# -XX:+UseContainerSupport 让JVM在容器环境里分配合理的堆内存
# -XX:MaxRAMPercentage 取值范围0.0到100.0，默认值25.0
ENTRYPOINT java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -jar eureka-server.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE
