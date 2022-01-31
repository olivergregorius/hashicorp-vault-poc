FROM adoptopenjdk:16-jre

WORKDIR /app

ARG APP_NAME=hashicorp-vault-poc
ARG APP_VERSION

ENV JAR_FILE=${APP_NAME}-${APP_VERSION}.jar

COPY build/libs/${JAR_FILE} ${JAR_FILE}

EXPOSE 8080
ENTRYPOINT exec java -jar ${JAVA_OPTS} ${JAR_FILE}
