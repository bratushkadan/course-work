# Запуск

## Шаги

1. `cd backend`
2. `docker run --name mongo -d -v mongodbdata:/data/db -v $(pwd)/mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js -e MONGO_INITDB_ROOT_USERNAME=danila -e MONGO_INITDB_ROOT_PASSWORD=bratushka -e MONGO_INITDB_DATABASE=admin -p 27017:27017 mongo`
3. /Users/bratushkadan/Desktop/university/course-work/frontend/index.html

## Пересоздание тома с данными монги

```sh
docker volume create mongodbdata
```
