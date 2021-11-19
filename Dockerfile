FROM navikt/java:16

USER root
RUN apt-get update && apt-get install -y \
    fontconfig \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*
USER apprunner

COPY build/libs/pol-innsyn-tp-1.jar /app/app.jar
COPY init.sh /init-scripts/init.sh
