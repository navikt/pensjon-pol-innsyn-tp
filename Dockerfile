FROM navikt/java:13
COPY target/app.jar /app/app.jar
COPY init.sh /init-scripts/init.sh
