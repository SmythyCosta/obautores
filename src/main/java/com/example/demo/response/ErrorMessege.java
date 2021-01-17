package com.example.demo.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ErrorMessege {

    private LocalDateTime dateAndTime;
    private String menssage;
    
}
