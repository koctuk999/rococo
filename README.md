# Rococo

* Web-приложение для хранения музеев, художников и их картин.
* Просмотр контента доступен для неавторизованных пользователей.
* Добавление и редактирование требует oauth2 авторизацию.

<img src="rococo.png" width="800">

### Взаимодействие компонент

* Бэкенд реализован в виде микросервисов, c которыми фронт взаимодействует с помощью gateway
* Межсервисное взаимодействие организовано с помощью grpc
* Фронт взаимодйствует с бэкендом через rest
* Сервис rococo-userdata получает данные о пользователях из kafka-топика `users`, в который пишет rococo-auth
* Сервис rococo-painting получает данные по музеям и художникам (из rococo-museum и rococo-artist) и кэширует их. При
  обновлении музея или художника, соответствующий сервис пишет в kafka-топик `updated` id-элемента и название сервиса, а
  rococo-painting вычитывает их и рефрешит кэш для обновленного элемента.

Полная схема:
<img src="rococo-components.png" width="1000">

## Запуск сервисов

Сервисы можно запустить двумя способами: через IDE и docker-compose

### Запуск через IDE

1. Создать volume для сохранения данных из БД

```shell
docker volume create rococo-data
```

2. Нужно подготовить контейнеры с кафкой и бд, а также запустить фронт. Для этого можно просто выполнить
   скрипт `./local_preparing_script.sh`

3. Создать БД для всех компонент:
```postgresql
create database "rococo-auth" with owner postgres;
create database "rococo-userdata" with owner postgres;
create database "rococo-country" with owner postgres;
create database "rococo-museum" with owner postgres;
create database "rococo-artist" with owner postgres;
create database "rococo-painting" with owner postgres;
```

4. Запустить все сервисы по очереди, с spring-profile=`local`

### Запуск в Docker-контейнерах

1. Необходимо прописать в /etc/hosts на запускаемой машине элиасы для docker-имён:
```
127.0.0.1       client.rococo.dc
127.0.0.1       auth.rococo.dc
127.0.0.1       gateway.rococo.dc
127.0.0.1       country.rococo.dc
127.0.0.1       museum.rococo.dc
127.0.0.1       artist.rococo.dc
127.0.0.1       painting.rococo.dc
127.0.0.1       rococo-db
```
2. Поднять контейнеры через docker-compose, выполнив скрипт `./up_docker-services.sh`

* При запуске без параметров, образы спулятся из registry
* При запуске с параметром build `./up_docker-services.sh build` образы будут собраны через jib
* А при запуске с параметрами build и push `./up_docker-services.sh build` собранные образы будут запушены в registry

## Запуск тестов

[**Документация по тестовому фреймворку**](test-framework-documentation.md)
 
_Для запуска тестов необходимо полностью поднятое приложение (все сервисы и клиент)_

 Выполнить скрипт `./test_run.sh`, который запустит прогон тестов и сформирует allure-отчет.
 
* Без параметров запустятся все тесты для локально-поднятого (через IDE) приложения
* С параметром docker `./test_run.sh docker` запустятся все тесты для приложения, запущенного в docker-контейнерах
* Для запуска тестов по тэгу можно запустить скрипт, указав в качестве параметра соответствующий тэг (как для локального запуска, так и для запуска в контейнерах)
`./test_run.sh museum_acceptance` или `./test_run.sh docker museum_acceptance`. Список тэгов можно посмотреть [здесь](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/core/TestTag.java).

