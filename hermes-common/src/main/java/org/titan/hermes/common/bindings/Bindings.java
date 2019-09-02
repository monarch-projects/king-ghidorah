package org.titan.hermes.common.bindings;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @Title: Bindings
 * @Description:
 * @Author: daozhang
 * @date: 2019/9/1
 */
@Data
public class Bindings {

    private List<Binding> bindings = new LinkedList<>();
}
