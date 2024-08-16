## Dockerfile

```dockerfile
# Stage 1: Build the Go Application
FROM golang:1.22.1-alpine AS builder

WORKDIR /app

COPY main.go go.mod ./

RUN go mod tidy

RUN go build -o tiny-service .

# Stage 2: Create the Final Image
FROM scratch

WORKDIR /app

COPY --from=builder /app/tiny-service /app/tiny-service

COPY public ./public

CMD ["/app/tiny-service"]
```


## Docker Compose File

```yaml
version: "3.9"

services:
  redis-server:
    image: redis:7.2.4
    environment:
      REDIS_ARGS: --save 30 1
    ports:
      - "6379:6379"
    volumes:
      - ./data:/data

  webapp:
    build: .
    ports:
      - "8080:8080"
    environment:
      MESSAGE: "Hello from Docker Compose"
      TITLE: "My favorite restaurants"
      REDIS_URL: redis-server:6379
    depends_on:
      - redis-server

```


**Explanation:**

* **Dockerfile:** 
    * The first stage uses the `golang:1.22.1-alpine` image as a base and sets the working directory to `/app`.
    * It copies the `main.go` and `go.mod` files from the host's current directory to the `/app` directory in the container. 
    * The `go mod tidy` command ensures consistent module dependencies.
    * Finally, it builds the Go application with the output binary named `tiny-service`.
    * The second stage uses a `scratch` image and copies the built binary from the previous stage to the current working directory. It also copies the `public` directory from the host's current directory to `/app/public` in the container. 
    * The final command executes the `tiny-service` binary.

* **Docker Compose File:**
    * Defines two services: `redis-server` and `webapp`.
    * **redis-server:** Uses the official Redis image with tag `7.2.4`, sets an environment variable to configure Redis, exposes port 6379 both internally and externally, and mounts a volume from the host's `/data` directory to `/data` inside the container.
    * **webapp:** Builds its image using the Dockerfile in the current directory, exposes port 8080 both internally and externally, sets environment variables for message, title, and Redis connection, and depends on `redis-server` to ensure it starts after Redis is up.

**Note:**  Make sure you have Docker installed and running before building or deploying these containers. You can use the following commands: 
* **Build the image:** `docker build -t my-app .` (Replace `my-app` with your desired image name)
* **Run the container:** `docker run -p 8080:8080 my-app`



