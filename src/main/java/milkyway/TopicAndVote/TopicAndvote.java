package milkyway.TopicAndVote;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by Developer_Unlocated on 2017-02-27.
 */
public class TopicAndvote {
    static Scroller sr = new Scroller("You vote me right round baby right round like a record baby",16,15);
    final static File saveFile = new File("voteData.votifier");

    final static String prefix = "[TopicAndVote] ";
    final static String version = "#1 베타";
    static BotLogger logger = new BotLogger(TopicAndvote.prefix);
    static Random r = new Random();
    public static void taskPlayerAdd(String vote,String player,int taskid){
        if(!usedTopic.containsKey(vote))
            usedTopic.put(vote,new HashMap<String, List<Integer>>());
        if(!usedTopic.get(vote).containsKey(player))
            usedTopic.get(vote).put(player,new ArrayList<Integer>());
        usedTopic.get(vote).get(player).add(taskid);
    }

    public static boolean taskPlayerHas(String vote,String player,int taskid){
        if(!usedTopic.containsKey(vote))
            usedTopic.put(vote,new HashMap<String, List<Integer>>());
        if(!usedTopic.get(vote).containsKey(player))
            usedTopic.get(vote).put(player,new ArrayList<Integer>());
       return usedTopic.get(vote).get(player).contains(taskid);
    }

    public static void taskPlayerRemove(String vote,String player,int taskid){
        if(!usedTopic.containsKey(vote))
            usedTopic.put(vote,new HashMap<String, List<Integer>>());
        if(!usedTopic.get(vote).containsKey(player))
            usedTopic.get(vote).put(player,new ArrayList<Integer>());
        usedTopic.get(vote).get(player).remove((Integer)taskid);
    }
    public static HashMap<Integer,VoteTopic> getListTopic(String vote){
        if(!topicList.containsKey(vote))
            topicList.put(vote,new HashMap<Integer, VoteTopic>());
        return topicList.get(vote);
    }
    public static boolean disagree(String vote,int taskId,int channelPlayer){
        if(!topicList.containsKey(vote))
            topicList.put(vote,new HashMap<Integer, VoteTopic>());
        if(topicList.get(vote).containsKey(taskId)){
            VoteTopic v = topicList.get(vote).get(taskId);
            v.disagree++;
            int split = channelPlayer / 2;
            if(split <= 0)
                split = 1;
            if(v.agree >= split || v.disagree >= split)
            {
                if(!remove.containsKey(vote))
                    remove.put(vote,new ArrayList<Integer>());
                remove.get(vote).add(taskId);
            }
        }
        return false;
    }

