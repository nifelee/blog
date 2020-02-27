package com.nifelee.springdatar2dbc.example;

import com.nifelee.springdatar2dbc.domain.Person;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

public class R2dbcApp {

    private static final Log log = LogFactory.getLog(R2dbcApp.class);

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = ConnectionFactories.get(
                /*
                 * protocol option tcp is unsupported (file, mem)
                 * at io.r2dbc.h2.H2ConnectionFactoryProvider.create(H2ConnectionFactoryProvider.java:82)
                 */
                "r2dbc:h2:mem://localhost/~/workspace/study/blog/spring-data-r2dbc/doc/h2/r2dbc?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

        DatabaseClient client = DatabaseClient.create(connectionFactory);

        client.execute("CREATE TABLE person" +
                "(id VARCHAR(255) PRIMARY KEY," +
                "name VARCHAR(255)," +
                "age INT)")
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        Person person = Person.builder()
                .name("Joe")
                .age(20)
                .build();

        client.insert()
                .into(Person.class)
                .using(person)
                .then()
                .as(StepVerifier::create)
                .verifyComplete();

        client.select()
                .from(Person.class)
                .fetch()
                .first()
                .doOnNext(it -> log.info(it))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

}
