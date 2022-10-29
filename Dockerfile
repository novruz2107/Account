FROM alpine
RUN apk add --no-cache openjdk11
COPY build/libs/account-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/
ENTRYPOINT ["java"]
CMD ["-jar", "/app/account-0.0.1-SNAPSHOT.jar"]
