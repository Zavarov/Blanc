package zav.discord.blanc.reddit;

import zav.jra.Subreddit;
import zav.jra.listener.SubredditListener;

public interface RedditListener extends SubredditListener {
    default void destroy(Subreddit subreddit){
        destroy(subreddit.getDisplayName());
    }

    void destroy(String name);
}
