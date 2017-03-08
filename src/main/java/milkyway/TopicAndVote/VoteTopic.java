package milkyway.TopicAndVote;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Developer_Unlocated on 2017-02-27.
 */
public class VoteTopic implements Serializable{

    private static final long serialVersionUID = 9161893414002102524L;
    String topic = "";
    int agree = 0;
    int disagree = 0;
    Calendar end;
    transient Channel sv;
    String channelID = "";
    String player;
    public VoteTopic(String topic, Channel sv,String player){
        this.topic = topic;
        channelID = sv.getId();
        end = Calendar.getInstance();
        end.add(Calendar.DATE,2);
        this.sv = sv;
        this.player = player;
    }
    public String getEndTime(){
        return end.get(Calendar.YEAR) + "�� " + (end.get(Calendar.MONTH) + 1) + "�� " + end.get(Calendar.DATE) + "�� " + end.get(Calendar.HOUR_OF_DAY) + "�� " + end.get(Calendar.MINUTE) + "�� " + end.get(Calendar.SECOND ) + "��";
    }
    public String getAgreePercent(){
        double ag = (double)agree;
        double dis = (double)disagree;
        return String.format("%.2f",(ag / (ag + dis)) * 100);
    }

    public String getdisAgreePercent(){
        double ag = (double)agree;
        double dis = (double)disagree;
        return String.format("%.2f",(dis / (ag + dis)) * 100);
    }
}
