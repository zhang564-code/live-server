package com.markerhub.demo.entity;

import lombok.Data;

@Data
public class Chat {


    private Integer userId;


    private String username;


    private String avatar;


    private String msg;


    private int count;
}
