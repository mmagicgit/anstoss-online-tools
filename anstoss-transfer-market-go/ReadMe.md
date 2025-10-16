# Build Cross
env GOOS=linux go build

# Build
CGO_ENABLED=0 go build