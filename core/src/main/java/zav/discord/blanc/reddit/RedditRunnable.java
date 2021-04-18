package zav.discord.blanc.reddit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RedditRunnable implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedditRunnable.class);
    private final RedditObservable observable;

    public RedditRunnable (RedditObservable observable) {
        this.observable = observable;
    }

    @Override
    public void run() {
        try {
            observable.notifyAllObservers();
        } catch(IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
