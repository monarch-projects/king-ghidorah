package org.titan.hermes.common.exception;

import lombok.Getter;

/**
 * @Title: HermesException
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Getter
public class HermesException extends RuntimeException {

    private String message;

    public HermesException(String message) {
        super(message);
    }

    public HermesException(String message, Exception e) {
        super(message, e);
    }

}
