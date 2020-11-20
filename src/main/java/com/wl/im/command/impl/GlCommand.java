package com.wl.im.command.impl;

import com.wl.im.command.Command;
import com.wl.im.model.Message;
import com.wl.im.util.Registry;
import io.netty.channel.Channel;

import java.util.List;

/**
 * Created on 2020/11/20 17:24
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class GlCommand implements Command {
    private String help="{$groupname}   //浏览一个讨论组的用户";

    @Override
    public String getHelp(){
        return this.help;
    }

    @Override
    public Message clientRun(String username, String... input) {
        Message message=null;
        if(input.length==1){
            String groupName = input[0];
            message=new Message(getCommandStr(),username,SERVER,groupName);
        }
        return message;
    }

    @Override
    public Message serverRun(Channel incoming, Message message) {
        String groupname = message.getContext();
        String nickname = message.getFromUser();
        List<String> userNicknameList= Registry.getGroupUserList(groupname);
        if(!userNicknameList.isEmpty()){
            return new Message(PRINT,SERVER,nickname,userNicknameList.toString());
        }else {
            return new Message(PRINT,SERVER,nickname,"未找到讨论组");
        }
    }
}
