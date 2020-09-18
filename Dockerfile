FROM navikt/java:14
COPY build/libs/*.jar /app/app.jar
COPY init.sh /init-scripts/init.sh
