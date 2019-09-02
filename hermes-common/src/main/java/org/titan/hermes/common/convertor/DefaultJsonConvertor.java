package org.titan.hermes.common.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.titan.hermes.common.exception.HermesException;
import org.titan.hermes.common.message.Message;

import java.io.IOException;
import java.util.Objects;

/**
 * @Title: DefaultJsonConvertor
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Slf4j
public class DefaultJsonConvertor implements MessageConvertor {


    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public Object decode(Message message) {
        if (Objects.isNull(message.getToClass()))
            return message.getMessage();

        try {
            return mapper.readValue(message.getMessage(), message.getToClass());
        } catch (IOException e) {
            log.error("error to decodeBaseMessage message", e);
            throw new HermesException("error to decodeBaseMessage message", e);
        }
    }

    @Override
    public byte[] encode(Object message) {
        try {
            return this.mapper.writeValueAsString(message).getBytes();
        } catch (JsonProcessingException e) {
            log.error("error to decodeBaseMessage message , because of :", e);
            throw new HermesException("decodeBaseMessage message exception", e);
        }
    }
}
