package com.wl.im.command.impl;

import com.wl.im.command.Command;
import com.wl.im.model.Message;
import io.netty.channel.Channel;

/**
 * Created on 2020/11/20 16:29
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class PrintCommand implements Command {

    private String help = "";

    @Override
    public Message clientRun(String userName, String... input) {
        return null;
    }

    @Override
    public Message serverRun(Channel income, Message message) {
        if (message != null){
            System.out.println(message);
        }
        return null;
    }

    @Override
    public String getHelp() {
        return help;
    }
}