    public static VoteTopic getTopic(String vote, int taskId){
        if(!topicList.containsKey(vote))
            topicList.put(vote,new HashMap<Integer, VoteTopic>());
        return topicList.get(vote).get(taskId);
    }
    public static boolean agree(String vote,int taskId,int channelPlayer){
        if(!topicList.containsKey(vote))
            topicList.put(vote,new HashMap<Integer, VoteTopic>());
        if(topicList.get(vote).containsKey(taskId)){
            VoteTopic v = topicList.get(vote).get(taskId);
            v.agree++;
            int split = channelPlayer / 2;
            if(split <= 0)
                split = 1;
            if(v.agree >= split || v.disagree >= split)
            {
                if(!remove.containsKey(vote))
                    remove.put(vote,new ArrayList<Integer>());
                remove.get(vote).add(taskId);
                for(String s : usedTopic.get(vote).keySet())
                  taskPlayerRemove(vote,s,taskId);
            }
        }
        return false;
    }
    public static int addTopic(String vote,VoteTopic topic){
        if(!topicList.containsKey(vote))
            topicList.put(vote,new HashMap<Integer, VoteTopic>());
        int id = r.nextInt(99999);
        while(topicList.get(vote).containsKey(id))
            id = r.nextInt(99999);
        topicList.get(vote).put(id,topic);
        return id;
    }
    public static void updateVote(String vote){
        Calendar cur = Calendar.getInstance();
        if(topicList.containsKey(vote)){
            for(int topicLister : topicList.get(vote).keySet()){
                VoteTopic v = topicList.get(vote).get(topicLister);
                if(v.sv == null)
                    continue;
                if(cur.after(v.end) || (remove.containsKey(vote) && remove.get(vote).contains(topicLister))){
                    if(v.agree == v.disagree){
                        logger.sendMessage(v.sv,"건의 #" + topicLister + "번이 종료되었습니다.",new String[]{"건의 내용:","**__" + v.topic + "__**","","해당 안건이 찬성 " + v.agree + "표,반대 " + v.disagree + "표로 부결되었습니다."}, Color.black);
                        if(!remove.containsKey(vote))
                            remove.put(vote,new ArrayList<Integer>());
                        remove.get(vote).add(topicLister);
                        subPlayer(vote,v.player);
                    }else if(v.agree > v.disagree){
                        logger.sendMessage(v.sv,"건의 #" + topicLister + "번이 종료되었습니다.",new String[]{"건의 내용:","**__" + v.topic + "__**","","해당 안건이 찬성 " + v.agree + "표,반대 " + v.disagree + "표로 가결되었습니다."}, Color.green);
                        if(!remove.containsKey(vote))
                            remove.put(vote,new ArrayList<Integer>());
                       remove.get(vote).add(topicLister);
                        subPlayer(vote,v.player);
                    }else if(v.agree < v.disagree){
                        logger.sendMessage(v.sv,"건의 #" + topicLister + "번이 종료되었습니다.",new String[]{"건의 내용:","**__" + v.topic + "__**","","해당 안건이 찬성 " + v.agree + "표,반대 " + v.disagree + "표로 부결되었습니다."}, Color.red);
                        if(!remove.containsKey(vote))
                            remove.put(vote,new ArrayList<Integer>());
                        subPlayer(vote,v.player);
                        remove.get(vote).add(topicLister);
                    }
                }
            }

        }else
            topicList.put(vote,new HashMap<Integer, VoteTopic>());
    }
    public static void removeVote(){
        for(String a : remove.keySet())
            if(topicList.containsKey(a))
                for(int b : remove.get(a))
                    topicList.get(a).remove(b);
        remove.clear();
    }


    public static void removeVote(String vote,int taskId){
        if(!remove.containsKey(vote))
            remove.put(vote,new ArrayList<Integer>());
        remove.get(vote).add(taskId);
    }
    public static VoteTopic getVote(String vote,int taskId){
        if(!topicList.containsKey(vote))
            topicList.put(vote,new HashMap<Integer, VoteTopic>());
        return topicList.get(vote).get(taskId);
    }
    public static boolean isRemoveVote(String vote,int taskId){
       if(!remove.containsKey(vote))
           return false;
       return remove.get(vote).contains(taskId);
    }
    public static void addPlayer(String vote,String player){
        if(playerTopic.containsKey(vote)){
            if(!playerTopic.get(vote).containsKey(player))
                 playerTopic.get(vote).put(player, 1);
            else
                playerTopic.get(vote).put(player, playerTopic.get(vote).get(player)+1);
        }else
        {
            playerTopic.put(vote,new HashMap<String, Integer>());
            if(!playerTopic.get(vote).containsKey(player))
                playerTopic.get(vote).put(player, 1);
            else
                playerTopic.get(vote).put(player, playerTopic.get(vote).get(player)+1);
        }
    }

    public static void subPlayer(String vote,String player){
        if(playerTopic.containsKey(vote)){
            if(!playerTopic.get(vote).containsKey(player))
                playerTopic.get(vote).put(player, 0);
            else
                playerTopic.get(vote).put(player, playerTopic.get(vote).get(player)-1);
        }else
        {
            playerTopic.put(vote,new HashMap<String, Integer>());
            if(!playerTopic.get(vote).containsKey(player))
                playerTopic.get(vote).put(player, 0);
            else
                playerTopic.get(vote).put(player, playerTopic.get(vote).get(player)-1);
        }
    }

    public static int getPlayer(String vote,String player){
        if(playerTopic.containsKey(vote)){
            if(playerTopic.get(vote).containsKey(player))
            {
                return playerTopic.get(vote).get(player);
            }
            else
               return 0;
        }else
        {
            playerTopic.put(vote,new HashMap<String, Integer>());
           return 0;
        }
    }

    static java.util.List<String> isUpdated = new ArrayList<>();
    public static void updateServer(Server sv){
        if(isUpdated.contains(sv.getId()))
            return;
        hasConnected.add(sv.getId());
        isUpdated.add(sv.getId());

        if(topicList.containsKey(sv.getId()))
            for(int id : topicList.get(sv.getId()).keySet())
            {
                Channel a = sv.getChannelById(topicList.get(sv.getId()).get(id).channelID);

                if(a == null)
                {
                    if(!remove.containsKey(sv.getId()))
                        remove.put(sv.getId(),new ArrayList<Integer>());
                    remove.get(sv.getId()).add(id);
                }else
                    topicList.get(sv.getId()).get(id).sv = a;
            }
    }

