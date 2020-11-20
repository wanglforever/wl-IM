package com.wl.im.client;

import com.wl.im.command.Command;
import com.wl.im.command.CommandFactory;
import com.wl.im.model.Message;
import com.wl.im.server.RequestHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created on 2020/11/20 16:39
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class ClientApplication {
    private final String ip;
    private final int port;
    private String nickName = "c1";
    private Channel channel;
    private CommandFactory commandFactory = CommandFactory.DEFAULT_PACKAGE;


    public ClientApplication(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    private void start(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(ip, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ClientHandler())
                                    .addLast(new ClientOutHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            channel = future.channel();
            clientRun();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    private void send(Message msg){
        channel.writeAndFlush(msg);
    }

    @SneakyThrows
    private void clientRun() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        nickName = bufferedReader.readLine();
        send(commandFactory.clientInvoke("add", nickName, null));

        while (true){
            String text = bufferedReader.readLine();
            if ("quit".equalsIgnoreCase(text)){
                break;
            }
            if ("help".equalsIgnoreCase(text)){
                help();
                continue;
            }

            Matcher matcher = Pattern.compile("-(\\w+)\\s+(.*)").matcher(text);
            if (matcher.find()) {
                String commandStr = matcher.group(1);
                String context = matcher.group(2);
                Message message = commandFactory.clientInvoke(commandStr, nickName, context);
                send(message);
            }else {
                System.err.println("input error");
            }
        }

        bufferedReader.close();
    }

    private void help(){
        commandFactory.helpStr().forEach(System.out::println);
    }

    public static void main(String[] args) {
        new ClientApplication("localhost", 10080).start();
    }
}
