# Jelajah Bandung API

This is a Spring Boot application for managing tourism spots in Bandung. It provides a RESTful API for creating, reading, updating, and deleting tourism spot data.

## features

- **Technology Stack**:
  - Java 21
  - Spring Boot 3.5.9
  - PostgreSQL
  - Spring Data JPA
  - Spring Security
  - Lombok
- **API Capabilities**:
  - CRUD operations for `Wisata` (Tourism Spots).
  - Search/Filter tourism spots by name.
  - Standardized API Response structure.
  - Global Exception Handling.
  - Input Validation.

## Prerequisites

- Java 21 or later
- Maven 3.6 or later
- PostgreSQL installed and running
- Database created named `jelajahbandungdev` (or update `application.properties`)

## Configuration

The database configuration is located in `src/main/resources/application.properties`.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jelajahbandungdev
spring.datasource.username=postgres
spring.datasource.password=p4ssw0rd
```

## Running the Application

1.  **Clone the repository**:

    ```bash
    git clone <repository-url>
    cd jelajahbandung
    ```

2.  **Build the project**:

    ```bash
    mvn clean install
    ```

3.  **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

The application will start on port `8080`.

## API Documentation

### Base URL

`http://localhost:8080/api/v1`

### Endpoints

#### Tourism Spots (Wisata)

| Method   | Endpoint       | Description                    | Request Param                      | Request Body         |
| :------- | :------------- | :----------------------------- | :--------------------------------- | :------------------- |
| `GET`    | `/wisata`      | Get all tourism spots          | `nama` (optional) - Filter by name | N/A                  |
| `GET`    | `/wisata/{id}` | Get details of a specific spot | N/A                                | N/A                  |
| `POST`   | `/wisata`      | Create a new tourism spot      | N/A                                | `WisataRequest` JSON |
| `PUT`    | `/wisata/{id}` | Update an existing spot        | N/A                                | `WisataRequest` JSON |
| `DELETE` | `/wisata/{id}` | Delete a tourism spot          | N/A                                | N/A                  |

### Example Request Body (JSON)

**POST /api/v1/wisata**

```json
{
  "nama": "Gedung Sate",
  "deskripsi": "Iconic government building in Bandung",
  "alamat": "Jl. Diponegoro No.22",
  "jamBuka": "08:00 - 16:00",
  "hargaTiket": 5000.0,
  "urlGambar": "https://example.com/gedungsate.jpg",
  "latitude": -6.9024812,
  "longitude": 107.61881
}
```

### Standard Response Format

**Success**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

**Error**

```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

## Security

Currently, all endpoints are configured to permit all requests (`permitAll`) for development purposes.

## License

[MIT](LICENSE)
