package com.wl.im.client;

import com.wl.im.model.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

/**
 * Created on 2020/11/20 16:40
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        System.out.println("【"+ message.getFromUser() +"】" + LocalDateTime.now() + "【" +  message.getContext() + "】");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + " 抛异常");
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
