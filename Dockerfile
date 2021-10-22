FROM navikt/java:16
COPY build/libs/pol-innsyn-tp-1.jar /app/app.jar
COPY init.sh /init-scripts/init.sh