    public static void remServer(Server sv){
        if(isUpdated.contains(sv.getId()))
            isUpdated.remove(sv.getId());
        if(topicList.containsKey(sv.getId()))
            topicList.remove(sv.getId());
        if(usedTopic.containsKey(sv.getId()))
            usedTopic.remove(sv.getId());
        if(playerTopic.containsKey(sv.getId()))
            playerTopic.remove(sv.getId());
        if(hasConnected.contains(sv.getId()))
            hasConnected.remove(sv.getId());
    }
    private static void save(){
        File saveFile = new File("voteData.votifier");
        System.out.println(prefix + "저장중..");
        try{

            if(saveFile.exists())
                saveFile.delete();
            saveFile.createNewFile();
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(saveFile));
            stream.writeObject(topicList);
            stream.writeObject(playerTopic);
            stream.writeObject(usedTopic);
            stream.writeObject(remove);
            stream.writeObject(hasConnected);
            stream.flush();
            stream.close();
        }catch (Exception ex){ex.printStackTrace();}
    }
    public static void main(String[] args){
        if(saveFile.exists()){
            try{
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(saveFile));
                topicList = (HashMap<String, HashMap<Integer, VoteTopic>>) stream.readObject();
                playerTopic = (HashMap<String, HashMap<String, Integer>>) stream.readObject();
                usedTopic = (HashMap<String, HashMap<String, List<Integer>>>) stream.readObject();
                remove = (HashMap<String, List<Integer>>) stream.readObject();
                hasConnected = (Set<String>) stream.readObject();
            }catch (Exception e){}
        }
        //
        DiscordAPI apiCreation = Javacord.getApi("Token ID",true);
       apiCreation.connect(new FutureCallback<DiscordAPI>() {
           @Override
           public void onSuccess(DiscordAPI discordAPI) {

           }

           @Override
           public void onFailure(Throwable throwable) {

           }
       });
        apiCreation.connect(new FutureCallback<DiscordAPI>() {
            public void onSuccess(final DiscordAPI discordAPI) {
                discordAPI.setGame("You vote me right round");
                System.out.println(prefix + "투표 봇 TopicAndVote 버전 " + version + "가 활성화되었습니다.");
                discordAPI.registerListener(new VoteListener());
                discordAPI.registerListener(new VoteJoinListener());
                new Thread(){
                    @Override
                    public void run() {
                        while(true){
                            try {
                                Thread.sleep(1000L);
                                new Thread(){
                                    @Override
                                    public void run() {
                                        for(String s : topicList.keySet())
                                            updateVote(s);
                                        removeVote();
                                    }
                                }.start();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
                new Thread() {
                    @Override
                    public void run() {
                        String n ;
                        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
                        try {
                            while((n = r.readLine()) != null ) {
                                if(n.equals("저장")){
                                    save();
                                   System.out.println(prefix + "저장 완료");
                                }else if(n.equals("서버")){
                                    System.out.println("=========================================================");
                                    System.out.println(prefix + topicList.keySet().size() + "개의 서버에서 봇 기능을 사용한 전적이 있습니다.");
                                    System.out.println(prefix + isUpdated.size() + "개의 서버가 온라인입니다.");
                                    System.out.println(prefix + hasConnected.size() + "개의 서버에 봇이 참여되어 있습니다.");
                                    System.out.println("=========================================================");
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                new Thread(){
                    @Override
                    public void run() {
                        while(true)
                        {
                            try {
                                Thread.sleep(600000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            save();
                            System.out.println(prefix + "저장 완료");
                        }
                    }
                }.start();
            }

            public void onFailure(Throwable throwable) {

            }
        });

    }
    private static Set<String> hasConnected = new HashSet<>();
    private static HashMap<String,HashMap<Integer,VoteTopic>> topicList = new HashMap<>();
    private static HashMap<String,HashMap<String,Integer>> playerTopic = new HashMap<>();
    private static HashMap<String,HashMap<String,List<Integer>>> usedTopic = new HashMap<>();
    private  static HashMap<String,List<Integer>> remove = new HashMap<>();
}
