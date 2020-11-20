package com.wl.im.command.impl;

import com.wl.im.command.Command;
import com.wl.im.model.Message;
import com.wl.im.util.Registry;
import io.netty.channel.Channel;

/**
 * Created on 2020/11/20 15:49
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class AddCommand implements Command {

    private String help = "{$groupName}  //加入一个讨论组";

    @Override
    public Message clientRun(String userName, String... input) {
        if (input.length > 0){
            return new Message(getCommandStr(), userName, SERVER, input[0]);
        }
        return new Message(getCommandStr(), userName, SERVER, null);
    }

    @Override
    public Message serverRun(Channel income, Message message) {
        String nickName = message.getFromUser();
        String groupName = message.getContext();
        if (groupName == null){
            Registry.userJoin(income, nickName);
            return null;
        }
        return Registry.joinGroup(nickName, groupName);
    }

    @Override
    public String getHelp() {
        return this.help;
    }
}
