package com.example.demo.webSocket;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class MyMessage {
    private String userId;

    private String message;
}
