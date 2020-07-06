package thisisapackage;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.Message;
import thisisapackage.commands.SuperPingCommand;

public class Main {

    public static void main(String[] args) {

        //Generate invite link for the bot
        DiscordApi api = new DiscordApiBuilder().setToken("tokenhere").login().join();
        //System.out.println(api.createBotInvite());

        //Add listeners
        api.addMessageCreateListener(new SuperPingCommand());
    }

}