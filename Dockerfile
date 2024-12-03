FROM amazoncorretto:21-alpine
# Define un argumento para el nombre del archivo JAR
ARG JAR_FILE=target/pasteleriaback-0.0.1.jar

# Copia el archivo JAR de tu proyecto al contenedor
COPY ${JAR_FILE} pasteleriaback.jar

# Expone el puerto 8080 en el que correrá tu aplicación
EXPOSE 8080

# Define el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/pasteleriaback.jar"]