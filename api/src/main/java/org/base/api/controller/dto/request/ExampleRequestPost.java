package org.base.api.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.base.api.service.dto.input.ExamplePostInput;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExampleRequestPost {

  private String description;
  private String createdBy;

  public ExamplePostInput toInput(){
    return ExamplePostInput.builder()
        .description(description)
        .createdBy(createdBy)
        .build();
  }
}
