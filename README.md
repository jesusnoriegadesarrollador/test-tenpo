# 🧮 Test-Tenpo API

## 📘 Descripción general

**Test-Tenpo** es una API REST desarrollada en **Java 21** con **Spring Boot 3**, que realiza operaciones de cálculo entre dos números aplicando un **porcentaje dinámico** obtenido desde un servicio externo (mockeado).  
Además, guarda un **historial de todas las operaciones** en PostgreSQL, implementa **cache de porcentaje** por 30 minutos, y registra los requests de forma **asíncrona** para no afectar el rendimiento.

---

## ⚙️ Arquitectura y estructura del proyecto

El proyecto utiliza una arquitectura **hexagonal (puertos y adaptadores)** para mantener bajo acoplamiento entre las capas.
```
com.tenpo.testtenpo
├── application
│ ├── service
│ │ ├── impl
│ │ │   ├── CalculateServiceImpl.java → Lógica principal de cálculo
│ │ │   ├── PercentageServiceImpl.java → Obtiene el porcentaje del servicio externo (mock)
│ │ │   └── HistoryServiceImpl.java → Maneja el historial de operaciones
│ │ ├── CalculateService.java → Interfaz lógica principal de cálculo
│ │ ├── PercentageService.java → Interfaz del porcentaje del servicio externo (mock)
│ │ └── HistoryService.java → Interfaz para classe que maneja el historial de operaciones
│ └── cache
│   └── PercentageCache.java → Almacena el porcentaje por 30 minutos
│
├── domain
│ ├── dto
│ │ ├── CalculateRequestDto.java
│ │ ├── CalculateResponseDto.java
│ │ └── HistoryResponseDto.java
│ └── model
│   └── HistoryEntity.java
│
├── infrastructure
│ ├── controller
│ │ ├── CalculateController.java → Endpoint de cálculo
│ │ └── HistoryController.java → Endpoint de historial
│ ├── logging
│ │ └── AsyncLog.java → Guarda logs de requests asíncronamente
│ ├── repository
│ │ └── HistoryRepository.java → Acceso a base de datos (JPA)
│ └── config
│   └── OpenAPIConfig.java → Configuración de Swagger / OpenAPI
│
├── shared
│ └── Constans -> Contiene las constantes del backend
│
└── TestTenpoApplication.java → Clase principal (Spring Boot)
```

---

## 🧩 Requisitos previos

Antes de ejecutar, asegúrate de tener instalado:

- 🐳 **Docker Desktop** (última versión)
- 🧰 **Docker Compose**
- ☕ **Java 21** (si lo corres localmente)
- 🛠️ **Maven 3.9+** (para build manual)

---

## 🚀 Ejecución con Docker

### 1️⃣ Construir y levantar la app + base de datos

Desde la raíz del proyecto:

```bash
docker-compose up --build
```

Esto levantará:

* #### **App** → Spring Boot (`http://localhost:8080`)
* #### **DB** → PostgreSQL (`localhost:5432`)

### 2️⃣ Verificar los contenedores

```bash
docker ps
```

Deberías ver algo como:

```bash
CONTAINER ID   IMAGE             STATUS          PORTS
abc12345       test-tenpo-app    Up 2 minutes    0.0.0.0:8080->8080/tcp
xyz98765       postgres:15       Up 2 minutes    0.0.0.0:5432->5432/tcp
```

### 🌐 Swagger UI (Documentación interactiva)

Una vez que la app esté corriendo, abre en tu navegador:

👉 http://localhost:8080/swagger-ui.html

ó

👉 http://localhost:8080/swagger-ui/index.html

Aquí podrás probar directamente los endpoints `/calculate` y `/history`.


### 📡 Endpoints principales
#### 🔹 **POST** `/api/v1/calculate`

Realiza la suma de `numberOne` + `numberTwo` + porcentaje dinámico (del servicio externo o cache).

**Request Body:**
```json
{
"numberOne": 100,
"numberTwo": 200
}
```
**Response:**
```json
{
  "numberOne": 100.0,
  "numberTwo": 200.0,
  "percentage": 10.0,
  "result": 330.0
}
```

#### 🔹 **GET** `/api/v1/history?page=0&size=10`

Devuelve el historial de operaciones registradas (con paginación).

**Ejemplo de respuesta:**
```json
{
  "page": 0,
  "size": 10,
  "totalElements": 2,
  "content": [
    {
      "id": 1,
      "endpoint": "/api/v1/calculate",
      "request": "{\"numberOne\":100,\"numberTwo\":200}",
      "response": "{\"result\":330.0}",
      "error": null,
      "timestamp": "2025-11-08T10:45:00"
    }
  ]
}
```


