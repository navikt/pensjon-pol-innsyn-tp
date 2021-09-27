FROM navikt/java:14
USER root
RUN apt-get update && apt-get install -y fontconfig libfreetype6 && rm -rf /var/lib/apt/lists/*
USER nais
COPY build/libs/*.jar /app/app.jar
COPY init.sh /init-scripts/init.sh
