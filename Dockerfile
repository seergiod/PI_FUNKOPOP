FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiamos las librerías necesarias y el código fuente de Java
COPY lib/ /app/lib/
COPY *.java /app/

# Compilamos todas las clases incluyendo el driver JDBC de MySQL en el classpath
RUN javac -cp "lib/*" Tienda.java FunkoPop.java Cliente.java Pedido.java

# Mantiene el contenedor vivo en segundo plano esperando a que invoques el menú
CMD ["tail", "-f", "/dev/null"]