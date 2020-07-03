package com.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 11:51 上午
 * @description:
 */
@Data
public class ResultInfo implements Serializable {
    private int code;
    private String message;
}
