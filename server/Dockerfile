FROM amazoncorretto:21
COPY target/*.jar app.jar
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]