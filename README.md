# LetMeCodeInterviewtask. Задание
***
**Платформа:** Android

**Язык:** Java

**Задача:** Приложение подгружает и отображает списки рецензий на фильмы и их авторов.

**Rest API:**
[The New York Times Developer Network](https://developer.nytimes.com/)

**Консоль для тестирования API:**
[API Console](https://developer.nytimes.com/movie_reviews_v2.json#/Console/GET/reviews/%7Bresource-type%7D.json)

**Пример API для получения списка критиков:**
[Example](https://api.nytimes.com/svc/movies/v2/critics/all.json?api-key=99dba655b7e54a89a90b8dfd613b2ac3)

**Пример API для получения списка рецензий:**
[Example](https://api.nytimes.com/svc/movies/v2/reviews/search.json?api-key=99dba655b7e54a89a90b8dfd613b2ac3)


## Вкладка "Рецензии". 
>1.	Постраничная загрузка; +
>2.	Обновления списка по свайпу (pull to refresh) +
>3.	Поиск рецензии по ключевому слову (api request) +
>4.	Фильтр рецензий по дате выхода публикации +/-
>5.	Форматы даты и времени должны соответствовать дизайну (см Рис.1). +

_Примеры фильтрации, поиска и т. п. доступны в api консоли._

## Вкладка "Критики". (См Рис.2)
>1.	Постраничная загрузка; +
>2.	Обновление списка по свайпу (pull to refresh) +
>3.	Поиск критика по имени (api request) +
>4.	Клик по ячейке критика открывает новый экран (см. Рис.3) +

_Примеры поиска доступны в api консоли._


## Экран "Критик". (См Рис.3)
>1.	В шапке показать био, фото, имя и статус критика +
>2.	Загрузить список рецензий критика (постраничная загрузка) +

## Макет
***Повторение дизайна приложенных к заданию макетов не обязательно, это будет дополнительным плюсом. Целью макетов является визуальное объяснение данного тестового задания.*** +

![Техническое задание](http://protasov-dev.ru/img_for_github/tz.jpg)

# LetMeCodeInterviewtask. Результат

---
**Начал выполнять:** 18.06.2018
**Закончил выполнять:** 23.06.2018

**Устройства, на которых проводилось тестирование:**

>1. Elephone P9000 (Android 6.0) (Real device)
>2. Google Nexus 5X (Android 6.0) (Emulator: Genymotion)

## Вкладка "Рецензии"

![Рецензии](http://protasov-dev.ru/img_for_github/reviewes.jpg)

## Вкладка "Критики"

![Критики](http://protasov-dev.ru/img_for_github/critics.jpg)

## Экран "Критик"

![Критик](http://protasov-dev.ru/img_for_github/critic.jpg)

## Экран "Просмотр рецензии"

    Сделал дополнительный экран "Просмотр рецензии". Иначе так получается скучное приложение. 
    При клике на заголовке рецензии можно посмотреть полную рецензию с сайта NYTimes из WebView.

![Просмотр рецензии](http://protasov-dev.ru/img_for_github/review-page.jpg)

## Используемые технологии и языки

> Android App
>
> * Java
> * REST API
> * Glide v4
> * Gson

## Комментарий

    Написал как умею, но готов учиться и совершенствоваться;)
    
## APK файл

[Скачать с MEGA](https://mega.nz/#!JyIS0SRL!qZ1llSF5NrxZD6JIT4l_sYpYC7ZhXlrgw3bQ9J3EffI)
