FROM docker.io/gradle
COPY . /app
WORKDIR /app
CMD ["sh", "-c", "gradle run --args=${SECRET_TOKEN}"]