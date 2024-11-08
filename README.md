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

TTL: Time to live

```bash
ttl key
```

Obtener el valor de una llave

```bash
get key
```

COnfiguración notify-keyspace-events

```bash
config set notify-keyspace-events KEA
```

Eliminar una llave

```bash
del key
```