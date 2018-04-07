DROP TABLE IF EXISTS countries;
DROP TABLE IF EXISTS resp_messages;
DROP TABLE IF EXISTS req_messages;
DROP TABLE IF EXISTS resp_provider_messages;
DROP TABLE IF EXISTS req_provider_messages;
DROP SEQUENCE IF EXISTS countries_seq;
DROP SEQUENCE IF EXISTS req_message_seq;
DROP SEQUENCE IF EXISTS resp_message_seq;
DROP SEQUENCE IF EXISTS req_prov_message_seq;
DROP SEQUENCE IF EXISTS resp_prov_message_seq;

CREATE SEQUENCE countries_seq START 1;
CREATE SEQUENCE req_message_seq START 1000;
CREATE SEQUENCE resp_message_seq START 1000;
CREATE SEQUENCE req_prov_message_seq START 1000;
CREATE SEQUENCE resp_prov_message_seq START 1000;

CREATE TABLE countries
(
  id               INTEGER PRIMARY KEY DEFAULT nextval('countries_seq'),
  name             VARCHAR                 NOT NULL,
  capital            VARCHAR                 NOT NULL,
  currency         VARCHAR                 NOT NULL,
  currency_code        VARCHAR                 NOT NULL
);

CREATE TABLE req_messages (
  id          INTEGER PRIMARY KEY DEFAULT nextval('req_message_seq'),
  request_message     TEXT   NOT NULL,
  date_time   TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE resp_messages (
  id          INTEGER PRIMARY KEY DEFAULT nextval('resp_message_seq'),
  response_message     TEXT   NOT NULL,
  date_time   TIMESTAMP DEFAULT now() NOT NULL,
  req_message_id INTEGER NOT NULL,
  FOREIGN KEY (req_message_id) REFERENCES req_messages (id) ON DELETE CASCADE
);

CREATE TABLE req_provider_messages (
  id          INTEGER PRIMARY KEY DEFAULT nextval('req_prov_message_seq'),
  request_message     TEXT   NOT NULL,
  date_time   TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE resp_provider_messages (
  id          INTEGER PRIMARY KEY DEFAULT nextval('resp_prov_message_seq'),
  response_message     TEXT   NOT NULL,
  date_time   TIMESTAMP DEFAULT now() NOT NULL,
  req_prov_message_id INTEGER NOT NULL,
  FOREIGN KEY (req_prov_message_id) REFERENCES req_provider_messages (id) ON DELETE CASCADE
);

INSERT INTO countries (name, capital, currency, currency_code) VALUES
  ('Австралия','Мельбурн','Австралийский доллар','AUD'),
  ('Австрия','Вена','Евро','EUR'),
  ('Албания','Тирана','Лех','ALL'),
  ('Алжир','Алжир ','Алжирский    динар','DZD'),
  ('Ангола','Луанда','Кванза','AOA'),
  ('Андорра','Андорра','Евро','EUR'),
  ('Аргентина','Буэнос-Айрес','Аргентинский    песо','ARS'),
  ('Армения','Ереван','Драм','AMD'),
  ('Беларусь','Минск','Беларусский    рубль','BYN'),
  ('Бельгия','Брюссель','Евро','EUR'),
  ('Болгария','София','Лев','BGN'),
  ('Боливия','Ла-Пас','Боливиано','BOB'),
  ('Босния и    Герцеговина','Сараево','Марка','BAM'),
  ('Бразилия','Бразилиа','Реал','BRL'),
  ('Великобритания','Лондон','Фунт    стерлингов','GBP'),
  ('Венгрия','Будапешт','Форинт','HUF'),
  ('Вьетнам','Ханой','Донг','VND'),
  ('Германия','Берлин','Евро','EUR'),
  ('Греция','Афины','Евро','EUR'),
  ('Дания','Копенгаген','Крона','DKK'),
  ('Египет','Каир','Египетский    фунт','EGP'),
  ('Израиль','Иерусалим','Шекель','ILS'),
  ('Индия','Дели','Рупия','INR'),
  ('Индонезия','Джакарта','Индонезийская    рупия','IDR'),
  ('Иордания','Амман','Иорданский    динар','JOD'),
  ('Ирак','Багдад','Иракский    динар','IQD'),
  ('Иран','Тегеран','Иранский    реал','IRR'),
  ('Ирландия','Дублин','Евро','EUR'),
  ('Исландия','Рейкьявик','Исландская    крона','ISK'),
  ('Испания','Мадрид','Евро','EUR'),
  ('Италия','Рим','Евро','EUR'),
  ('Казахстан','Астана','Тенге','KZT'),
  ('Канада','Оттава','Канадский    доллар','CAD'),
  ('Кения','Найроби','Кенийский    шиллинг','KES'),
  ('Кипр','Никосия','Еврот','EUR'),
  ('Китай','Пекин','Юань','CNY'),
  ('Корея    (северная)','Пхеньян','Вон','KRW'),
  ('Корея    (южная)','Сеул','Вон','KRW'),
  ('Куба','Гавана','Кубинский    песо','CUP'),
  ('Кувейт','Кувейт','Кувейтский    динар','KWD'),
  ('Кыргызстан','Бишкек','Сом','KGS'),
  ('Латвия','Рига','Евро','EUR'),
  ('Лаос','Вьентьян','Новый кип','LAK'),
  ('Либерия','Монровия','Либерийский    доллар','LRD'),
  ('Ливия','Триполи','Ливийский    динар','LYD'),
  ('Литва','Вильнюс','Евро','EUR'),
  ('Лихтенштейн','Вадуц','Швейцарский    франк','CHF'),
  ('Люксембург','Люксембург','Евро','EUR'),
  ('Македония','Скопье','Денар','MKD'),
  ('Мальта','Валлетта','Евро','EUR'),
  ('Марокко','Рабат','Дирхам','MAD'),
  ('Мексика','Мехико','Мексиканский    песо','MXN'),
  ('Молдова','Кишинэу','Лей','MDL'),
  ('Монако','Монако','Евро','EUR'),
  ('Монголия','Улан-Батор','Тугрик','MNT'),
  ('Непал','Катманду','Непальская    рупия','NPR'),
  ('Нигерия','Абуджа','Найра','NAD'),
  ('Нидерланды','Амстердам','Евро','EUR'),
  ('Новая    Зеландия','Веллингтон','Новозеландский    доллар','NZD'),
  ('Норвегия','Осло','Норвежская    крона','NOK'),
  ('Панама','Панама','Бальбоа','PAB'),
  ('Польша','Варшава','Злотый','PLN'),
  ('Португалия','Лиссабон','Евро','EUR'),
  ('Россия','Москва','Рубль','RUB'),
  ('Румыния','Бухарест','Лей','RON'),
  ('Сербия','Белград','Динар','RSD'),
  ('Сингапур','Сингапур','Сингапурский    доллар','SGD'),
  ('Сирия','Дамаск','Сирийский    фунт','SYP'),
  ('Словакия','Братислава','Евро','EUR'),
  ('Словения','Любляна','Евро','EUR'),
  ('США','Вашингтон','Доллар','USD'),
  ('Таджикистан','Душанбе','Рубл','TJS'),
  ('Таиланд','Бангкок','Таиландский    бат','THB'),
  ('Тайвань','Тайпей','Тайваньский    доллар','TWD'),
  ('Тунис','Тунис','Тунисский    динар','TND'),
  ('Туркмения','Ашгабат','Манат','TMT'),
  ('Турция','Анкара','Турецкая    лира','TRY'),
  ('Узбекистан','Ташкент','Сум','UZS'),
  ('Украина','Киев','Гривна','UAH'),
  ('Уругвай','Монтевидео','Уругвайский    песо','UYU'),
  ('Филиппины','Манила','Филиппинский    песо','PHP'),
  ('Финляндия','Хельсинки','Евро','EUR'),
  ('Франция','Париж','Евро','EUR'),
  ('Чехия','Прага','Евро','EUR'),
  ('Чили','Сантьяго','Чилийский    песо','CLP'),
  ('Швейцария','Берн','Швейцарский    франк','CHF'),
  ('Швеция','Стокгольм','Шведская    крона','SEK'),
  ('Шри-Ланка','Коломбо','Рупия','LKR'),
  ('Эстония','Таллинн','Евро','EUR'),
  ('ЮАР','Претория','Южно-Африканский    рэнд','ZAR'),
  ('Япония','Токио','Йена','JPY');