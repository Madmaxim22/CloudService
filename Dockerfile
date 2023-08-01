FROM amazoncorretto:17-alpine
#По умолчанию в Windows создает mvnw c окончанием строк CRLF,
#при запуске в контейнере Linux его необходимо преобразовать,
#установка dos2unix поможет преобразовать окончание строк
RUN apk --no-cache add --update                                         \
        --repository http://dl-3.alpinelinux.org/alpine/edge/testing/   \
        dos2unix
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN dos2unix mvnw
RUN ./mvnw dependency:resolve
COPY src ./src
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]