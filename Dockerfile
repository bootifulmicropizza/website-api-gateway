FROM openjdk:8u131-jdk

ADD target/website-api-gateway.jar /app/dist/website-api-gateway.jar

EXPOSE 8000

VOLUME /tmp

ENTRYPOINT java -Dspring.profiles.active=$BOOTIFUL_MICRO_PIZZA_ENV -Djava.security.egd=file:/dev/./urandom -jar /app/dist/website-api-gateway.jar
