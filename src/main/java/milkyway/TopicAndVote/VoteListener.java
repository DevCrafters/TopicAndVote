package milkyway.TopicAndVote;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.*;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.message.MessageCreateListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Developer_Unlocated on 2017-02-27.
 */
public class VoteListener implements MessageCreateListener {
    String logItem = "+건의 ";
    String agreeItem = "+찬성 ";
    String disagreeItem = "+반대 ";
    String endItem = "+종료 ";
    String listItem = "+목록";

    int minLength = 8;
    static int maxItem = 3;
    BotLogger logger = new BotLogger(TopicAndvote.prefix);
    java.util.Queue<Runnable> run = new ConcurrentLinkedQueue<>();

    public void onMessageCreate(DiscordAPI discordAPI, final Message message) {
        if(message == null)
            return;
        else if(message.getChannelReceiver() == null)
            return;

        TopicAndvote.updateServer(message.getChannelReceiver().getServer());
        String msg = message.getContent();
        if(msg.startsWith(logItem)){
            String topical = msg.substring(logItem.length(),msg.length());
            if(topical.length() < minLength)
            {

                final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),"건의 주제의 길이가 너무 짧습니다!",new String[]{"건의 내용의 길이는 최소 **__" + minLength + "자__**여야만 합니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            }
            if(TopicAndvote.getPlayer(message.getChannelReceiver().getServer().getId(),message.getAuthor().getId()) >= maxItem)
            {
                final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),"너무 많은 건의를 하였습니다!",new String[]{"이미 **__" + maxItem + "개__**이상의 건의를 하였습니다.","추가 건의를 하고 싶다면 **" + endItem + " [건의 ID]**로 건의를 종료하세요.","건의 목록은 " + listItem + "으로 조회가 가능합니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            }
           System.out.println(message.getAuthor().getRoles(message.getChannelReceiver().getServer()));
            message.delete();
            TopicAndvote.addPlayer(message.getChannelReceiver().getServer().getId(),message.getAuthor().getId());
            VoteTopic topic = new VoteTopic(topical,message.getChannelReceiver(),message.getAuthor().getId());
            int voteID = TopicAndvote.addTopic(message.getChannelReceiver().getServer().getId(),topic);
            logger.sendMessage(message.getChannelReceiver(),topical,new String[]{"__** @" + message.getAuthor().getName() + " **__님이 건의한 주제입니다.","과반수 이상 찬성시,투표가 **찬성**으로 종료됩니다.","이 안건에 찬성하신다면 __" + agreeItem + voteID + "__를,반대하신다면 __"+ disagreeItem + voteID + "__를 사용하세요.","" ,"이 투표는 " + topic.getEndTime() + "에 종료됩니다."}, Color.blue);
        }else if(msg.startsWith(agreeItem)){
            String token =  msg.substring(agreeItem.length(),msg.length());
            if(!isInteger(token))
            {
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"건의 ID는 숫자여야 합니다.",new String[]{"건의의 ID는 숫자여야만 합니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                message.delete();
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            }
            int tok = Integer.parseInt(token);
            VoteTopic vote = TopicAndvote.getVote(message.getChannelReceiver().getServer().getId(),tok);
            if(TopicAndvote.isRemoveVote(message.getChannelReceiver().getServer().getId(),tok))
            {
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"이미 종료된 안건입니다.",new String[]{"이미 이 채널에서 종료된 안건입니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
             else if(vote == null)
            {
                final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),"존재하지 않는 안건입니다.",new String[]{"이 채널에서 존재하지 않는 안건입니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            else{
                if(TopicAndvote.taskPlayerHas(message.getChannelReceiver().getServer().getId(),message.getAuthor().getId(),tok)){
                    message.delete();
                    final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),message.getAuthor().getName() + "님,당신은 이미 해당 안건에 투표하였습니다!",new String[]{"이미 투표를 진행하신 안건입니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                m.get().delete();
                            } catch (InterruptedException e) {

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    return;
                }
                message.delete();
                TopicAndvote.taskPlayerAdd(message.getChannelReceiver().getServer().getId(),message.getAuthor().getId(),tok);
                TopicAndvote.agree(message.getChannelReceiver().getServer().getId(),tok,message.getChannelReceiver().getServer().getMemberCount());
                boolean isTrue = vote.agree > vote.disagree;
                logger.sendMessage(message.getChannelReceiver(),"누군가가 건의에 찬성하였습니다.",new String[]{"누군가가 다음의 건의에 찬성하였습니다 :","__**" + vote.topic + "**__","찬성 " + vote.getAgreePercent() + "% | 반대 " + vote.getdisAgreePercent() + "%","Vote ID: " + tok},((isTrue) ? Color.GREEN : Color.RED));

            }
        }else if(msg.startsWith(disagreeItem)){
            String token =  msg.substring(agreeItem.length(),msg.length());
            if(!isInteger(token))
            {
                final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),"건의 ID는 숫자여야 합니다.",new String[]{"건의의 ID는 숫자여야만 합니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            }

            int tok = Integer.parseInt(token);
            VoteTopic vote = TopicAndvote.getVote(message.getChannelReceiver().getServer().getId(),tok);
            if(TopicAndvote.isRemoveVote(message.getChannelReceiver().getServer().getId(),tok))
            {
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"이미 종료된 안건입니다.",new String[]{"이미 이 채널에서 종료된 안건입니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
             else if(vote == null)
            {
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"존재하지 않는 안건입니다.",new String[]{"이 채널에서 존재하지 않는 안건입니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
             else{
                if(TopicAndvote.taskPlayerHas(message.getChannelReceiver().getServer().getId(),message.getAuthor().getId(),tok)){
                    message.delete();
                    final Future<Message> m =   logger.sendMessage(message.getChannelReceiver(),message.getAuthor().getName() + "님,당신은 이미 해당 안건에 투표하였습니다!",new String[]{"이미 투표를 진행하신 안건입니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                m.get().delete();
                            } catch (InterruptedException e) {

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    return;
                }
                boolean isTrue = vote.agree > vote.disagree;
                message.delete();
                TopicAndvote.taskPlayerAdd(message.getChannelReceiver().getServer().getId(),message.getAuthor().getId(),tok);
                TopicAndvote.disagree(message.getChannelReceiver().getServer().getId(),tok,message.getChannelReceiver().getServer().getMemberCount());
                logger.sendMessage(message.getChannelReceiver(),"누군가가 건의에 반대하였습니다.",new String[]{"누군가가 다음의 건의에 반대하였습니다 :","__**" + vote.topic + "**__","찬성 " + vote.getAgreePercent() + "% | 반대 " + vote.getdisAgreePercent() + "%","Vote ID: " + tok},((isTrue) ? Color.GREEN : Color.RED));

            }

        }else if(msg.startsWith(listItem)){
            message.delete();
            java.util.List<String> msgs = new ArrayList<>();
            HashMap<Integer,VoteTopic> listTopic = TopicAndvote.getListTopic(message.getChannelReceiver().getServer().getId());
            if(listTopic.size() <= 0){
                msgs.add("아직 진행중인 안건이 없습니다.");
                logger.sendMessage(message.getChannelReceiver(),"진행중인 안건이 없습니다.",msgs,Color.GRAY);
            }else{
                for(int a : listTopic.keySet())
                {
                    msgs.add("**#" + a+ "** - __" + listTopic.get(a).topic + "__");
                }
                logger.sendMessage(message.getChannelReceiver(),listTopic.size() + "개의 안건이 진행중입니다.",msgs,Color.GRAY);
            }

        }else if(msg.startsWith(endItem)){
            String token =  msg.substring(agreeItem.length(),msg.length());
            if(!isInteger(token))
            {
                final Future<Message> m =   logger.sendMessage(message.getChannelReceiver(),"건의 ID는 숫자여야 합니다.",new String[]{"건의의 ID는 숫자여야만 합니다.","_이 메시지는 3초 후에 사라집니다._"},Color.RED);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            }
            message.delete();
            int tok = Integer.parseInt(token);

            VoteTopic topic = TopicAndvote.getTopic(message.getChannelReceiver().getServer().getId(),tok);
            if(topic == null)
            {
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"등록되지 않은 안건입니다.",new String[]{"해당 ID의 안건은 등록되지 않았습니다.","_이 메시지는 3초 후에 사라집니다._"},Color.GRAY);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            m.get().delete();
                        } catch (InterruptedException e) {

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            else{
                 if(!topic.player.equals(message.getAuthor().getId())){
                     final Future<Message> m =   logger.sendMessage(message.getChannelReceiver(),"권한이 부족합니다.",new String[]{"해당 ID의 안건은 당신이 건의한 안건이 아닙니다.","_이 메시지는 3초 후에 사라집니다._"},Color.GRAY);
                     new Thread(){
                         @Override
                         public void run() {
                             try {
                                 Thread.sleep(3000);
                                 m.get().delete();
                             } catch (InterruptedException e) {

                             } catch (ExecutionException e) {
                                 e.printStackTrace();
                             }
                         }
                     }.start();
                     return;
                 }
                TopicAndvote.removeVote(message.getChannelReceiver().getServer().getId(),tok);
                logger.sendMessage(message.getAuthor(),"곧 해당 안건이 종료됩니다.",new String[]{"당신이 종료를 의뢰한 안건이 큐 리스트에 추가되었습니다."},Color.GREEN);
            }


        }
    }
    boolean isInteger(String token){
        try{
            Integer.parseInt(token);
            return true;
        }catch(Exception e){}
        return false;
    }

}
