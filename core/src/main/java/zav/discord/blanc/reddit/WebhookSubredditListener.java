package zav.discord.blanc.reddit;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc.Guild;
import zav.discord.blanc.Shard;
import zav.discord.blanc.Webhook;
import zav.discord.blanc.exceptions.WebhookException;
import zav.jra.Link;
import zav.jra.Subreddit;

import javax.annotation.Nonnull;
import java.io.IOException;

public class WebhookSubredditListener implements RedditListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookSubredditListener.class);
    private final Guild guild;
    private final Webhook webhook;

    public WebhookSubredditListener(Guild guild, Webhook webhook){
        this.guild = guild;
        this.webhook = webhook;
    }

    @Override
    public void newLink(@Nonnull Subreddit subreddit, @Nonnull Link link) throws InvalidListenerException{
        try {
            webhook.send(subreddit, link);
            //This exception indicates that we're never able to post the link
        } catch(WebhookException e){
            destroy(subreddit);
            throw new InvalidListenerException();
        } catch(IOException e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy(String subredditName) {
        //Remove subreddit from webhook and update persistence file
        webhook.removeSubreddits(subredditName);
        Shard.write(guild, webhook);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(guild.getId(), webhook.getId());
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof WebhookSubredditListener) {
            WebhookSubredditListener listener = (WebhookSubredditListener) o;
            return listener.guild.getId() == guild.getId() && listener.webhook.getId() == webhook.getId();
        } else {
            return false;
        }
    }
}
