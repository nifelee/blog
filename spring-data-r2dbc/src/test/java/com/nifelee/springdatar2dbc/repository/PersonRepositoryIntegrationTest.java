package com.nifelee.springdatar2dbc.repository;

import com.nifelee.springdatar2dbc.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class PersonRepositoryIntegrationTest {

    @Autowired PersonRepository personRepository;
    @Autowired DatabaseClient database;

    @BeforeEach
    void before() {
        Hooks.onOperatorDebug();

        List<String> statements = Arrays.asList(//
                "DROP TABLE IF EXISTS person;",
                "CREATE TABLE person" +
                        "(id INT IDENTITY PRIMARY KEY," +
                        "name VARCHAR(255)," +
                        "age INT)");

        statements.forEach(it -> database.execute(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }

    void insertPerson(Person person) {
        personRepository.save(person)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

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