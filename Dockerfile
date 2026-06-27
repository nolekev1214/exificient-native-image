FROM ghcr.io/graalvm/native-image-community:21 AS build
RUN microdnf install -y maven && microdnf clean all
WORKDIR /build

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src/ src/
COPY schemas/ schemas/

RUN mvn -B package

FROM scratch
COPY --from=build /build/target/*.so /
COPY --from=build /build/target/*.h /