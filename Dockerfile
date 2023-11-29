FROM ghcr.io/navikt/baseimages/temurin:21

USER root
RUN apt-get update && apt-get install -y \
    fontconfig \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*
USER apprunner

COPY build/libs/pol-innsyn-tp-1.jar /app/app.jar
