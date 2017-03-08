package milkyway.TopicAndVote;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.concurrent.Future;

/**
 * Created by Developer_Unlocated on 2017-02-27.
 */
public class BotLogger {
    final String prefix;
    public BotLogger(String prefixer){
        this.prefix = prefixer;

    }
    public Future<Message> sendMessage(Channel channel, String title, String[] message, Color color){
        if(message.length <= 0)
            return null;
        StringBuilder msg = new StringBuilder(message[0]);
        for(int i = 1;i < message.length;i++)
            msg.append("\n" + message[i]);
        EmbedBuilder b = new EmbedBuilder();
        b.setColor(color);
        b.setTitle(title);
        b.setDescription(msg.toString());
        return channel.sendMessage("",b);
    }
    public Future<Message> sendMessage(Channel channel, String title, java.util.List<String> message, Color color){
        if(message.size() <= 0)
            return null;
        StringBuilder msg = new StringBuilder(message.get(0));
        for(int i = 1;i < message.size();i++)
            msg.append("\n" + message.get(i));
        EmbedBuilder b = new EmbedBuilder();
        b.setColor(color);
        b.setTitle(title);
        b.setDescription(msg.toString());
        return channel.sendMessage("",b);
    }
    public Future<Message> sendMessage(User user, String title, String[] message, Color color){
        if(message.length <= 0)
            return null;
        StringBuilder msg = new StringBuilder(message[0]);
        for(int i = 1;i < message.length;i++)
            msg.append("\n" + message[i]);
        EmbedBuilder b = new EmbedBuilder();
        b.setColor(color);
        b.setTitle(title);
        b.setDescription(msg.toString());
        return user.sendMessage("",b);
    }

    public Future<Message> sendMessage(User user, String title, java.util.List<String> message, Color color){
        if(message.size() <= 0)
            return null;
        StringBuilder msg = new StringBuilder(message.get(0));
        for(int i = 1;i < message.size();i++)
            msg.append("\n" + message.get(i));
        EmbedBuilder b = new EmbedBuilder();
        b.setColor(color);
        b.setTitle(title);
        b.setDescription(msg.toString());
        return user.sendMessage("",b);
    }
}
