package com.nifelee.springdatar2dbc.repository;

import com.nifelee.springdatar2dbc.domain.Person;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {

    //Query derivation not yet supported!
    @Query("select * from person where name = :name")
    Flux<Person> findByName(String name);

}
