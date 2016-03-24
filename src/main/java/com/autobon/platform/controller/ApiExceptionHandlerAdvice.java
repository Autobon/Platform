package com.autobon.platform.controller;

import com.autobon.shared.JsonMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Created by dave on 16/3/24.
 */
@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandlerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JsonMessage handleUploadException(MaxUploadSizeExceededException ex) {
        return new JsonMessage(false, "UPLOAD_SIZE_EXCEED", "上传文件单个不能超过2MB,一次总共不能超过10MB");
    }

}
