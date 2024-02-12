package org.base.api.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.base.api.service.dto.input.ExampleGetInput;
import org.base.api.service.dto.input.ExamplePostInput;
import org.base.api.service.dto.output.ExampleGetOutput;
import org.base.api.service.dto.output.ExamplePostOutput;
import org.base.shared.db.entities.Example;
import org.base.shared.db.repo.ExampleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExampleService {

  private final ExampleRepository exampleRepository;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  private final RestTemplate restTemplate;

  @Value(value = "${kafka.send-notification-topic}")
  private String topic;

  public List<ExampleGetOutput> getExample(ExampleGetInput input) {
    List<Example> examples = exampleRepository.findAll();
    List<ExampleGetOutput> exampleGetOutputList = examples.stream()
        .map(ExampleGetOutput::fromExample)
        .collect(Collectors.toList());
    return exampleGetOutputList;
  }

  public Page<ExampleGetOutput> getExamplePagePagination(ExampleGetInput input, Pageable pageable) {
    input.normalize();
    Page<Example> examplePage = exampleRepository.getExamplePage(input.getDescription(),
        input.getCreatedFrom(), input.getCreatedTo(), input.getCreatedBy(), pageable);
    return examplePage.map(ExampleGetOutput::fromExample);
  }

  public ExamplePostOutput saveExample(ExamplePostInput input) {
    Example example = Example.builder()
        .id(11L)
        .description(input.getDescription())
        .createdAt(Instant.now())
        .createdBy(input.getCreatedBy())
        .build();
    kafkaTemplate.send(topic, example.getId().toString(), example);
    ExamplePostOutput output = ExamplePostOutput.builder()
        .message("Success")
        .build();
    return output;
  }

  public String helloCloud() throws InterruptedException {
    log.info("---------Hello method started---------");
    ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("https://httpbin.org/post", "Hello, Cloud!", String.class);
    Thread.sleep(2000);
    return responseEntity.getBody();
  }
}
