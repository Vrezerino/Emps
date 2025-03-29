FROM openjdk:25-slim

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin

ENV SBT_VERSION 1.10.11

# Install curl and vim
RUN \
  apt-get update && \
  apt-get -y install curl && \
  apt-get -y install vim

# Install both scala and sbt
RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://repo.scala-sbt.org/scalasbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get -y install sbt

# Set working directory inside the container
WORKDIR /app

# Copy the app's files into container
COPY . .

# Install sbt, build the app (packaged version)
RUN ./sbt stage

# Default port 9000
EXPOSE 9000

# Run the Scala Play app inside container
CMD ["target/universal/stage/bin/emps", "-Dplay.http.secret.key=g5DD0nTYfWlJov7BlFtM5Sl7I2QYzzl+n4TqIN2z/Rk="]