# Spring Data R2DBC

### 샘플 코드

- 도메인
```java
@Getter @ToString @EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table("person")
public class Person {
    @Id private Long id;
    private String name;
    private int age;

    @Builder
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

- R2DBC 활성화
```java
@Configuration
@EnableR2dbcRepositories(basePackages = "...")
public class R2dbcConfig {
}
```

- Repository
```java
public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {
    @Query("select * from person where name = :name")
    Flux<Person> findByName(String name);
}
```

- Tests
```java
@SpringBootTest
class PersonRepositoryIntegrationTest {
    @Autowired PersonRepository personRepository;
    @Autowired DatabaseClient database;

    @Test
    void findByName() {
        //given
        Person person = Person.builder()
                .name("Joe")
                .age(20)
                .build();

        insertPerson(person);

        //when
        personRepository.findByName("Joe")
                .log()
                .as(StepVerifier::create)
                .assertNext(person::equals)
                .verifyComplete();
    }
}
```


### 제약 사항
- `Query method`를 아직 지원 안함
> Query derivation not yet supported!

- UUID 컨버터 지원 안함
> Caused by: org.springframework.core.convert.ConverterNotFoundException:
>   No converter found capable of converting from type [java.nio.HeapByteBuffer] to type [java.util.UUID]

- H2 DB 메모리 프로토콜만 지원
> protocol option tcp is unsupported (file, mem)
>   at io.r2dbc.h2.H2ConnectionFactoryProvider.create(H2ConnectionFactoryProvider.java:82)


### 결론
정식 버전이 나왔다고 얼핏 들어서 살짝 검토해보니 아직 마일스톤 버전이라 그런지 실무에서 사용하기에는 좀 더 기다려봐야겠다.
