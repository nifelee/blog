package com.nifelee.springreactor.wikibook.ch02.sse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Temperature {

    private final double value;

}
