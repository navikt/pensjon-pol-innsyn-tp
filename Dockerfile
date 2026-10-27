FROM debian:13 as build
RUN apt-get update && apt-get install -y \
    fontconfig \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*

FROM gcr.io/distroless/java21-debian12:nonroot
COPY --from=build /usr/lib/x86_64-linux-gnu/libfreetype.so.6 /usr/lib/x86_64-linux-gnu/
COPY --from=build /usr/share/fonts /usr/share/fonts
COPY --from=build /etc/fonts /etc/fonts

COPY build/libs/pol-innsyn-tp-1.jar /app/app.jar

CMD ["-jar", "/app/app.jar"]
