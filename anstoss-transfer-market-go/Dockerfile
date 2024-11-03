FROM golang:1.23.2-alpine AS builder
WORKDIR /app/
COPY . .
RUN go build

FROM scratch
COPY --from=builder /app/anstoss-transfer-market-go /anstoss-transfer-market-go
EXPOSE 1111
CMD ["/anstoss-transfer-market-go"]
