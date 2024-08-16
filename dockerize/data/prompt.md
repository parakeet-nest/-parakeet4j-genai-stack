Create a multi-stage Dockerfile for a Go application. The Dockerfile should follow these steps:

1. **First stage:**
   - Use the `golang:1.22.1-alpine` image as the base, naming this stage `builder`.
   - Set the working directory to `/app`.
   - Copy the `main.go` and `go.mod` files from the current directory on the host to the working directory in the container.
   - Run the following commands:
      - Use `go mod tidy` to ensure that the module dependencies are cleaned up and consistent.
      - Build the Go application with the output binary named `tiny-service`.

2. **Second stage:**
   - Use the `scratch` image as the base to create a minimal final image.
   - Set the working directory to `/app`.
   - Copy the `tiny-service` binary from the `/app` directory in the `builder` stage to the current working directory in this stage.
   - Copy the `public` directory from the current directory on the host to the `/app/public` directory in the container.
   - Set the container's command to execute the `tiny-service` binary.

Output the configuration as a Dockerfile.

Create a Docker Compose file with two services: `redis-server` and `webapp`.

1. The `redis-server` service should:
   - Use the official Redis image with the tag `7.2.4`.
   - Set an environment variable `REDIS_ARGS` to `--save 30 1` to configure Redis to save the dataset every 30 seconds if at least 1 key has changed.
   - Expose port `6379` both internally and externally.
   - Mount a volume from `./data` on the host machine to `/data` inside the container.

2. The `webapp` service should:
   - Build its image from the Dockerfile located in the current directory.
   - Expose port `8080` both internally and externally.
   - Set environment variables:
      - `MESSAGE` to "Hello from Docker Compose".
      - `TITLE` to "My favorite restaurants".
      - `REDIS_URL` to `redis-server:6379` to connect the web application to the Redis server.
   - Specify that it depends on the `redis-server` service to ensure Redis is up before starting the webapp.

Output the configuration as a YAML file.

