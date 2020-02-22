package com.zzh.console;

import com.zzh.core.ChatApi;
import io.netty.channel.Channel;

import java.util.Scanner;

public interface ConsoleCommand
{
    void exec(Scanner scanner, ChatApi chatApi);
}
