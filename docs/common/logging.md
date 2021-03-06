[Главная](../main.md)

#  Логгирование

Логгирование в logcat осуществляется с помощью [Logger][logger].

В обязательном порядке следует логгировать:
* Url запросов и статус ответов сервера

* NonFatalExceptions (состояния приложения, когда часть его функционала
неправильно отработала, но при этом приложение может продолжать работать),
например - неправильный парсинг ответа сервера. В этом случае логгируется еще
и [тело ответа сервера][response].

* Основные события экранов

Все эти логи следует отсылать на удаленный сервер (мы используем
[*Firebase Crashlytics*][firebase-crashlytics]). Трекинг этих событий
позволяет быстрее понять, где и из-за чего произошла та или иная ошибка.

**Важно** :
- Оставлять логи, которые не несут важной информации `запрещено`.
- Не оставляем пустые обработчики ошибок rx потока.
- Не пишем e.printStackTrace().
- __Не забиваем Firebase Crashlytics ненужными NonFatal__.

*Примечание*: для того, чтобы не загрязнять Firebase Crashlytiсs ошибками из Debug-версии
приложения, можно выделить эту версию в отдельный проект(добавить постфикс к package-name
и завести это приложение в Firebase).

*Совет*: удобно логгировать также некоторые пользовательские данные, например
id, email при входе в аккаунт (для быстрой связи с пользователем, у которого
произошла ошибка), какие-либо пары ключ-значение(доп. информация о приложении
на момент сбоя).

Для отслеживания ANR применяется библиотека AnrWatchDog. При детектировании
ANR соответствующий NonFatal следует отправлять на удаленный сервер.

[logger]: ../../logger/README.md
[response]: ../../deprecated/converter-gson/README.md
[firebase-crashlytics]: https://firebase.google.com/docs/crashlytics