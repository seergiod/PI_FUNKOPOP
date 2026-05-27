FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiamos las librerías, el código fuente y los datos iniciales
COPY lib/ /app/lib/
COPY datos/ /app/datos/
COPY *.java /app/

# Compilamos el proyecto incluyendo el driver de MySQL en el classpath
RUN javac -cp "lib/*" Tienda.java FunkoPop.java Cliente.java Pedido.java

# Comando para mantener el contenedor vivo. 
# Como es una app por terminal, se ejecutará de forma interactiva al invocarlo.
CMD ["tail", "-f", "/dev/null"]