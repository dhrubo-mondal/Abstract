package me.falcon.abstractbot;

import me.falcon.abstractbot.commands.*;
import me.falcon.abstractbot.events.BotOnlineEvent;
import me.falcon.abstractbot.events.JoinAndLeaveEvent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Bot {
    public static String prefix = ",";
    public static String owner_id = System.getenv("OWNER_ID");
    public static void main(String[] args) throws Exception {
        JDABuilder.createDefault(System.getenv("TOKEN"))
                .setActivity(Activity.watching("me getting coded, do "+prefix+"ping"))
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new BotOnlineEvent())
                .addEventListeners(new JoinAndLeaveEvent())
                .addEventListeners(new CustomPrefixCommand())
                .addEventListeners(new RedirectChannelCommand())
                .addEventListeners(new ShutdownCommand())
                .addEventListeners(new PingCommand())
                .addEventListeners(new KickCommand())
                .addEventListeners(new ClearCommand())
                .build();
    }
}
