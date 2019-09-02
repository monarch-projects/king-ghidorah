package org.titan.hermes.client.meta;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Title: ListenerMetaInfo
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@EqualsAndHashCode
@Data
@Accessors(chain = true)
public class ListenerMetaInfo {
    private List<String> queueNames;
    private String key;

    public static ListenerMetaInfo of(List<String> queueName,  String key) {

        return new ListenerMetaInfo().setQueueNames(queueName).setKey(key);
    }
}
