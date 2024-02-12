package org.base.api.controller;

import lombok.AllArgsConstructor;
import org.base.api.controller.dto.request.ExampleRequestGet;
import org.base.api.controller.dto.request.ExampleRequestPost;
import org.base.api.controller.dto.response.ExampleResponseGet;
import org.base.api.controller.dto.response.ExampleResponsePost;
import org.base.api.service.ExampleService;
import org.base.shared.response.AimsCommonResponse;
import org.base.shared.utils.PageableUtils;
import org.base.shared.utils.ResponseUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/example")
public class ExampleController {

  private final ExampleService exampleService;

  @GetMapping("")
  public ResponseEntity<AimsCommonResponse<Object>> getExampleId(
      ExampleRequestGet request
  ) {
    return ResponseUtil.toSuccessCommonResponse(exampleService.getExample(request.toInput()));
  }

  @GetMapping("/pagination")
  public ResponseEntity<AimsCommonResponse<Object>> getExampleIdPagination(
      ExampleRequestGet request
  ) {
    Pageable pageable = PageableUtils.generate(request.getPage(), request.getSize(), "");
    return ResponseUtil.toSuccessCommonResponse(ExampleResponseGet.from(
        exampleService.getExamplePagePagination(request.toInput(), pageable))
    );
  }

  @GetMapping("/hello")
  public ResponseEntity<AimsCommonResponse<Object>> hello() throws InterruptedException {

    return ResponseUtil.toSuccessCommonResponse(
        exampleService.helloCloud()
    );
  }

  @PostMapping("")
  public ResponseEntity<AimsCommonResponse<Object>> addExample(
      @RequestBody ExampleRequestPost request
  ) {
    return ResponseUtil.toSuccessCommonResponse(
        ExampleResponsePost.from(exampleService.saveExample(request.toInput())));
  }
}
