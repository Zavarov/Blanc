package zav.discord.blanc.reddit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedditRunnable implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedditRunnable.class);
    private final RedditObservable observable;

    public RedditRunnable (RedditObservable observable) {
        this.observable = observable;
    }

    @Override
    public void run() {
        LOGGER.info("Update Reddit feed.");
        try {
            observable.notifyAllObservers();
        } catch(Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
