package com.wl.im.model;

import lombok.*;

import java.io.Serializable;

/**
 * Created on 2020/11/20 14:53
 *
 * @author wanglei
 * @Description 消息类实体
 * @projectName wlIM
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message implements Serializable {

    private static final long serialVersionUID = -535744004133321692L;

    /**
     * 用户要做的操作
     */
    private String commandStr;
    private String fromUser;
    private String toUser;
    private String context;
}
