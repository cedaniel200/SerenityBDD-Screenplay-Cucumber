# Imagen para ejecutar la suite de Serenity BDD + Cucumber 8 sin instalar nada en la máquina.
#
# No incluye navegador: el escenario de ejemplo no abre ninguno. Cuando añadas
# escenarios web, apunta Serenity a un Selenium Grid (ver README, sección Docker).
FROM amazoncorretto:17

WORKDIR /app

# El wrapper de Gradle 9 invoca xargs; la imagen base de Amazon Linux no lo trae.
RUN dnf install -y findutils && dnf clean all

# El wrapper se resuelve antes de copiar el código para aprovechar la caché de capas:
# si cambian las fuentes, no se vuelve a descargar Gradle.
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --version --no-daemon

COPY serenity.properties ./
COPY src ./src

CMD ["./gradlew", "clean", "test", "aggregate", "--no-daemon"]
