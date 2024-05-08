# Тестовый фреймворк

## Описание   
Тестовый фреймворк написан на основе Junit5, с использованием Extensions. 

* Web-тесты наследуют класс `BaseWebTest`, который помечен аннотацией `@WebTest`
* Grpc-тесты наследуют класс `BaseGrpcTest`, который помечен аннотацией `@GrpcTest` и в котором инстацированы grpc-клиенты
* Rest-тесты наследуют класс `BaseRestTest`, который помечен аннотацией `@RestTest` и в котором инстацирован клиент для gateway
* Тестовые классы и методы помечены аннотацией @DisplayName с описанием, а также аннотацией @Tag/@Tags с тегами для фильтрации. 

Пример Web-теста
```java
@DisplayName("Some web tests")
@Tag(EXAMPLE_TAG)
public class SomeWebTest extends BaseWebTest {

    @DisplayName("Check user's profile")
    @Test
    public void exampleTest() {
        ...
    }
}
```

## UI
Для работы с web-элементами используется Selenide

* Все страницы описываются в виде PageObject и должны наследовать `BasePage`
* Выделяемые, повторяющиеся компоненты, должны наследовать `BaseComponent`
* BasePage имеет абстрактный метод для ожидания загрузки страницы. Его должны реализовать все наследующие страницы.
Также есть метод для доскролла элементов в коллекциях на страницах `scrollToElement` и методы проверки всплывающих сообщений `checkSuccessMessage` `checkErrorMessage`

Пример страницы:
```java
public class ExamplePage extends BasePage<ExamplePage> {
    
    private final SelenideElement someElement = $(byDataTestId("some-element"));

    @Override
    public ExamplePage waitForPageLoaded() {
        someElement.shouldBe(visible);
        return this;
    }

    @Step("Check element {0}")
    public ExamplePage checkElementByText(String expectedText) {
        this.someElement.should(text(expectedText));
        return this;
    }
}
```
* Для проверок изображений используется кастомный condition ImageCondition. Если передать `byPath` true, то ожидаемое изображение будет конвертироваться в base64string из ресурсов, иначе сравниваются сразу строки.
* В rococo-client на некоторые элементы (например на страницах музеев, художников, картин) навешены локаторы с аттрибутом `data-testid`. Для описания таких элементов в PageObject нужно использовать кастомный селектор `byDataTestId`

Пример
```java
public class ArtistPage extends BasePage<ArtistPage> {

    private final SelenideElement name = $(byDataTestId("artist-name"));
    private final SelenideElement biography = $(byDataTestId("artist-biography"));
    private final SelenideElement photo = $(byDataTestId("artist-photo"));
}
```
## БД
Для подключения к БД используется Hibernate, который реализован через потокобезопасный [ThreafLocalEntityManager](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/db/hibernate/ThreadLocalEntityManager.java) и [EmfProvider](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/db/hibernate/EmfProvider.java). После выполнения тестовых прогонов в `AfterRunExtension` закрываются все соединения. Для работы с таблицами используется ORM подход. Таблицы описываются как data-классы с аннотацией `@Entity`
Для подключения новой БД к тестам необходимо: 

1). Добавить адрес в enum Database
```java
@RequiredArgsConstructor
public enum Database {
    AUTH("jdbc:postgresql://%s:%d/rococo-auth"),
    NEW_DB("jdbc:postgresql://%s:%d/new-db");
    ...
}
```
2). Добавить интерфейс новой БД и реализовать его через Hibernate:
```java
public interface NewDbRepository {
    ArtistEntity findElementById(UUID id);
}
```
```java
public class NewDbRepositoryHibernate extends JpaService implements ArtistRepository {
    public NewDbRepositoryHibernate() {
        super(of(NEW_DB, new ThreadLocalEntityManager(INSTANCE.emf(NEW_DB))));
    }
    @Override
    @Step("Find element by id {0}")
    public NewDbTableEntity findElementById(UUID id) {
        return entityManager(NEW_DB).find(NewDbTableEntity.class, id);
    }
}
```
3). Описать модели нужных таблиц:
```java
@Entity
@Data
@Builder
@Table(name = "new_db_table")
@NoArgsConstructor
public class NewDbTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;
}
```

