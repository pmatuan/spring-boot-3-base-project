package org.base.shared.utils;

import org.base.shared.response.AimsCommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static ResponseEntity<AimsCommonResponse<Object>> toSuccessCommonResponse(Object data) {
        return new ResponseEntity<>(new AimsCommonResponse<>(data), HttpStatus.OK);
    }
}
