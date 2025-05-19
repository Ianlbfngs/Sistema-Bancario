# Sistema-Bancario

Conversion del proyecto monolitico "Sistema de gestión bancaria" a una arquitectura de microservicios utilizando Spring Boot y React

## Tabla de Contenidos
- [Tecnologías y conceptos utilizados](#tecnologías-y-conceptos-utilizados)
- [Funcionalidades](#funcionalidades)
- [Instalación e utilización](#Instalación-e-utilización)
- [Estructura del repositorio](#Estructura-del-repositorio)
- [Estructura general de los servicios](#Estructura-general-de-los-servicios)


## Tecnologías  y conceptos utilizados
- Java 21
- Spring Boot
- Spring Cloud Gateway
- React
- MySQL
- Git y GitHub
- IntelliJ IDEA
- Postman
- Maven
- Docker
- REST APIs
- Microservicios

## Funcionalidades
- Login de clientes y administradores
- Listados con multiples filtros
- Validaciones al realizar operaciones ABML 
### Funciones del usuario administrador
- ABML de clientes
- ABML de cuentas
### Funciones del usuario cliente
- Listado de movimientos
- Transferencias
- Gestion de multiples cuentas


## Instalación e utilización
### Requisitos previos
- Tener Docker y Docker Compose instalados
### Clonar el repositorio
```bash
git clone https://github.com/Ianlbfngs/Sistema-Bancario.git
cd sistema-bancario
```
### Levantar con Docker
```bash
docker-compose up --build
```
### Acceder
1. Ir a la direccion http://localhost:3000 mediante un navegador web
2. Loguearse como administrador <br>
    <u>Usuario</u>: admin <br>
    <u>Contraseña</u>: admin

## Estructura del repositorio
```
/api-gateway         --> Carpeta con el gateway de los servicios
/auth-service        -->  Carpeta con el servicio de credenciales
/register-service    --> Carpeta con el servicio gestionador del registro de clientes
/clients-service     --> Carpeta con el servicio de clientes
/account-service     --> Carpeta con el servicio de cuentas
/movements-service   --> Carpeta con el servicio de movimientos
/frontend            --> Carpeta con el frontend en react
/db-scripts          --> Carpeta con los scripts para crear las bases de datos 
```
## Estructura general de los servicios
```
/servicio/src/main/java/com/ib/servicio/config      --> Configuración general del servicio
/servicio/src/main/java/com/ib/servicio/controller  --> Controladores REST 
/servicio/src/main/java/com/ib/servicio/dto         --> Objetos de transferencia de datos
/servicio/src/main/java/com/ib/servicio/entity      --> Clases que representan las entidades del servicio
/servicio/src/main/java/com/ib/servicio/mapper      --> Clases para mapear entre entidades y DTOs
/servicio/src/main/java/com/ib/servicio/repository  --> Interfaces para el acceso a la base de datos
/servicio/src/main/java/com/ib/servicio/response    --> Clases para devolver respuestas personalizadas
/servicio/src/main/java/com/ib/servicio/service     --> Clases e Interfaces que implementan la lógica de negocio
```

 