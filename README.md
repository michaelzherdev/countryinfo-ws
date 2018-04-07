### Задание

Необходимо реализовать веб-сервис c использование технологии JAX-WS.
Сервис должен представлять собой серверное приложение, желательно использовать
Tomcat или glassfish, в крайнем случае – любой другой бесплатный/opensource сервер
приложений.

Веб-сервис должен обеспечить прием и обработку запроса по сбору информации
о полученной стране, а именно – вернуть информацию по курсу валюты по отношению
к российскому рублю на текущей день, используемой в стране и прогноз погоды в
столице на текущий день.

В качестве входного параметра должно быть наименование страны (например,
«США»), в результате должна вернуться информация, содержащаяся в ответах веб-
сервисов погоды (Погода для города «Вашингтон») и курсов валют (RUR-USD в данном
случае), а также должен быть уникальный идентификатор ответа.

Для запроса актуальных курсов валют рекомендуется использовать информацию
с сайта центрального банка (http://www.cbr.ru/scripts/Root.asp?Prtid=DWS).
Для запроса актуального прогноза погоды можно использовать веб-сервисы gismeteo
(http://ws.gismeteo.ru).

Дополнительные требования к реализации:

Веб-сервис должен выполнять логирование в СУБД по следующим
требованиям:
* Записывается в лог каждый запрос и сгенерированный ответ,
запросы и ответы полученные от поставщиков веб-сервисов;
* Каждой записи должен присваиваться уникальный идентификатор
– должен совпадать с идентификаторами, отправляемые в ответ;
* Должна быть отметка по времени для каждой записи;
* Должна быть возможность сгруппировать все запросы в рамках
одной сессии (одна сессия – период от получения исходного
запроса с информацией о стране до выдачи ответа на него,
включая все промежуточные запросы и ответы в сторонние
сервисы);
* Содержать статус обработки сообщения (Успешно/неуспешно);
* Содержать информацию об ошибке, если таковая имеется;
* В качестве СУБД желательно использовать MySQL или MS SQL, структура
БД – свободная;
* При необходимости можно использовать дополнительные внутренние
справочные сведения, если потребуется;
* Реализовать обработку ошибок (веб-сервис поставщика не ответил,
приняты некорректные сведения, ответ не валиден);
* Форматы входных и выходных сообщений должны быть структурированы
– содержать блок Courses – ответ от веб-сервиса курсов валют, Weather –
ответ от веб-сервиса о погоде.


#### Работа с поставщиками веб-сервисом
<b>Запрос курсов валют</b> с сайта Центрального Банка рекомендуется выполнять с
использованием метода GetCursOnDate, сервис
http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx. Из ответа забирать только блок
«ValuteCursOnDate», актуальный для исходного запроса (в исходном примере – для
«Доллара США»).

<b>Запрос информации о погоде</b> рекомендуется выполнять следующим образом:
1. Выполнить регистрацию приложения с использованием метода RegisterHHUser,
веб-сервис http://ws.gismeteo.ru/Registration/Register.asmx
2. Полученный ключ использовать в дальнейшем при выполнении запросов
(значение поля key)
3. Выполнить запрос для кода региона с использованием метода FindByNameFull,
веб-сервис http://ws.gismeteo.ru/Locations/Locations.asmx (параметры запроса:
serial - значение, полученное на шаге 2, name – название города, count –
количество результатов в ответе, language – значение «RU»);
4. Взять первый блок подходящий по названию страны блок data/LocationInfoFull,
получить значение поля id
5. Выполнить запрос для получения информации о погоде с использованием метода
GetHHForecast, веб-сервис http://ws.gismeteo.ru/Weather/Weather.asmx
(параметры запроса: serial - значение, полученное на шаге 2, location –
значение поля id, полученное на шаге 4)
6. Забрать значение блока data в ответ.

#### Результат
В качестве результата необходимо:
* примеры запросов на вход и на выход из реализованного веб-сервиса
(файлы XML);
* скрипты для развертыванию БД на выбранной СУБД (создание структуры
и наполнение, если требуется);
* описание по решению, если считаете нужным. 


#### Запуск
* Создать БД countryinfo
* Запустить на ней скрипт resources/init_db.sql
* Запуск сервера через IDE: класс ServicePublisher
* Запуск клиента через IDE: класс WsClient