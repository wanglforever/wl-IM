package com.wl.im.command.impl;

import com.wl.im.command.Command;
import com.wl.im.model.Message;
import com.wl.im.util.Registry;
import io.netty.channel.Channel;

/**
 * Created on 2020/11/20 17:12
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class GCommand implements Command {

    private String help = "{$groupName} {$text}   //向一个讨论组发消息";

    @Override
    public Message clientRun(String userName, String... input) {
        if (input.length < 2){
            return null;
        }
        String groupName = input[0];
        StringBuilder sb = new StringBuilder();
        for (int i=1; i<input.length; i++){
            sb.append(input[i]);
        }
        return new Message(getCommandStr(), userName, groupName, sb.toString());
    }

    @Override
    public Message serverRun(Channel income, Message message) {
        return Registry.sendMsgToGroup(message, income);
    }

    @Override
    public String getHelp() {
        return help;
    }
}