## GRPC
* Для работы с GRPC-сервисами реализованы соответствующие клиенты на основе подключенного project `rococo-grpc`. Все java-модели автоматически сгенерированы из моделей, описанных в proto-файлах.
* Для конвертации из JsonNode в Grpc-model и наоборот есть методы `toJson` и `toGrpc` в [Helper](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/utils/Helper.java)
* Каналы для grpc-стабов создаются через синглтон `ChannelProvider`, а закрываются как и emf в `AfterRunExtension` 

Для добавления нового клиента нужно создать канал cо стабом (описанный в проекте rococo-grpc).
Пример
```java
public class NewGrpcClient {
    private static final Config CFG = Config.getInstance();
    private static final Channel newChannel = ChannelProvider
            .INSTANCE
            .channel(of(CFG.newGrpcHost(), CFG.newGrpcPort()));
    private static final RococoArtistServiceGrpc.RococoNewServiceBlockingStub rococoNewServiceBlockingStub = newBlockingStub(newChannel);
}
```
## REST
* Rest-тесты используют api сервиса `rococo-gateway`. Для взаимодействия используется клиент на основе Retrofit и OkHttpClient3.
* Практически все респонсы/реквесты де/сериализуются в JsonNode. Тк в тестах в основном используются grpc-модели, JsonNode конвертируются (как описано выше) с помощью "хелперов".
Для добавления нового rest-клиента нужно:

1). Описать интерфейс c методами api:
```java
public interface NewApi {
    @GET("/api/method")
    Call<JsonNode> getMethod(@Query("param") String param);
}
```
2). Создать класс, отнаследованный от RestClient, передать в его конструктор url и проинициализировать api.
```java
public class GatewayClient extends RestClient {
    private static final Config CFG = Config.getInstance();
    private final NewApi newApi;

    public NewClient() {
        super(CFG.newClientUrl());
        this.newApi = retrofit.create(NewApi.class);
    }
    
    public JsonNode getMethod(String param){
        try {
            return newApi.getMethod(param)
                    .execute()
                    .body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```
## Тестовые данные
### Контент
Для того чтобы создать музей, художника или картину перед тестом используются аннотации `@GeneratedMuseum`, `@GeneratedArtist` и
`@GeneratedPainting`.
#### Музеи
* Аннотация @GeneratedMuseum обрабатывается расширением [MuseumExtension](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/core/extensions/MuseumExtension.java), которое с помощью grpc создает музей перед запуском теста и кладет его в MUSEUM_NAMESPACE
* В тесте будет разрезолвлена grpc-модель Museum из MUSEUM_NAMESPACE
* Аннотация принимает в качестве параметров title, description, city, countryName
* Если параметры не переданы, то будут рандомно сгенерированы по-умолчанию

```java
    @Test
    @GeneratedMuseum(
            title = "Russian Museum",
            city = "St.Petersburg",
            countryName = "Russia"
    )
    @DisplayName("Get museum")
    public void getMuseum(Museum museum)
```

#### Художники
* Аннотация @GeneratedArtist обрабатывается расширением [ArtistExtension](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/core/extensions/ArtistExtension.java), которое с помощью grpc создает художника перед запуском теста и кладет его в ARTIST_NAMESPACE
* В тесте будет разрезолвлена grpc-модель Artist из ARTIST_NAMESPACE
* Аннотация принимает в качестве параметров name и biography
* Если параметры не переданы, то будут рандомно сгенерированы по-умолчанию

```java
    @Test
    @GeneratedArtist(
            name = "Ilya Repin",
            biography = "Famous Russian artist"
    )
    @DisplayName("Get artist")
    public void getArtist(Artist artist) 
```

#### Картины
* Аннотация @GeneratedPainting обрабатывается расширением [PaintingExtension](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/core/extensions/PaintingExtension.java), которое с помощью grpc создает картину перед запуском теста и кладет его в PAINTING_NAMESPACE
* В тесте будет разрезолвлена grpc-модель Painting из PAINTING_NAMESPACE
* Аннотация принимает в качестве параметров title, description, а также museum в виде аннотации @GeneratedMuseum и artist в виде @GeneratedArtist
* Если параметры title и description не переданы, то будут рандомно сгенерированы по-умолчанию, а museum и artist - обязательные

