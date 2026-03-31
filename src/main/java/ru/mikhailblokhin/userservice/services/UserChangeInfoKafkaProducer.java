package ru.mikhailblokhin.userservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mikhailblokhin.userservice.entities.UserChangeInfo;

@Service
public class UserChangeInfoKafkaProducer implements KafkaProducer<UserChangeInfo> {

    private final KafkaTemplate<String, UserChangeInfo> kafkaTemplate;

    private static final Logger log = LoggerFactory.getLogger(UserChangeInfoKafkaProducer.class);

    public UserChangeInfoKafkaProducer(KafkaTemplate<String, UserChangeInfo> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(UserChangeInfo userChangeInfo) {
        kafkaTemplate.send("userChangeInfo", userChangeInfo);
        log.info("В кафку отправлена информация об изменении пользователя {}", userChangeInfo);
    }
}
