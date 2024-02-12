package org.base.api.service.dto.output;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import org.base.shared.db.entities.Example;

@Data
@Builder
public class ExampleGetOutput {

  private Long id;

  private String description;

  private Instant createdAt;

  private String createdBy;

  public static ExampleGetOutput fromExample(Example example) {
    return ExampleGetOutput.builder()
        .id(example.getId())
        .description(example.getDescription())
        .createdAt(example.getCreatedAt())
        .createdBy(example.getCreatedBy())
        .build();
  }
}