### 💾 Base de datos PostgreSQL
###### **Tabla**: `history`
| Campo     | Tipo      | Descripción                   |
| --------- | --------- | ----------------------------- |
| id        | bigint    | Identificador autoincremental |
| endpoint  | varchar   | Endpoint invocado             |
| request   | text      | JSON con la solicitud         |
| response  | text      | JSON con la respuesta         |
| error     | text      | Mensaje de error (si aplica)  |
| timestamp | timestamp | Fecha y hora de la ejecución  |


## 🧠 Lógica funcional
### ✅ Cálculo con porcentaje dinámico

1. El servicio recibe `numberOne` y `numberTwo`.
2. Llama a un s**ervicio externo mockeado** (simulado) que devuelve un porcentaje (ej. `10%`).
3. Si el servicio externo falla, se usa el **último porcentaje cacheado (30 min)**.
4. Si tampoco hay valor en cache, se devuelve un **error 503**.

### ✅ Caché de porcentaje
* Implementado con un `ConcurrentHashMap`.
* TTL (tiempo de vida): **30 minutos**.
* Evita llamadas repetidas al servicio externo.

### ✅ Historial de llamadas
* Cada request se guarda asíncronamente mediante **`@Async`**.
* Esto evita afectar el tiempo de respuesta del endpoint.
* Los registros incluyen:
  * Endpoint
  * Request
  * Response o error
  * Fecha/hora


### 🧪 Tests unitarios
Ubicados en:

`src/test/java/com/tenpo/testtenpo/application/service/impl/CalculateServiceImplTest.java`

Ejecutar:
```bash
mvn test
```
Verifica:
* Cálculo correcto con porcentaje válido.
* Manejo de error cuando falla el servicio externo.
* Uso del valor cacheado.

### 📚 Documentación OpenAPI / Swagger
Archivo: `src/main/java/com/tenpo/testtenpo/infrastructure/config/OpenAPIConfig.java`
```java
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("Test Tenpo API")
                    .version("1.0")
                    .description("API REST para realizar cálculos de porcentaje dinámico, basandose en caché e historial de llamadas.")
                    .contact(new Contact()
                            .name("Backend Tenpo")
                            .email("backend_tempo@tenpo.com"))
                    .license(new License()
                            .name("Apache 2.0")
                            .url("http://springdoc.org")))
            .externalDocs(new ExternalDocumentation()
                    .description("Repositorio GitHub")
                    .url("https://github.com/tu-repo/test-tenpo"));
  }
}
```

### 🧰 Variables de entorno (`docker-compose.yml`)
```yaml
environment:
  - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/testtenpo
  - SPRING_DATASOURCE_USERNAME=postgres
  - SPRING_DATASOURCE_PASSWORD=postgres
  - SPRING_JPA_HIBERNATE_DDL_AUTO=update
  - SERVER_PORT=8080
```

### 📦 Ejecución local (sin Docker)
1️⃣ Asegúrate de tener PostgreSQL corriendo localmente
2️⃣ Configura `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/testtenpo
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
springdoc.swagger-ui.path=/swagger-ui.html
```
3️⃣ Ejecuta:
```bash
mvn clean spring-boot:run
```

### 📬 Colección Postman
Puedes importar el archivo `test-tenpo.postman_collection.json` con ejemplos listos:

**Endpoints incluidos:**

* #### POST `/api/v1/calculate`
* #### GET `/api/v1/history`

Cada request incluye ejemplos de entrada y salida, junto con encabezados estándar (`Content-Type: application/json`).


### 🧱 Tecnologías utilizadas
| Tecnología            | Descripción                      |
|-----------------------| -------------------------------- |
| **Java** 21           | Lenguaje principal               |
| **Spring Boot** 3.3.2 | Framework backend                |
| **Spring Data JPA**   | ORM con PostgreSQL               |
| **PostgreSQL** 15     | Base de datos relacional         |
| **Lombok**            | Simplificación de POJOs          |
| **Spring Cache**      | Cache en memoria                 |
| **Spring Async**      | Logging asíncrono                |
| **Springdoc OpenAPI** | Generación automática de Swagger |
| **Docker Compose**    | Orquestación de servicios        |
| **JUnit / Mockito**   | Pruebas unitarias                |

### 🧾 Licencia
Este proyecto fue desarrollado como una prueba técnica.
El código puede ser utilizado libremente para fines educativos o demostrativos.

### 👨‍💻 Autor
**Jesús David Noriega Calanche**

📧 [jssnoriega@gmail.com]

💼 Desarrollador Fullstack Front - Angular | Backend — Java | Spring Boot | Docker | PostgreSQL

🌐 Proyecto “Test-Tenpo” — 2025

