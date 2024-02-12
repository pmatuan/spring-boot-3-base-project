package org.base.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.base.consumer.listener.dto.ExampleEvent;
import org.base.consumer.utils.KafkaUtils;
import org.base.shared.db.entities.Example;
import org.base.shared.db.repo.ExampleRepository;
import org.base.shared.utils.JsonMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExampleListener {

    private final ExampleRepository exampleRepository;

    public ExampleListener(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @KafkaListener(topics = "${kafka.example-topic}", groupId = "example")
    public void listenExampleTopic(String eventString) {
        KafkaUtils.handle(() -> {
            ExampleEvent event = JsonMapper.convertJsonToObject(eventString, ExampleEvent.class);
            if (event == null) {
                log.error("error parse event CustomerEvent, raw string: {}", eventString);
                return;
            }

            Example example = Example.builder()
                    .id(event.getId())
                    .description(event.getDescription())
                    .createdAt(event.getCreatedAt())
                    .createdBy(event.getCreatedBy())
                    .build();
            exampleRepository.save(example);
            log.info("save success event with id {}", example.getId());
        });
    }

}
