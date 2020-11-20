package com.wl.im.command.impl;

import com.wl.im.command.Command;
import com.wl.im.model.Message;
import com.wl.im.util.Registry;
import io.netty.channel.Channel;

import java.util.List;

/**
 * Created on 2020/11/20 17:28
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class LsCommand implements Command {

    private String help="{user||group} //浏览在线的用户名单或已存在的讨论组名单";

    @Override
    public Message clientRun(String userName, String... input) {
        if (input.length > 0) {
            String type = input[0];
            if ("group".equals(type) || "user".equals(type)) {
                return new Message(getCommandStr(), userName, SERVER, type);
            }
        }
        return null;
    }

    @Override
    public Message serverRun(Channel income, Message message) {
        String nickname = message.getFromUser();
        String type = message.getContext();
        List<String> list = null;
        if ("group".equals(type)) {
            list = Registry.getGroupNameList();
        } else if ("user".equals(type)) {
            list = Registry.getNickNameList();
        }
        if (list != null) {
            return new Message(PRINT, SERVER, nickname, list.toString());
        }
        return null;
    }

    @Override
    public String getHelp() {
        return help;
    }
}
