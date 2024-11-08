# reactive-redis

# Configurar Docker

```bash
  docker-compose -f docker-compose-redis.yml up
```

```bash
  docker-compose -f docker-compose-redis.yml exec -it redis sh
```

# Redis Commands

```bash
redis-cli -h redis -p 6379
```

Listar todas las llaves

```bash
keys *
```

TTL

```bash
ttl key
```