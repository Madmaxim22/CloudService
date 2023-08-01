<h1 align="center"><b> Сервис хранения файлов в облаке </b> </h1>

<p align="center">

<img src="https://img.shields.io/badge/made-Madmaxim22-blue">
<img src="https://img.shields.io/badge/JDK-17-blue">
<img src="https://img.shields.io/github/stars/Madmaxim22/CloudService?color=yellow">
<img src="https://img.shields.io/github/forks/Madmaxim22/CloudService?color=blue">
<img src="https://img.shields.io/github/watchers/Madmaxim22/CloudService?color=blue">
<img src="https://img.shields.io/github/downloads/Madmaxim22/CloudService/total?color=blue">
</p>

<h2 align="center"><b>Описание проекта</b></h2>
Получи доступ к файлам из любой точки мира!   

Предназначен для хранения и извлечения файлов в облачном хранилище.

Проект реализова с использованием следующих Api:
- Spring boot;
- Spring security;
- Spring data;
- Spring mvc;
- Jwwt;
- Lombok;
- Liquebase;
- MySql;
- Junit;
- Testconteiners;
- Docker;
- Docker-compose.

<h2 align="center"><b>Как установить и запустить проект</b></h2>

Для запуска приложения необходимо:
- установить Docker Compose;
- сделать git pull приложения;
- перейти в корневую папку приложения;
- собрать проект выполнив команду **docker-compose build**;
- выполнить команду **docker compose up -d**
- перейти по адресу http://localhost:8081/

<h2 align="center"><b>Справочно</b></h2>

- структура проекта схематично отображена в директории documentation;  
- ТЗ проекта представлено в директории documentation;  
- файлы системных логов записываются:   
  - В Windows тома находятся по умолчанию в **\\wsl.localhost\docker-desktop-data\data\docker\volumes\moneytransferservice_logs_app\_data**;  
  - В Linux тома находятся по умолчанию в **/var/lib/docker/volumes/**;  
- по умолчанию сервер запускается на порту 8080;  
- процесс обмена backend'ом и frontend'ом описан в CloudServiceSpecification.yaml.   
 

<h2 align="center"><b> Научился </b></h2>

1. Создавать REST-приложение;
2. Использовать библиотеку валидации jakarta.validation.Valid;
3. Обрабатывать Json объекты с использованием аннотаций;
4. Использовать @CrossOrigin аннотацию для совместного использования ресурсов между разными источниками;
5. Настраивать политику обратки входящих запросов;
6. Реализовывать аутентификацию с использование токенов;
7. Создавать и настраивать фильтры в spring security;
8. Реализовал разделение активных профилей для разработки и тестов;
9. Создавать Dockerfile для backend'а и frontend'а;
10. Создавать композиции с помощью docker-compose.yaml;
11. Использовать тома для сохранения лог фалов приложения;
12. Использовать spring data jpa;
13. Создавать собственные запросы к MySql;
14. Создавать и управлеять связанными таблицами;
15. Создавать миграции с использованием Liqeibase;
16. Фильтровать миграции в зависимости от context'a;
17. Настройка логирования(разделение по уровням логирования, логирование в файл и в консоль) с использованием Logback;
18. Интеграционное тестирование приложения на уровне представления с использованием MockMvc;
19. Интеграционное тестирование с использованием аннотации @Testcontainers;