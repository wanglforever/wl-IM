package com.wl.im.command;

import com.wl.im.model.Message;
import io.netty.channel.Channel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 2020/11/20 15:15
 *
 * @author wanglei
 * @Description
 * @projectName wlIM
 */
public class CommandFactory {

    public static final CommandFactory DEFAULT_PACKAGE = new CommandFactory("com.wl.im.command.impl");

    private Map<String, Command> commandMap = null;
    private String packageName;

    public CommandFactory(String packageName){
        this.packageName = packageName;
        load();
    }

    private void load() {
        commandMap = new HashMap<>();
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
            while (resources.hasMoreElements()){
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                if ("file".equalsIgnoreCase(protocol)){
                    String path = resource.getPath();
                    loadClass(packageName, path);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadClass(String packageName, String path) {
        File[] files = new File(path).listFiles(file -> file.isDirectory() || file.getName().endsWith(".class"));

        assert files != null;

        for (File file : files){
            String fileName = file.getName();
            if (file.isDirectory()){
                String subPackagePath = path + "/" + fileName;
                String subPackageName = packageName + "." + fileName;
                loadClass(subPackageName, subPackagePath);
            }else{
                String className = packageName + "." + fileName.substring(0, fileName.lastIndexOf("."));

                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (clazz != null){
                    if (!clazz.equals(Command.class) && Command.class.isAssignableFrom(clazz)){
                        Command command = null;
                        try {
                            command = (Command) clazz.newInstance();
                            String commandStr = clazz.getSimpleName();
                            commandStr = commandStr.replaceAll("Command","").toLowerCase();
                            commandMap.put(commandStr, command);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public List<String> helpStr(){
        return commandMap.entrySet()
                .stream()
                .map(entry -> "-" + entry.getKey() + " " + entry.getValue().getHelp())
                .collect(Collectors.toList());
    }

    public Message clientInvoke(String commandStr, String userName, String text) {
        Command command = commandMap.get(commandStr);

        if (command == null){
            return null;
        }

        if (text == null){
            return command.clientRun(userName);
        }

        return command.clientRun(userName, text.split("\\s+"));
    }

    public Message serverInvoke(Channel income, Message message) {
        if (message == null) {
            throw new RuntimeException("message is null!!!");
        }

        System.out.println(message);

        String commandStr = message.getCommandStr();
        if (commandStr == null || commandStr.trim().length() == 0){
            throw new RuntimeException("command is null in message!!!");
        }

        Command command = commandMap.get(commandStr);
        if (command == null){
            throw new RuntimeException("command is not exists!!!");
        }

        return command.serverRun(income, message);
    }
}
