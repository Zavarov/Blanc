package vartas.discord.bot.mpi.command;

import org.jetbrains.annotations.NotNull;
import vartas.discord.bot.RedditFeed;
import vartas.discord.bot.mpi.MPICoreCommands;
import vartas.discord.bot.mpi.serializable.MPISubredditFeedModification;

public class MPIAddRedditFeedToTextChannel extends MPICommand<MPISubredditFeedModification> {
    @Override
    public void visit(@NotNull String subreddit, @NotNull RedditFeed.SubredditFeed subredditFeed){
        if(subreddit.equals(message.getSubredditName()))
            subredditFeed.add(message.getGuildId(), message.getChannelId());
    }

    @Override
    protected int getCode() {
        return MPICoreCommands.MPI_ADD_REDDIT_FEED_TO_TEXT_CHANNEL.getCode();
    }
}
