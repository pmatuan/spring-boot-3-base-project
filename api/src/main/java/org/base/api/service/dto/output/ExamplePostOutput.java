package org.base.api.service.dto.output;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ExamplePostOutput {
  private String message;
}