**Аннотации `@GeneratedArtist` и `@GeneratedMuseum` обрабатываются как отдельно так и внутри аннотации `@GeneratedPainting`**

```java
    @Test
    @GeneratedPainting(
            title = "Barge Haulers on the Volga",
            museum = @GeneratedMuseum(
                    title = "Russian Museum",
                    city = "St.Petersburg",
                    countryName = "Russia"
            ),
            artist = @GeneratedArtist(
                    name = "Ilya Repin",
                    biography = "Famous Russian artist"
            )
    )
    @DisplayName("Get painting")
    public void getPainting(Painting painting)
```

### Пользователь
Для создания юзера перед тестом используется аннотация `@CreatedUser`
#### Создание пользователя
* Аннотация обрабатывается расширением [CreateUserExtension](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/core/extensions/CreateUserExtension.java), которая через БД создает юзера в auth и userdata, создает модель TestUser и кладет её в CREATE_USER_NAMESPACE
* В тесте модель TestUser будет разрезолвлена из CREATE_USER_NAMESPACE
* Аннотация принимает в качестве параметров username и password
* Если параметры не переданы, то будут рандомно сгенерированы по-умолчанию
```java
    @Test
    @CreatedUser(
            username = "Bob",
            password = "12345"
    )
    @DisplayName("Get user")
    public void getUser(TestUser user)
```
#### Проставление авторизационных кук
Для проставления авторизационных кук в браузере в web-тестах, а также получения oauth-токена в rest-тестах используется аннотация `@LoggedIn`
* Аннотация обрабатывается расширением [ApiLoginExtension](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/core/extensions/ApiLoginExtension.java), которая через rest-api логинится пользователем, кладет полученные авторизационные куки (включая oauth-токен) в API_LOGIN_NAMESPACE
* В качестве параметров принимает user = `@CreatedUser` и `setCookies` (по умолчанию true)
* Если setCookies указан, то расширение также поднимает браузер и сетит куки в local-storage
* Для получения oauth-токена в rest-тестах используется аннотация `@Token`
```java
    @Test
    @DisplayName("Add artist")
    @LoggedIn(user = @CreatedUser, setCookies = false)
    public void addArtist(@Token String token)
```
**Аннотация `@CreatedUser` обрабатывается как отдельно так и внутри аннотации `@LoggedIn`**
### RandomUtils
Для генерации рандомных текстовых значений (имен, описаний и тд) используются методы [RandomUtils](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/utils/RandomUtils.java) на основе библиотеки [com.github.javafaker](https://github.com/DiUS/java-faker)
## Ожидания
* Для получения ожидаемого результата, при выполнения какого-то действия, можно использовать метод `waitFor` из [хелперов](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/utils/Helper.java), который принимает лямбду, возвращающую boolean (если true - дождались ожидаемого результата)

Пример использования
```java
    public <T> T scrollToElement(SelenideElement element, ElementsCollection collection) {
        waitFor(
                "element %s to appear".formatted(element),
                15000,
                () -> {
                    collection.last().scrollIntoView(true);
                    return element.exists();
                }
        );
        element.shouldBe(visible);
        return (T) this;
    }
```
* Для получения ожидаемого результата в течении нескольких попыток, игнорируя исключения, можно использовать метод `attempt` из [хелперов](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/utils/Helper.java), который принимает количество попыток, интервал между попытками и лямбду с действием

Пример использования
```java
    public ArtistEntity findArtistByName(String name) {
        TypedQuery<ArtistEntity> query = entityManager(ARTIST).createQuery("SELECT a FROM ArtistEntity a WHERE a.name = :name",ArtistEntity.class)
                        .setParameter("name", name);
                        return attempt(10,1000,() -> query.getSingleResult());
    }
```
## Матчеры
* Для проверок в тестах в основном используются матчеры из библиотеки `hamcrest`
* В качестве ассертов используется [кастомный ассерт](rococo-e-2-e-tests/src/test/java/guru/qa/rococo/utils/CustomAssert.java) `check`, который оборачивает assertThat в allure-step

Пример использования
```java
check("artist info is correct",
        artistInList.get(), equalTo(artist))
```



