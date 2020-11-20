package com.wl.im.command.impl;

import com.wl.im.command.Command;
import com.wl.im.model.Message;
import com.wl.im.util.Registry;
import io.netty.channel.Channel;

/**
 * Created on 2020/11/20 17:32
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class UCommand implements Command {

    private String help="{$username} {&text} //向用户发消息";

    @Override
    public Message clientRun(String userName, String... input) {
        Message message = null;
        if (input.length > 1) {
            StringBuilder text = new StringBuilder();
            String toUsername = input[0];
            for (int i = 1; i < input.length; i++) {
                text.append(input[i]);
            }
            message = new Message(getCommandStr(), userName, toUsername, text.toString());
        }
        return message;
    }

    @Override
    public Message serverRun(Channel income, Message message) {
        return Registry.sendMsgToUser(message);
    }

    @Override
    public String getHelp() {
        return help;
    }
}
