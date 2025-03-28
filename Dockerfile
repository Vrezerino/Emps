FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the app's files into container
COPY . .

# Default port 9000
EXPOSE 9000

# Install sbt, build the app (packaged version)
RUN ./sbt stage

# Run the Scala Play app inside container
CMD ["target/universal/stage/bin/<your-app-name>", "-Dplay.http.secret.key=changeme"]