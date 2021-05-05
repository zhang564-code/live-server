package com.markerhub.demo.common.dto;

import com.markerhub.demo.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class LiveDto implements Serializable {

    private Long id;

    private User user;

    private String livename;

    private Date date;

    private String pushUrl;//推流地址

}
