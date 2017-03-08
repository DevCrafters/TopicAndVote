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
    String logItem = "+���� ";
    String agreeItem = "+���� ";
    String disagreeItem = "+�ݴ� ";
    String endItem = "+���� ";
    String listItem = "+���";

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

                final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),"���� ������ ���̰� �ʹ� ª���ϴ�!",new String[]{"���� ������ ���̴� �ּ� **__" + minLength + "��__**���߸� �մϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),"�ʹ� ���� ���Ǹ� �Ͽ����ϴ�!",new String[]{"�̹� **__" + maxItem + "��__**�̻��� ���Ǹ� �Ͽ����ϴ�.","�߰� ���Ǹ� �ϰ� �ʹٸ� **" + endItem + " [���� ID]**�� ���Ǹ� �����ϼ���.","���� ����� " + listItem + "���� ��ȸ�� �����մϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
            logger.sendMessage(message.getChannelReceiver(),topical,new String[]{"__** @" + message.getAuthor().getName() + " **__���� ������ �����Դϴ�.","���ݼ� �̻� ������,��ǥ�� **����**���� ����˴ϴ�.","�� �Ȱǿ� �����ϽŴٸ� __" + agreeItem + voteID + "__��,�ݴ��ϽŴٸ� __"+ disagreeItem + voteID + "__�� ����ϼ���.","" ,"�� ��ǥ�� " + topic.getEndTime() + "�� ����˴ϴ�."}, Color.blue);
        }else if(msg.startsWith(agreeItem)){
            String token =  msg.substring(agreeItem.length(),msg.length());
            if(!isInteger(token))
            {
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"���� ID�� ���ڿ��� �մϴ�.",new String[]{"������ ID�� ���ڿ��߸� �մϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"�̹� ����� �Ȱ��Դϴ�.",new String[]{"�̹� �� ä�ο��� ����� �Ȱ��Դϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),"�������� �ʴ� �Ȱ��Դϴ�.",new String[]{"�� ä�ο��� �������� �ʴ� �Ȱ��Դϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                    final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),message.getAuthor().getName() + "��,����� �̹� �ش� �Ȱǿ� ��ǥ�Ͽ����ϴ�!",new String[]{"�̹� ��ǥ�� �����Ͻ� �Ȱ��Դϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                logger.sendMessage(message.getChannelReceiver(),"�������� ���ǿ� �����Ͽ����ϴ�.",new String[]{"�������� ������ ���ǿ� �����Ͽ����ϴ� :","__**" + vote.topic + "**__","���� " + vote.getAgreePercent() + "% | �ݴ� " + vote.getdisAgreePercent() + "%","Vote ID: " + tok},((isTrue) ? Color.GREEN : Color.RED));

            }
        }else if(msg.startsWith(disagreeItem)){
            String token =  msg.substring(agreeItem.length(),msg.length());
            if(!isInteger(token))
            {
                final Future<Message> m = logger.sendMessage(message.getChannelReceiver(),"���� ID�� ���ڿ��� �մϴ�.",new String[]{"������ ID�� ���ڿ��߸� �մϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"�̹� ����� �Ȱ��Դϴ�.",new String[]{"�̹� �� ä�ο��� ����� �Ȱ��Դϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"�������� �ʴ� �Ȱ��Դϴ�.",new String[]{"�� ä�ο��� �������� �ʴ� �Ȱ��Դϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                    final Future<Message> m =   logger.sendMessage(message.getChannelReceiver(),message.getAuthor().getName() + "��,����� �̹� �ش� �Ȱǿ� ��ǥ�Ͽ����ϴ�!",new String[]{"�̹� ��ǥ�� �����Ͻ� �Ȱ��Դϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                logger.sendMessage(message.getChannelReceiver(),"�������� ���ǿ� �ݴ��Ͽ����ϴ�.",new String[]{"�������� ������ ���ǿ� �ݴ��Ͽ����ϴ� :","__**" + vote.topic + "**__","���� " + vote.getAgreePercent() + "% | �ݴ� " + vote.getdisAgreePercent() + "%","Vote ID: " + tok},((isTrue) ? Color.GREEN : Color.RED));

            }

        }else if(msg.startsWith(listItem)){
            message.delete();
            java.util.List<String> msgs = new ArrayList<>();
            HashMap<Integer,VoteTopic> listTopic = TopicAndvote.getListTopic(message.getChannelReceiver().getServer().getId());
            if(listTopic.size() <= 0){
                msgs.add("���� �������� �Ȱ��� �����ϴ�.");
                logger.sendMessage(message.getChannelReceiver(),"�������� �Ȱ��� �����ϴ�.",msgs,Color.GRAY);
            }else{
                for(int a : listTopic.keySet())
                {
                    msgs.add("**#" + a+ "** - __" + listTopic.get(a).topic + "__");
                }
                logger.sendMessage(message.getChannelReceiver(),listTopic.size() + "���� �Ȱ��� �������Դϴ�.",msgs,Color.GRAY);
            }

        }else if(msg.startsWith(endItem)){
            String token =  msg.substring(agreeItem.length(),msg.length());
            if(!isInteger(token))
            {
                final Future<Message> m =   logger.sendMessage(message.getChannelReceiver(),"���� ID�� ���ڿ��� �մϴ�.",new String[]{"������ ID�� ���ڿ��߸� �մϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.RED);
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
                final Future<Message> m =  logger.sendMessage(message.getChannelReceiver(),"��ϵ��� ���� �Ȱ��Դϴ�.",new String[]{"�ش� ID�� �Ȱ��� ��ϵ��� �ʾҽ��ϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.GRAY);
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
                     final Future<Message> m =   logger.sendMessage(message.getChannelReceiver(),"������ �����մϴ�.",new String[]{"�ش� ID�� �Ȱ��� ����� ������ �Ȱ��� �ƴմϴ�.","_�� �޽����� 3�� �Ŀ� ������ϴ�._"},Color.GRAY);
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
                logger.sendMessage(message.getAuthor(),"�� �ش� �Ȱ��� ����˴ϴ�.",new String[]{"����� ���Ḧ �Ƿ��� �Ȱ��� ť ����Ʈ�� �߰��Ǿ����ϴ�."},Color.GREEN);
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
