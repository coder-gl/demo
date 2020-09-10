package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@Getter
@Setter
@ToString
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1209433464620823607L;
    private String name ;
    private String sex ;
    private Integer age;
}
