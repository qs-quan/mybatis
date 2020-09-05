package com.wayzim.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体
 */
@Data
public class User implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 账号
     */
    private String username;

}
