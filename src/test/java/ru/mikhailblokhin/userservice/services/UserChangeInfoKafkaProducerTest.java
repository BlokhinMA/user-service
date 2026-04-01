package ru.mikhailblokhin.userservice.services;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import ru.mikhailblokhin.userservice.entities.OperationType;
import ru.mikhailblokhin.userservice.entities.UserChangeInfo;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class UserChangeInfoKafkaProducerTest {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka:latest");

    @Autowired
    private UserChangeInfoKafkaProducer userChangeInfoKafkaProducer;

    @Autowired
    private KafkaTemplate<String, UserChangeInfo> kafkaTemplate;

    private Consumer<String, UserChangeInfo> consumer;

    private static final String USER_CHANGE_INFO_TOPIC = "userChangeInfo";

    @BeforeEach
    void setUp() {
        String bootstrapServers = kafkaContainer.getBootstrapServers();
        kafkaTemplate.getProducerFactory().updateConfigs(Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers));
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
                bootstrapServers,
                "userChangeInfo-group"
        );
        JsonMapper jsonMapper = new JsonMapper();
        JacksonJsonDeserializer<UserChangeInfo> jsonDeserializer =
                new JacksonJsonDeserializer<>(UserChangeInfo.class, jsonMapper);
        ConsumerFactory<String, UserChangeInfo> cf = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                jsonDeserializer);
        consumer = cf.createConsumer();
        consumer.subscribe(Collections.singleton(USER_CHANGE_INFO_TOPIC));
    }

    @Test
    void testKafkaMessage() {
        UserChangeInfo expectedUserChangeInfo = new UserChangeInfo(OperationType.CREATION, "1@1");
        userChangeInfoKafkaProducer.produce(expectedUserChangeInfo);
        ConsumerRecord<String, UserChangeInfo> record = KafkaTestUtils.getSingleRecord(consumer, USER_CHANGE_INFO_TOPIC);
        UserChangeInfo actualUserChangeInfo = record.value();
        assertEquals(expectedUserChangeInfo, actualUserChangeInfo);
    }
}