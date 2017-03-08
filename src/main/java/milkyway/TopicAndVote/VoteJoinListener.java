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
                    System.out.println(TopicAndvote.prefix + "새로운 서버에 봇이 참가하였습니다.");
                    Thread.sleep(1000);
                    final java.util.List<Future<Message>> msg = new ArrayList<>();
                    for(Channel channel : server.getChannels()){
                        Future<Message> msgSingle = logger.sendMessage(channel,"TopicAndVote",new String[]{"**원활한 현피와 끝없는 욕설보다는 말로합시다,말로!**","개발자 milkyway0308의 디스코드 투표 봇 **TopicAndVote**가 입장하였습니다.","현재 봇 버전 : " + TopicAndvote.version ,"__**이 메시지는 20초 후에 사라집니다.**__"},Color.RED);
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
