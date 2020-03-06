package com.nifelee.springreactor.wikibook.ch02.observer;

public interface Observer<T> {

    void observe(T event);

}
