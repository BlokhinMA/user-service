package ru.mikhailblokhin.userservice.services;

public interface KafkaProducer<T> {
    void produce(T t);
}
