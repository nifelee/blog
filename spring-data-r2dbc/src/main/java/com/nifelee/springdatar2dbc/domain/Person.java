package com.nifelee.springdatar2dbc.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table("person")
public class Person {

    @Id
    private Long id;

    private String name;

    private int age;

    @Builder
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
