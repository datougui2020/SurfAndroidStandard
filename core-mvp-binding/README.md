[Главная](/docs/main.md)

- [MVP-Rx](#mvp-rx)
    - [Общее](#общее)
      - [BindModel](#bindmodel)
      - [Потоки данных](#потоки-данных)
      - [Направленность потока](#направленность-потока)
      - [Relation](#relation)
    - [Известные проблемы/тонкости](#известные-проблемытонкости)
      - [Инстанцирование презентера](#инстанцирование-презентера)
      - [Null-нетерпимость](#null-нетерпимость)
      - [Зацикливание](#зацикливание)
        - [Как разрешать](#как-разрешать)
          - [Пример](#пример)
    - [Рекомендуется к использованию](#рекомендуется-к-использованию)
- [Core mvp binding (deprecated)](#core-mvp-binding-deprecated)
- [Использование](#использование)
- [Подключение](#подключение)

# MVP-Rx
## Общее
### BindModel
Это сущность созданая для замены ScreenModel. Ключевая разница в них, то
что ScreenModel отражает в идеале текущее состояние объекта. В то время
как BindModel служит для того чтобы организовать обмен данными между
View и Presenter. Можно рассматривать BindModel как интерфейс в
широком смысле этого слова.

### Потоки данных
В рамках архитектуры подразумвается организация двух направлений
потоков данных
* от view к presentor`у (view > view binding > presenter)
* от presentor`a ко view (presenter > view binding > view)
* Важно отметить, что здесь не регламентируется связь презентора и модели

### Направленность потока
Под направленностью потоков подразумевается то, что данные могут быть
отправлены определенным типом отправителей для определнного типа получателей.
Например, view могут посылать данные только для презентора и подписаться
на поток могут только презенторы и никто больше, в том числе эта
возможность недоступна и для самих view.

### Relation
Механизм организации направлености потоков. Подразумевает, что у потока
есть отправитель и получатель. Соответственно, отправитель может быть
либо источником данных для одного потока, либо получателем этих данных.

#### Сущности

##### [`RelationEntity`][related]
Тип источника или получателя. В данный момент сейчас это View и Presenter

##### [`Related`][related]
Конечный объект который будет источником или получателем. Имеет тип
`RelationEntity`

##### [`Relation`][related]
Направленный поток данных, связывает два `Related` объекта для передачи
данных.

Реализованы следующие типы:

* `Action` Деиствие исходящее от пользователя. При подписке эмитит
последний объект. Симметрично State.

* `State` Изменение состояния исходящее из ui/бизнес-логики. При
подписке эмитит последний объект. Симетрично Action.

* `Bond` Action и State в одной сущности. Deprecated. Лучше использовать совместно `Acton` и `State` взамен

* `Command` Во многом повторяет State за тем исключением, что не хранит
последний объект, и потому не эмитит его при подписке.

## Известные проблемы/тонкости

### Инстанцирование презентера

Для инстанцирования презентера необходимо наследовать компонент экрана от BindableScreenComponent и в модуле добавить provide метод,
он должен возвращать тип Any. В самом презентере нужно поставить @Inject на конструкторе. Это позволит не прописывать создание презентера вручную.

```kotlin
@Provides
@PerScreen
fun providePresenter(presenter: YourPresenter) = Any()
```

### Null-нетерпимость
Реализация основанна на [RxRely][rxrelylib] потому не принимает null.
Как и вся RxJava2

### Зацикливание
Описание проблемы [здесь][pmexist] `Two-way Data Binding`
По возможности, старайтесь избегать. Если все же нужно циклическое
связвание данных, то предусмотрите выход на строне презентора или вью.

#### Как разрешать
Во-первых. При создании цикличных зависимостей используйте взаимно обратные 
преобразования со стороны вью и презентора, 
т.е. такие которые при использовании взаимно нейтрализуют друг друга.
f1(f2(x)) = y, f2(f1(y)) = x  
где  
x - значение передаваемое из презентера во вью (через State)  
y -значение передаваемое из вью в презентер (через Action)  
f1 - преобразование значения со стороны вью  
f2 - преобразование значение со стороны презентора.  
Во-вторых используйте оператор `distinctUntilChanged()` на стороне презентора или вью

Для большей надежности имеет смысл передавать через Action/State объекты доменной модели, это уменьшит количество ошибок на перобразованиях

##### Пример
На вью виджет ввода суммы, который добавляет в символ валюты в конец.
Виджет эмитит строку целиком: сумма+символ валюты.
Со стороны презентора приходит только сумма без символа валюты - ее он и эммитит в стейт.

`TwoWayActivityView` в samples 
 
## Рекомендуется к использованию

* [RxBinding][rxbindinglib]
* [RxRelay][rxrelylib]
* [RxKotlin][rxkotlin]


[rxbindinglib]: https://github.com/JakeWharton/RxBinding
[rxrelylib]: https://github.com/JakeWharton/RxRelay
[pmexist]: https://habr.com/company/mobileup/blog/342850/
[pmenter]: https://habr.com/company/mobileup/blog/326962/
[pmscheme]: https://habrastorage.org/webt/rz/nb/rh/rznbrht-4vw_74h6wzrjrui8upk.png
[related]: lib-core-mvp-binding/src/main/java/ru/surfstudio/android/core/mvp/binding/rx/relation/Relation.kt
[rxkotlin]: https://github.com/ReactiveX/RxKotlin


# Core mvp binding (deprecated)
**(Данный модуль является экспериментальным и не является обязательным
стандартом использования в проекте)**
Поддежка data-binding

Основные классы:
* [`BindData`][bd]
* [`BindsHolder`][bh]
* [`BaseBindableView`][bbv]
* [`BaseBindingPresenter`][bbp]

# Использование
[Пример использования](sample)

# Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru)
необходимо, чтобы корневой `build.gradle` файл проекта был сконфигурирован так,
как описано [здесь](https://gitlab.com/surfstudio/projects/standard/android-standard/-/blob/HEAD/README.md).

Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.android:core-mvp-binding:X.X.X"
```

[bd]: lib-core-mvp-binding/src/main/java/ru/surfstudio/android/core/mvp/binding/BindData.kt
[bh]: lib-core-mvp-binding/src/main/java/ru/surfstudio/android/core/mvp/binding/BindsHolder.kt
[bbv]: lib-core-mvp-binding/src/main/java/ru/surfstudio/android/core/mvp/binding/BaseBindableView.kt
[bbp]: lib-core-mvp-binding/src/main/java/ru/surfstudio/android/core/mvp/binding/BaseBindingPresenter.kt