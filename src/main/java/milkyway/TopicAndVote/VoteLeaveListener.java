package milkyway.TopicAndVote;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.listener.server.ServerLeaveListener;

/**
 * Created by Developer_Unlocated on 2017-02-28.
 */
public class VoteLeaveListener implements ServerLeaveListener{
    @Override
    public void onServerLeave(DiscordAPI discordAPI, Server server) {
        TopicAndvote.remServer(server);
    }
}
