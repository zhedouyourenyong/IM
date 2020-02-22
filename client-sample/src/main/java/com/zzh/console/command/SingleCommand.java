package com.zzh.console.command;

import com.zzh.console.ConsoleCommand;
import com.zzh.core.ChatApi;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @version v1.0
 * @ProjectName: im
 * @Description: 私聊命令
 * @Author: Administrator
 * @Date: 2020/2/5 18:22
 */
@Slf4j
public class SingleCommand implements ConsoleCommand
{
    @Override
    public void exec(Scanner scanner, ChatApi chatApi)
    {
        System.out.print("目标用户ID:");
        String destId = scanner.next();
        System.out.print("内容:");
        String content = scanner.next();

        chatApi.sendText(destId, content);
    }
}
