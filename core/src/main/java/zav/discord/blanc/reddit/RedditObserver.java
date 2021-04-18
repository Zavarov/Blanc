package zav.discord.blanc.reddit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.jra.Subreddit;
import zav.jra.exceptions.ForbiddenException;
import zav.jra.exceptions.NotFoundException;
import zav.jra.observer.SubredditObserver;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RedditObserver extends SubredditObserver<RedditListener> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedditObserver.class);

    public RedditObserver(@Nonnull Subreddit subreddit) {
        super(subreddit);
    }

    @Override
    public void notifyListener(RedditListener listener) throws IOException {
        try {
            super.notifyListener(listener);
        } catch(InvalidListenerException e) {
            LOGGER.warn(e.getMessage(), e);
            //Remove the listener since it can't communicate with Discord
            removeListener(listener);
        //Indicate that we're never able to access the subreddit
        } catch(ForbiddenException | NotFoundException e){
            LOGGER.warn(e.getMessage(), e);
            listener.destroy(subreddit);
            //Remove the listener since it no longer listens to the subreddit
            removeListener(listener);
        }

        //In case the observer contains no more listeners, it can be removed
        if(size() == 0){
            throw new InvalidObserverException();
        }
    }
}
