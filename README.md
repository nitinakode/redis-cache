Sure! Here's the `README.md` with the necessary details:

```markdown
# Product Service with Redis Caching

This project demonstrates the integration of Redis caching with a Spring Boot application to store and retrieve product data efficiently.

## Redis

Redis is an open-source, in-memory key-value store that is commonly used for caching purposes. It helps improve application performance by reducing database load for frequently accessed data.

### Docker Command to Run Redis

Run Redis with the following Docker command:

```bash
docker run -d --name redis -e REDIS_PASSWORD=yourpassword -p 6380:6379 redis:latest redis-server --requirepass yourpassword
```

### Redis Configuration in `application.yml`

```yaml
redis:
  host: localhost
  password: yourpassword
  port: 6380
```

## PostConstruct

The `@PostConstruct` annotation in Spring is used to define methods that should be executed after the bean's initialization. In this project, it is used to sync product data from the database to Redis when the application starts up.

## API Endpoints

### 1. **GET /redis/all-products**
   - Fetch all product names stored in Redis.

### 2. **POST /save**
   - Create a new product and store it in both the database and Redis.

### 3. **DELETE /delete/{id}**
   - Delete a product by its ID from both the database and Redis.

### 4. **DELETE /redis/clear-all-cache**
   - Clear all cached product data from Redis.

## Conclusion

This service integrates Redis caching to speed up product retrieval and ensure consistency between the database and Redis. The `@PostConstruct` method ensures the cache is populated with the latest product data on application startup.
