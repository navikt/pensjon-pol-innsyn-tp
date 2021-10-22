FROM navikt/java:16
RUN fc-cache -rv /opt/songkong/fonts
COPY build/libs/*.jar /app/app.jar
COPY init.sh /init-scripts/init.sh
