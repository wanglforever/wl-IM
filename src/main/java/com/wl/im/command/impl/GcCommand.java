package com.wl.im.command.impl;

import com.wl.im.command.Command;
import com.wl.im.model.Message;
import com.wl.im.util.Registry;
import io.netty.channel.Channel;

/**
 * Created on 2020/11/20 17:19
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class GcCommand implements Command {

    private String help = "{$groupName}   //新增一个讨论组";

    @Override
    public Message clientRun(String userName, String... input) {
        if (input.length < 1){
            return null;
        }

        return new Message(getCommandStr(), userName, Command.SERVER, input[0]);
    }

    @Override
    public Message serverRun(Channel income, Message message) {
        String groupName = message.getContext();
        Registry.createGroup(groupName);
        System.out.println("创建聊天室" + groupName + "成功");
        return null;
    }

    @Override
    public String getHelp() {
        return help;
    }
}
