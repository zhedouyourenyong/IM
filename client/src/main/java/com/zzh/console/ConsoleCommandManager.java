package com.zzh.console;

import com.zzh.console.command.SingleCommand;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description:
 * @Author: Administrator
 * @Date: 2020/2/4 23:38
 */
@Slf4j
public class ConsoleCommandManager implements ConsoleCommand
{
    private Map<String, ConsoleCommand> consoleCommandMap = new HashMap<>();

    public ConsoleCommandManager()
    {
        consoleCommandMap.put("single", new SingleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel)
    {
        System.out.print("请输入指令:");
        String command = scanner.next();

        ConsoleCommand handler = consoleCommandMap.get(command);
        if (handler == null)
        {
            System.out.println("无法识别当前指令,:" + command);
        } else
        {
            handler.exec(scanner, channel);
        }
    }
}
