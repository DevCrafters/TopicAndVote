package milkyway.TopicAndVote;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.server.ServerJoinListener;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Developer_Unlocated on 2017-02-27.
 */
public class VoteJoinListener implements ServerJoinListener{
    BotLogger logger = new BotLogger(TopicAndvote.prefix);

    public void onServerJoin(DiscordAPI discordAPI, final Server server) {

        new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println(TopicAndvote.prefix + "���ο� ������ ���� �����Ͽ����ϴ�.");
                    Thread.sleep(1000);
                    final java.util.List<Future<Message>> msg = new ArrayList<>();
                    for(Channel channel : server.getChannels()){
                        Future<Message> msgSingle = logger.sendMessage(channel,"TopicAndVote",new String[]{"**��Ȱ�� ���ǿ� ������ �弳���ٴ� �����սô�,����!**","������ milkyway0308�� ���ڵ� ��ǥ �� **TopicAndVote**�� �����Ͽ����ϴ�.","���� �� ���� : " + TopicAndvote.version ,"__**�� �޽����� 20�� �Ŀ� ������ϴ�.**__"},Color.RED);
                        msg.add(msgSingle);
                    }
                    Thread.sleep(20000);
                    for(Future<Message> msgs : msg){
                        msgs.get().delete();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
