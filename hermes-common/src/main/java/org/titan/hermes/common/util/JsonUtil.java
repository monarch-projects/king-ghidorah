package org.titan.hermes.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.titan.hermes.common.exception.HermesException;

import java.io.IOException;

/**
 * @Title: JsonUtil
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T decode(String json, Class<T> t) {
        try {
            return MAPPER.readValue(json, t);
        } catch (IOException e) {
            log.error("error to decodeBaseMessage hermes message", e);
            throw new HermesException("error to decodeBaseMessage hermes message");
        }
    }


    public static String encode(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("error to encodeBaseMessage hermes message", e);
            throw new HermesException("error to encodeBaseMessage hermes message");
        }
    }
}
