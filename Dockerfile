# Usar imagen base de Java
FROM openjdk:17-jdk-slim

# Puerto expuesto
EXPOSE 8080

# Copiar el archivo del servidor
COPY ServidorSocket.java /app/

# Compilar y ejecutar
WORKDIR /app
RUN javac ServidorSocket.java
CMD ["java", "ServidorSocket"]