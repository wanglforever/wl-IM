package com.wl.im.util;

import com.wl.im.command.Command;
import com.wl.im.model.Message;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 2020/11/20 15:56
 *
 * @author wanglei
 * @Description 注册中心，消息分发中心（在线中心）
 * @projectName wlIM
 */
public class Registry {

    /**
     * 在线列表
     */
    private static ChannelGroup onlineGroup;
    /**
     * channel -> nickName 昵称
     */
    private static Map<Channel, String> channelNickNameMap;
    /**
     * nickName -> channel
     */
    private static Map<String, Channel> nickNameChannelMap;

    private static Map<String, ChannelGroup> chatGroup;

    static {
        onlineGroup = new DefaultChannelGroup("onlineGroup", GlobalEventExecutor.INSTANCE);
        channelNickNameMap = new HashMap<>();
        nickNameChannelMap = new HashMap<>();
        chatGroup = new HashMap<>();
    }

    public static void userJoin(Channel income, String userName){
        channelNickNameMap.put(income, userName);
        nickNameChannelMap.put(userName, income);
    }

    public static void addUser(Channel channel) {
        onlineGroup.add(channel);
    }

    public static void send(Message message) {
        System.out.println("sending message :" + message);
        Channel channel = nickNameChannelMap.get(message.getToUser());
        assert channel != null;
        channel.writeAndFlush(message);
        System.out.println("send message :" + message + " success.");
    }

    public static List<String> getGroupNameList() {
        return new ArrayList<>(chatGroup.keySet());
    }

    public static List<String> getNickNameList() {
        return new ArrayList<>(nickNameChannelMap.keySet());
    }

    private boolean isUserOnline(String userName) {
        Channel channel = nickNameChannelMap.get(userName);
        return channel != null && onlineGroup.contains(channel);
    }

    public static void removeUser(Channel income) {
        String nickName = channelNickNameMap.get(income);
        onlineGroup.remove(income);
        channelNickNameMap.remove(income);
        nickNameChannelMap.remove(nickName);
    }

    public static void createGroup(String groupName) {
        ChannelGroup group = new DefaultChannelGroup(groupName, GlobalEventExecutor.INSTANCE);
        chatGroup.put(groupName, group);
    }

    public static Message sendMsgToGroup(Message message, Channel income) {
        String fromUser = message.getFromUser();
        String groupName = message.getToUser();
        ChannelGroup group = chatGroup.get(groupName);
        if (group == null) {
            return new Message(Command.PRINT, Command.SERVER, fromUser,"未找到聊天室");
        }
        if (!group.contains(income)){
            return new Message(Command.PRINT, Command.SERVER, fromUser, "未加入聊天室");
        }
        for (Channel channel : group) {
            if (!channel.equals(income)){
                channel.writeAndFlush(message);
            }
        }

        return new Message(Command.PRINT, Command.SERVER, fromUser, "发送成功");
    }

    public static void sendMsgToEveryone(Message message){
        for (Channel channel : onlineGroup){
            message.setToUser(channelNickNameMap.get(channel));
            channel.writeAndFlush(message);
        }
    }

    public static List<String> getGroupUserList(String groupName) {
        ChannelGroup group = chatGroup.get(groupName);
        if (group == null){
            return Collections.emptyList();
        }

        return group.stream()
                .map(channel -> channelNickNameMap.get(channel))
                .collect(Collectors.toList());
    }

    public static Message sendMsgToUser(Message message) {
        String fromUser = message.getFromUser();
        String toUser = message.getToUser();
        Channel channel = nickNameChannelMap.get(toUser);

        if (channel == null){
            return new Message(Command.PRINT, fromUser, toUser, "未找到该用户");
        }

        message.setCommandStr(Command.PRINT);
        channel.writeAndFlush(message);

        return new Message(Command.PRINT, fromUser, toUser, "发送成功");
    }

    public static Message joinGroup(String nickName, String groupName) {
        Channel channel = nickNameChannelMap.get(nickName);
        ChannelGroup group = chatGroup.get(groupName);
        if (group != null){
            group.add(channel);
            return new Message(Command.PRINT, Command.SERVER, nickName, "加入成功");
        }
        return new Message(Command.PRINT, Command.SERVER, nickName, "未找到聊天室");
    }
}
