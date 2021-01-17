package com.example.demo.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessegeResponse {

    private LocalDateTime timestamp;
    private List<String> errors = new ArrayList<>();
}
