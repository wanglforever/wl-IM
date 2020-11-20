package com.wl.im.command;

import com.wl.im.model.Message;
import io.netty.channel.Channel;

/**
 * Created on 2020/11/20 15:09
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public interface Command {

    String SERVER = "server";
    String PRINT = "print";

    Message clientRun(String userName, String... input);

    Message serverRun(Channel income, Message message);

    String getHelp();

    default String getCommandStr(){
        return this.getClass().getSimpleName().replace("Command","").toLowerCase();
    }
}
