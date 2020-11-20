package com.wl.im.client;

import com.wl.im.model.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.time.LocalDateTime;

/**
 * Created on 2020/11/20 18:44
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class ClientOutHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Message message = (Message) msg;
        System.out.println("【"+ message.getFromUser() +"】" + LocalDateTime.now() + "【" +  message.getContext() + "】");
        super.write(ctx, msg, promise);
    }
}
