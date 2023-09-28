# Utiliza una imagen base de OpenJDK para Java 17
FROM openjdk:17-jdk-slim

# Copia el archivo JAR de tu aplicación al contenedor
COPY target/BankDevSu-0.0.1-SNAPSHOT.jar /BankDevSu-0.0.1-SNAPSHOT.jar
VOLUME /tmp

EXPOSE 8080
# Establece el directorio de trabajo en /app
WORKDIR /app

# Ejecuta la aplicación cuando se inicie el contenedor
CMD ["java", "-jar", "BankDevSu-0.0.1-SNAPSHOT.jar"]