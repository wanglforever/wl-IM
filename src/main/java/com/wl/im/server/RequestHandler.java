package com.wl.im.server;

import com.wl.im.command.CommandFactory;
import com.wl.im.model.Message;
import com.wl.im.util.Registry;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created on 2020/11/20 15:08
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class RequestHandler extends SimpleChannelInboundHandler<Message> {

    private static CommandFactory commandFactory = CommandFactory.DEFAULT_PACKAGE;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Channel channel = channelHandlerContext.channel();
        Message response = commandFactory.serverInvoke(channel, message);
//        if (response != null){
//            channel.writeAndFlush(response);
//        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel income = ctx.channel();
        Registry.addUser(income);
        System.out.println(income.remoteAddress() + "加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        Registry.removeUser(incoming);
        System.out.println(incoming.remoteAddress() + "离开");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + " 抛异常");
        cause.printStackTrace();
        ctx.close();
    }
}
