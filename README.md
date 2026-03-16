# IT Courses

Учебное Android-приложение — каталог онлайн-курсов.

## Стек

- **Kotlin** + **Jetpack Compose**
- **Navigation Compose** — single-activity навигация
- **Retrofit + Gson** — загрузка курсов из API
- **Room** — локальный кэш, офлайн-режим
- **Coroutines + Flow** — реактивный UI
- **Koin** — внедрение зависимостей
- **Clean Architecture** — разделение на data / domain / presentation
- **Multi-module** — `:app`, `:feature:login`, `:feature:courses`, `:feature:account`

## Функциональность

- Лента курсов с сортировкой по дате публикации
- Избранное с сохранением между сессиями
- Детальная страница курса с кнопкой «Начать»
- Экран профиля с начатыми курсами и прогрессом прохождения
- Офлайн-режим: данные кэшируются в Room, UI читает только из базы

## Требования

- Android Studio Hedgehog или новее
- minSdk 26 / targetSdk 36
