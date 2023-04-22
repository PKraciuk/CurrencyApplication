FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn verify --fail-never
COPY /src src
RUN mvn clean package -D maven.test.skip

FROM gcr.io/distroless/java17-debian11:nonroot@sha256:61c05080a2fbf2f6fcb2eb11b936e30ebc7eb5cad474056e5e356b5b1b94d200
USER nonroot
COPY --from=build /app/target/CurrencyApp-0.1.0.jar CurrencyApp.jar
CMD ["/CurrencyApp.jar"]