FROM navikt/java:14
COPY target/app.jar /app/app.jar
COPY init.sh /init-scripts/init.sh
