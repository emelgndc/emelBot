package thisisapackage.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.CertainMessageEvent;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SuperPingCommand implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        //Check if the command has been called
        if (event.getMessageContent().startsWith("!superping")) {
            MessageAuthor sender = event.getMessageAuthor();

            //Check if the message is from a bot
            if (sender.isBotUser()) {
                return;
            }

            //Storing stuff
            String msgStr = event.getMessageContent();
            String[] msgSpl = msgStr.split("::", 0);
            List<User> userList = event.getMessage().getMentionedUsers();

            //Check if command was called without parameters
            if (msgStr.equals("!superping")) {
                new MessageBuilder()
                        .append("here are the parameters")
                        .appendCode("java", "!superping [message]::[repeats(max 20)]::[time between repeats(max 60)]")
                        .append("e.g. !superping @poopy asdf::5::10 will ping poopy the message 'asdf' 5 times with 10 second gaps in between")
                        .send(event.getChannel());
                return;
            //Check for incorrect command call, e.g. !superpingsdfjsdf
            } else if (!msgStr.startsWith("!superping ")) {
                return;
            }

            //Check if the command is formatted correctly
            if (msgSpl.length != 3) {
                new MessageBuilder()
                        .append("use the right parameters g")
                        .appendCode("java", "!superping [message]::[repeats(max 20)]::[time between repeats(max 60)]")
                        .append("e.g. !superping @poopy asdf::5::10 will ping poopy the message 'asdf' 5 times with 10 second gaps in between")
                        .send(event.getChannel());
            //Check if integers have been used
            } else if (!msgSpl[1].matches("[0-9]+") || !msgSpl[2].matches("[0-9]+")) {
                event.getChannel().sendMessage("you have to use numbers for repeats and time g");
            //Check if 'repeats' is within the correct bounds
            } else if (Integer.parseInt(msgSpl[1]) > 20) {
                event.getChannel().sendMessage("repeats has to be under 20 g");
            //Check if 'time' is within the correct bounds
            } else if (Integer.parseInt(msgSpl[2]) > 60) {
                event.getChannel().sendMessage("time has to be under 60 g");
            //Check if someone has been pinged within the message
            } else if (userList.size() < 1) {
                event.getChannel().sendMessage("you have to ping someone g");
            } else {

                //User has passed all checks!
                //Remove leading '!superping ' from message
                String newMsg = msgSpl[0].substring(11);

                class Helper extends TimerTask {
                    @Override
                    public void run() {
                        event.getChannel().sendMessage(newMsg);
                    }
                }

                Timer timer = new Timer();
                int repeats = Integer.parseInt(msgSpl[1]);
                int time = Integer.parseInt(msgSpl[2])*1000;

                for (int i = 0; i < repeats; i++) {
                    TimerTask task = new Helper();
                    timer.schedule(task, i*time);
                }
            }
        }
    }
}
