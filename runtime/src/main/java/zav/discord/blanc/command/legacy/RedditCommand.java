/*
 * Copyright (c) 2020 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package zav.discord.blanc.command.legacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc.Main;
import zav.discord.blanc.Shard;
import zav.discord.blanc.TextChannel;
import zav.discord.blanc.reddit.TextChannelSubredditListener;
import zav.jra.Subreddit;

import java.io.IOException;

/**
 * This command allows to link subreddits to channels.
 */
public class RedditCommand extends RedditCommandTOP {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedditCommand.class);

    private TextChannel channel;
    private Subreddit subreddit;
    private TextChannelSubredditListener listener;

    @Override
    public void run() throws IOException {
        channel = getTextChannel().orElse(get$TextChannel());

        try {
            subreddit = Main.REDDIT_CLIENT.getSubreddit(getSubreddit());
            listener = new TextChannelSubredditListener(get$Guild(), channel);

            if (channel.containsSubreddits(getSubreddit())) {
                removeSubreddit();
            } else {
                addSubreddit();
            }
        } catch(InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void removeSubreddit() throws IOException {
        //Listeners are unique with respect to guild & text channel id
        Main.REDDIT_OBSERVABLE.get(subreddit).removeListener(listener);

        //Update persistence file
        channel.removeSubreddits(getSubreddit());
        Shard.write(get$Guild(), channel);

        get$TextChannel().send("Submissions from r/%s will no longer be posted in %s.", subreddit.getDisplayName(), channel.getAsMention());
    }

    private void addSubreddit() throws IOException {
        //Cancel the command if the bot can't post them in the targeted channel
        if(!get$Guild().canInteract(get$Guild().retrieveSelfMember(), channel)){
            get$TextChannel().send("I can't interact with "+channel.getName());
            return;
        }

        //Update the persistence file
        channel.addSubreddits(getSubreddit());
        Shard.write(get$Guild(), channel);

        //Register the new Reddit feed
        Main.REDDIT_OBSERVABLE.get(subreddit).addListener(listener);

        get$TextChannel().send("Submissions from r/%s will be posted in %s.", subreddit.getDisplayName(), channel.getAsMention());
    }
}
