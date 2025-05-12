# ActiveRecordCassandra
# Инструкции по запуску проекта.
Так как используется cassandra, то целесообразно иметь работающий докер на машине, на которой планируется запуск проекта.


docker pull cassandra.
<br>

После скачивания image, контейнер cassandra запускается с пробросом порта 9042 на локальную машину и пробросом 
файловой системы:
<br>

docker run --name cas735 -p 9042:9042 -td -v /mnt/hdd2/dockers/cassdata/:/var/lib/cassandra
<br>
/mnt/hdd2/dockers/cassdata/  в данном случае каталог на локальной машине, который отображается в директорию
/var/lib/cassandra в докер контейнере.
Имя --name cas735  выбрано произвольно для докер контейнера cassandra.
<br>
После старта контейнера необходимо подключиться к cqlsh cassandra в докере и создать ключевые кейспейсы и наполнить 
их тестовыми данными. Здесь <container_name> - имя докер контейнера из выхлопа команды (CONTAINER_ID)<br>
docker ps         ;
<br>
docker exec -ti <container_name> /bin/bash;
<br>
<br>
cqlsh;
<br>
cqlsh>

<br>
CREATE KEYSPACE cluster1 WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE cluster1.users (
uuid uuid PRIMARY KEY,
phones set<text>,
emails set<text>,
account text,
name text,
dateOfBirth timestamp,
hashPassword text,
currentBalance decimal,
initialBalance decimal
);

INSERT INTO cluster1.users
(uuid, phones, emails, account, name, dateOfBirth, hashPassword, currentBalance, initialBalance
) VALUES (uuid(), {'79684421213', '79113456777', '79213331211'}, {'emailme@yandex.ru'},
'ru1234555','Иван Сидоров', '1985-11-04', '1234567', 12000, 12000);

INSERT INTO cluster1.users
(uuid, phones, emails, account, name, dateOfBirth, hashPassword, currentBalance, initialBalance
) VALUES (uuid(), {'79111111113', '79253456657'}, {'viktor@gmail.com'},
'ru1234556','Виктор Петров', '1995-02-17', '1234567', 15000, 15000);

INSERT INTO cluster1.users
(uuid, phones, emails, account, name, dateOfBirth, hashPassword, currentBalance, initialBalance
) VALUES (uuid(), {'16467361779'}, {'pressoffice@donaldtrump.com'},
'us1234550','Donald Trump', '1946-06-14', '1234567', 1500000000, 1500000000);
INSERT INTO cluster1.users
(uuid, phones, emails, account, name, dateOfBirth, hashPassword, currentBalance, initialBalance
) VALUES (uuid(), {'74956063602'}, {'post_sf@gov.ru'},
'ru7777777','Владимир Путин', '1952-10-07', '1234567', 150000000000, 150000000000);
<br><br><br><br>

После вставки данных выйти из cqlsh и перейти в каталог ~/.cassandra:
<br>
cd ~/.cassandra;
И выполнить команду:
<br>
echo '[ui]

;; Used for displaying timestamps (and reading them with COPY)
datetimeformat = %d-%m-%Y %H:%M:%S%z

;;Used for used displaying timestamps generell

time_format = %d-%m-%Y %H:%M:%S%z' >> cqlshrc

<br><br><br>

Это создаст файл cqlshrc для отображения даты в нужном формате в cqlsh.
<br>

Для сборки прокета используется maven & JDK 17.
<br>
Сборка производится командой mvn package в корне git-репозитория данного проекта.
Для запуска следует достать из target fat-jar и запустить его командой
<br>
java -jar testexample-0.0.1-SNAPSHOT.jar




# Описание использованных технологий (если выходили за рамки базового стека).

В данном проекте используется Apache Cassandra - NOSql база данных, длядемонстрации того что NOSql решения в 
чем-то могут быть лучше традиционного sql. В частности это позволило использовать всего одну 
таблицу(или семейство колонок) вместо 4 как в ТЗ.

# Логику принятых решений (если были нестандартные подходы).
Так как приложение с довольно ограниченной бизнес-логикой , был испробован паттерн "Активная Запись"
Active Record по М. Фаулеру, где часть бизнес логики лежит непосредственно в Entity таблицы(статические методы).


# Доп сведения.
Порядок тестирования:<br>
1.
Для получения jwt токена из браузера перейти по адресу
http://localhost:12000/login

Ввести логин пароль, получить gwt токен. eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoiNDMxZmZmNjUtOGRhZS00ZDU3LWI3NjktOWZkMWY2MTk3NTZiIiwiaWF0IjoxNzQ3MDU4MzY1LCJleHAiOjE3NDcwNTkyNjV9.LAaEHHotH6voJjOMakRhRLCMmQxmc9lmEtDGHGxTfhE
По умолчанию пароль 1234567, логин email или phone.
<br><br>
2. Для добавления email , phone
Запрос http://localhost:12000/add?param=new@email.zdz
   <br><br>
3. Обновление, смена email, phone
   Запрос   http://localhost:12000/change?paramToChange=new@email.zdz&targetValue=D.Trump@aol.com
   <br><br>
4. Удаление
http://localhost:12000/delete?param=D.Trump@aol.com
   <br><br>
5. Поиск
http://localhost:12000/search?email=post_sf@gov.ru
   <br><br>
6. Transfer денег
   http://localhost:12000/transfer?sendToUuid=96b5ed38-dc45-4f45-8e1b-787deaf87134&amount=100000
   <br><br>
Скриншоты приложены в папке docs






