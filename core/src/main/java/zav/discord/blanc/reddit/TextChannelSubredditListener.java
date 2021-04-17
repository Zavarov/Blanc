package zav.discord.blanc.reddit;

import com.google.common.base.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zav.discord.blanc.Guild;
import zav.discord.blanc.Shard;
import zav.discord.blanc.TextChannel;
import zav.discord.blanc.exceptions.InsufficientPermissionException;
import zav.discord.blanc.exceptions.UnknownChannelException;
import zav.discord.blanc.exceptions.UnknownGuildException;
import zav.jra.Link;
import zav.jra.Subreddit;

import javax.annotation.Nonnull;
import java.io.IOException;

public class TextChannelSubredditListener implements RedditListener {
    private static final Logger LOGGER = LogManager.getLogger(TextChannelSubredditListener.class);
    private final Guild guild;
    private final TextChannel channel;

    public TextChannelSubredditListener(Guild guild, TextChannel channel) {
        this.guild = guild;
        this.channel = channel;
    }

    @Override
    public void newLink(@Nonnull Subreddit subreddit, @Nonnull Link link) throws InvalidListenerException{
        try {
            channel.send(subreddit, link);
        //These exceptions indicate that we're never able to post the link
        } catch(InsufficientPermissionException | UnknownGuildException | UnknownChannelException e){
            destroy(subreddit);
            throw new InvalidListenerException();
        } catch(IOException e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy(String subredditName) {
        //Remove subreddit from channel and update persistence file
        channel.removeSubreddits(subredditName);
        Shard.write(guild, channel);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(guild.getId(), channel.getId());
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof TextChannelSubredditListener) {
            TextChannelSubredditListener listener = (TextChannelSubredditListener) o;
            return listener.guild.getId() == guild.getId() && listener.channel.getId() == channel.getId();
        } else {
            return false;
        }
    }
}
