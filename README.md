# 🧸 PI_FUNKOPOP - Gestión de Tienda de Funkos

Este proyecto es una aplicación full-stack para la gestión de una tienda de Funkos. Incluye un sistema de base de datos para almacenar clientes, pedidos y existencias, un backend desarrollado en Java/Spring Boot y un frontend web servido a través de Nginx.

## 🚀 Despliegue Rápido en Local (Con Docker)

La forma más rápida de arrancar el proyecto en tu máquina local sin necesidad de instalar Java o MySQL manualmente es utilizando **Docker y Docker Compose**.

### 📋 Requisitos Previos

Asegúrate de tener instalado lo siguiente en tu sistema:
* [Git](https://git-scm.com/)
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) (incluye Docker Compose)

### 🛠️ Pasos para Levantar la Aplicación

1. **Clona el repositorio** en tu máquina local:
   ```bash
   git clone [https://github.com/seergiod/PI_FUNKOPOP.git](https://github.com/seergiod/PI_FUNKOPOP.git)
   cd PI_FUNKOPOP
   ```

2. **Construye y levanta los contenedores** en segundo plano:
   ```bash
   docker compose up -d --build
   ```

3. **¡Listo!** Docker se encargará de:
   * Descargar y configurar el contenedor de la base de datos (**MySQL 8.0**).
   * Ejecutar automáticamente el script `schema.sql` para crear las tablas e insertar los datos iniciales.
   * Compilar y arrancar el backend en Java.
   * Levantar el servidor web **Nginx** para servir la interfaz.

---

## 🌐 Puertos y Accesos Locales

Una vez que los contenedores estén arriba (`Up`), puedes acceder a los diferentes servicios a través de tu navegador o terminal local:

| Servicio | URL / Acceso Local | Puerto |
| :--- | :--- | :--- |
| **Página Web (Frontend)** | [http://localhost](http://localhost) | `80` / `443` |
| **Base de Datos (MySQL)** | `localhost` (Usuario: `root` / Contraseña: `toor`) | `3306` |

---

## 🖥️ Ejecución Alternativa: Solo el Cliente Java (`.jar`)

Si prefieres ejecutar el programa de consola interactivo directamente desde tu terminal local conectándote a la base de datos que ya está corriendo en el servidor, sigue estos pasos:

1. Asegúrate de tener **Java 17 o superior** instalado en tu máquina local.
2. Descarga o localiza el archivo `pi_Funkopop.jar`.
3. Ejecuta el programa forzando la conexión hacia el entorno correspondiente pasándole la variable de entorno:

```bash
DB_URL="jdbc:mysql://localhost:3306/tienda_funkos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true" java -jar pi_Funkopop.jar
```

*(Si te estás conectando al servidor en la nube, sustituye `localhost` por la dirección IP pública de la instancia).*

---

## 🛠️ Tecnologías Utilizadas

* **Backend:** Java, JDBC / Spring Boot
* **Base de Datos:** MySQL 8.0
* **Servidor Web & Proxy:** Nginx
* **Infraestructura:** Docker & Docker Compose

---
