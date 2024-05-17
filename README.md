
# Prueba técnica - Indra

Proyecto CRUD realizado en Java + Spring Boot + Spring WebFlux + MongoDB + Resilience4j + Drools. Siguiendo la arquitectura hexagonal.

## Requisitos funcionales

### 1. Conexión a Base de Datos Reactiva

Se utiliza integración con 2 bases de datos MongoDB (se configura un MongoTemplate por cada servidor de mongoDB). Se utiliza la interfaz org.springframework.data.mongodb.repository.ReactiveMongoRepository.

### 2. Manejo de errores

Se utiliza la anotación @org.springframework.web.bind.annotation.ControllerAdvice para manejar las excepciones de Dominio y las Exception en general.

### 3. Lógica de negocios con Drools

Se utiliza Drools para decidir a sobre qué base de datos se deben realizar la creación, actualización y eliminación de los clientes (Customer). En "resources/rules/customer.drl", se establece 6 reglas, 2 para cada operación mapeada. Para la creación se determina el repositorio de acuerdo al flag "vip" del cliente.

### 4. Repositorios de Datos A y B

Se utilizan dos fuentes de datos, dos servidores MongoDB cada uno con una base de datos.

#### db1
- host: localhost
- port 27017
#### db2
- host: localhost
- port 27018

### 5. Reintento y CircuiteBreaker

Se utiliza Resilience4j para implementar el patrón CircuiteBreak y Retry para los servicios CRUD.

## Requisitos no funcionales

### 1. Arquitectura hexagonal

Se utiliza la arquitectura hexagonal manejando las capas de dominio, application y infraestructure.

### 2. Código y Estructura del Proyecto

Se estructura el proyecto como se muestra

- **domain**
    - models
    - ports
        - in
        - out
    - exceptions
- **application**
    - service
    - usecases
    - dto
- **infrastructure**
    - config
    - controllers
    - entities
    - respositories
    - mappers

### 3. Prueba

Se implementan pruebas para los controladores, servicio y repositorios con uso de las librerias: Spring Boot Test, de.flapdoodle.embed.mongo y Reactor-test.

### 4. README.md

Se generó este archivo README.md detallando Instalación, Configuración y Pruebas.

### 5. Repositorio GitHub

Se creó repositorio GitHub kevin65751/practice-indra.
Se creó la rama **feature/practice** y **develop** y se realiza el merge solicitado.


## Installation

### Requisitos
- Maven
- Java 17
- Docker
- MongoDB

```bash
  # En el directorio del proyecto ejecutar, para compilar, correr los test y empaquetar el proyecto en un .jar
  mvn clean package

  # Para levantar los dos servidores MongoDB se puede utilizar docker compose
  docker compose up
  
  # Para ejecutar el microservicio ejecutar
  java -jar ./target/practice-indra-0.0.1-SNAPSHOT.jar
```
    
## Tests

Para correr solamente las pruebas ejecutar:

```bash
  mvn clean test
```

También se cuenta con una colección de postman para probar las operaciones.

./practice_indra.postman_collection.json
