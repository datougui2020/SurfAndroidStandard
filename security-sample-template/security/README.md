# Security
Используется для обеспечения безопасности приложения.

# Использование
#### Основные классы:

1. [AppDebuggableChecker](../security/src/main/java/ru/surfstudio/android/security/app/AppDebuggableChecker.kt)- класс, проверяющий debuggable-флаги приложения при его запуске.
2. [RootChecker](../security/src/main/java/ru/surfstudio/android/security/root/RootChecker.kt) - проверяет наличие рут-прав на устройстве.
3. [KeyEncryptor](../security/src/main/java/ru/surfstudio/android/security/crypto/KeyEncryptor.kt) - абстрактный класс для реализации безопасного [Encryptor'a](../../filestorage/src/main/java/ru/surfstudio/android/filestorage/encryptor/Encryptor.kt).
4. [CertificatePinnerCreator](../security/src/main/java/ru/surfstudio/android/security/ssl/CertificatePinnerCreator.kt) - класс, создающий CertificatePinner для OkHttpClient для реализации ssl-pinning.
5. [SessionManager](../security/src/main/java/ru/surfstudio/android/security/session/SessionManager.kt) - Менеджер для отслеживания сессии Activity.
6. [SecurityUiExtensions](../security/src/main/java/ru/surfstudio/android/security/ui/SecurityUiExtensions.kt) -  - Утилиты для реализаци безопасного UI.

[Пример использования модуля](../security-sample)

#### Подключение:
Выгрузка модуля как артефакта не предусмотрена. Модуль является примером реализации описанных ниже рекомендаций.


# Security tips
1. В приложении следует использовать только Explicit [Intent](https://developer.android.com/reference/android/content/Intent). Для открытия [Activity](https://developer.android.com/guide/components/activities/intro-activities), работы с [Service](https://developer.android.com/reference/android/app/Service), [BroadcastReceiver](https://developer.android.com/guide/components/broadcasts).
Implicit Intent следует использовать, когда необходимо запустить компонент не являющийся частью вашего приложения: Email клиент, Карты, Sharing данных.
2. BroadcastReceiver, Service, ContentProvider - должны иметь флаг exported = false, ContentProvider'у еще необхоимо добавить protectionLevel = "signature», дабы предотвраить утечку данных за пределы вашего приложения.
3. Использовать BroadcastReceiver для рассылки Intent, которые не должны выйти за пределы приложения строго не рекомендуется, для этого есть [LocalBroadcastManager](https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager).
4. Логгирование должно работать **только на debuggable** сборках.
5. Для релизных версий должен быть подключен [ProGuard](https://jebware.com/blog/?cat=16)
6. Любая [Sensitive data](https://en.wikipedia.org/wiki/Information_sensitivity) должна храниться в защищенном
[internal storage](https://developer.android.com/training/data-storage/files).
7. Пароли, пинкоды и.т.п строго не рекомендуется хранить на устройстве. Если всё же это необходимо - такие данные должны быть зашифрованы. См. п. 6.
[Пример](../security-sample/src/main/java/ru/surfstudio/android/security/sample/interactor/storage/ApiKeyStorageWrapper.kt)
8. Activity с важной информацией должны быть защищены с помощью флага [FLAG_SECURE](https://developer.android.com/reference/android/view/WindowManager.LayoutParams).
Флаг запрещает делать скриншоты с этого Activity и в свернутом виде контент окна тоже не будет отображаться.
9. Финансовые приложения, приложения имеющие дело с важными персональными данными пользователя должны иметь проверку на [ROOT](https://ru.wikipedia.org/wiki/Root).
Root права дают возможность залезть в закрытую область памяти, доступную только приложению, где хранится кеш приложения, базы данных, SharedPreferences итп. 
В случае обнаружения root прав на устройстве, приложение должно предупредить пользователя о рисках, связанных с этим.
<br>**Рекомендации при обнаружении рут прав:**
<br> 1. Прекратить хранение важных данных на устройстве.
<br> 2. Отчистить существующие хранилища.
<br> 3. Функционал приложения связанный с финансовыми операциями, работой с персональными и секретными данными должен быть ограничен/отключен.
<br> 4. Полностью прекратить доступ пользователя в приложение.
<br> Проверку root прав следует делать при помощи NDK, т.к проверка будет проходить на более низком уровне.
Использование проверки при помощи Java кода, не эффективна, т.к существуют способы ["спрятать"](https://github.com/devadvance/rootcloak) root права от проверяющего кода на Java.
[Пример](../security-sample/src/main/java/ru/surfstudio/android/security/sample/ui/screen/main/MainPresenter.kt)
10. Приложение должно быть защищено от debug'a и запуске на эмуляторе.
[Пример использования AppDebuggableChecker](../security-sample/src/main/java/ru/surfstudio/android/security/sample/app/CustomApp.kt)
11. Поля ввода секретных данных, должны поддерживать валидацию введеных данных с помощью Regex, InputFilter, ограничения ввода спецсимволов и.т.п.
<br>Данная мера поможет минимизировать риск [SQL injection](https://ru.wikipedia.org/wiki/Внедрение_SQL-кода).
Также у полей должны отсутствовать пункты "копировать", "вырезать" в контекстом меню, и отключить вызов контекстного меню по долгому нажатию. См.
[Пример использования](../security-sample)
13. Захардкоженные ключи рекомендуется хранить в нативном коде при помощи NDK.
13. Для ввода пинкода рекомендуется использовать кастомную клавиатуру.
14. Для дополнительной безопасности сетевого соединения используется SSL pining (Certificate pinning).
Certificate pinning – это внедрение SSL сертификата, который используется на сервере, в код мобильного приложения.
В этом случае приложение будет игнорировать хранилище сертификатов устройства, 
полагаясь только на свое хранилище и позволяя создать защищенное SSL соединение с хостом, подписанным только сертификатом, что хранится в самом приложении.
16. Ближе к релизу стоит прогнать приложение на "дыры" в безопасности с помощью пентест инструментов, например [DROZER](https://labs.mwrinfosecurity.com/tools/drozer/).
17. На релизных сборках отключать DebugScreen.
18. Приложение должно следить за сессией пользователя.
[Пример использвания SessionManager](../security-sample/src/main/java/ru/surfstudio/android/security/sample/app/CustomApp.kt)

<br>

# OWASP TOP

| №  | Уязвимость  | Описание  | Security tips  |
|---|---|---|---|
| М1 | Обход архитектурных ограничений (Improper Platform Usage)         | Эта уязвимость охватывает злоупотребление особенностями платформы, обхода ограничений или неиспользования систем контроля управления безопасности платформы. Затрагивает системы контроля безопасности, которые являются частью мобильной операционной системы. | см. П1, П2, П3  |
| М2 | Небезопасное хранение данных (Insecure Data Storage)              | К ней относятся небезопасное хранение и непреднамеренные утечки данных. | см. П4, П6, П7, П9, П13  |
| М3 | Небезопасная передача данных (Insecure Communication)             | Недостаточное подтверждение достоверности источников связи, неверные версии SSL, недостаточная проверка согласования, передача конфиденциальных данных в открытом виде (cleartext) и т.д. | см. П15. Использование HTTPS. |
| М4 | Небезопасная аутентификация (Insecure Authentication)             | Эта уязвимость относится к аутентификации конечного пользователя или неверное управление сеансами. Включает следующие пункты: <br>- Отсутствие требований проверки идентификации пользователя; <br> - Отсутствие проверки контроля сеанса; <br> - Недостатки управления сессиями. | см. П17. <br>Использование временноого токена на сервере.  |
| М5 | Слабая криптостойкость (Insufficient Cryptography)                | Применение криптостойких алгоритмов для передачи sensitive информации. Использование криптоалгоритмов может быть недостаточным в частных случаях. Эта категория описывает варианты ненадлежащего использования криптографических элементов, слабой или недостаточной криптостойкости. Всё, что связано с TLS или SSL относится к категории M3. Если приложение не использует криптографические средства при необходимости, это относится к категории М2.  | см. П14. <br>**Не** использовать устаревшие алгоритмы хеш функций и шифрования. **Не** использовать CLEARTEXT.|
| М6 | Небезопасная авторизация (Insecure Authorization)                 | Эта уязвимость описывает недостатки авторизации (проверка (валидация) на стороне клиента, принудительный просмотр и т.д.). Такие события отличаются от проблем аутентификации (например, устройства регистрации, идентификации пользователей и т.д.). Если приложение не проходит проверку подлинности пользователей при необходимости (например, предоставление анонимного доступа к некоторым ресурсам или службам, при отсутстви проверки подлинности и запрета несанкционированного доступа), это является ошибкой проверки подлинности, а не сбоем авторизации.  |  см. П17. <br>Приложение использующее, обрабатыающее sensitive data, должно иметь функционал авторизации и подтверждение сеанса пользователя.|
| М7 | Контроль содержимого клиентских приложений (Client Code Quality)  | Эта категория рассматривает контроль за входными данными. Проблемы реализации технологий кода в клиент-сайд приложениях, отличающиеся от написания кода и реализации в сервер-сайд приложениях. К этому относится: переполнение буфера, format string уязвимости, а также другие ошибки на уровне кода, где решением является необходимость переписать код, который работает на мобильном устройстве.  | см. П1, П2, П3 |
| М8 | Модификация данных (Code Tampering)                               | Эта категория описывает изменение исполняемых файлов, локальных ресурсов, перехват вызовов сторонних процессов, подмена runtime методов и динамическую модификацию памяти. После установки приложения, его код остается резидентнымв памяти устройства. Это позволяет зловредному приложению изменять код, содержимое памяти, изменять или заменять системные методы API, изменять данные и ресурсы приложения. Это может обеспечить злоумышленнику возможность манипулирования сторонними приложениями для совершения нелегитимных действий, кражи данных или извлечения иной финансовой выгоды.  |  см. П11 |
| М9 | Анализ исходного кода (Reverse Engineering)                       | Эта уязвимость включает в себя анализ бинарных файлов для определения исходного кода, библиотек, алгоритмов и т.д. Программное обеспечение, такое как IDA Pro, Hopper, otool и другие инструменты реверс-инжиниринга могут дать представление о внутренней работе приложения. Это может быть использовано для поиска уязвимостей приложения, извлечения критичной информации, такой как бэкенд-сервера, ключей шифрования или интеллектуальной собственности.  |  см. П4, П5, П10 |
| М10| Скрытый функционал (Extraneous Functionality)                     | Часто разработчики включают в код приложений скрытые функциональные возможности, бэкдоры или другие механизмы, функциональность которых предназначена для общего использования. Под эту категорию подходит известное определение security through obscurity. Разработчик может случайно оставить пароль в качестве комментария в гибридном приложении. Либо это может быть отключение двухфакторной аутентификации во время тестирования.  |  см. П4, П17 |


### TODO ROADMAP:
1. Проверка на эмулятор
2. Добавить пункты для пентеста. Drozer, декомпиляция и пр.