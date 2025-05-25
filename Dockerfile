# Build stage
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app

# Cài JDK 21
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz && \
    tar -xvf jdk-21_linux-x64_bin.tar.gz && \
    mv jdk-21.* /usr/local/jdk-21 && \
    update-alternatives --install /usr/bin/java java /usr/local/jdk-21/bin/java 1 && \
    update-alternatives --set java /usr/local/jdk-21/bin/java && \
    java -version

COPY . .

# Copy file .env vào cùng thư mục /app
# (Nếu .env nằm ở root project thì đã được copy ở trên rồi)
# Nếu bạn có .dockerignore loại trừ .env, nhớ bỏ nó đi hoặc thêm copy riêng

RUN gradle bootJar -x test

# Run stage
FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/music-web-be-0.0.1-SNAPSHOT.jar app.jar

# Copy file .env từ build stage vào run stage
COPY --from=build /app/.env .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
