package zav.discord.blanc.reddit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class RedditRunnable implements Runnable{
    private static final Logger LOGGER = LogManager.getLogger(RedditRunnable.class);
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
