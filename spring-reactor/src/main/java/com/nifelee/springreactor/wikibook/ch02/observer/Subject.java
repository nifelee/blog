package com.nifelee.springreactor.wikibook.ch02.observer;

public interface Subject<T> {

    void registerObserver(Observer<T> observer);

    void unregisterObserver(Observer<T> observer);

    void notifyObservers(T event);

}
