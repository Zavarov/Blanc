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
package zav.discord.blanc.command.mod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc.Main;
import zav.discord.blanc.Shard;
import zav.discord.blanc.TextChannel;
import zav.discord.blanc.Webhook;
import zav.discord.blanc.reddit.WebhookSubredditListener;
import zav.jra.Subreddit;

import java.io.IOException;
import java.util.Collection;

/**
 * This command allows to link subreddits to channels.
 */
public class RedditCommand extends RedditCommandTOP {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedditCommand.class);
    private static final String WEBHOOK_NAME = "Reddit";

    private TextChannel channel;
    private Collection<Webhook> webhooks;
    private Subreddit subreddit;

    @Override
    public void run() throws IOException {
        channel = getTextChannel().orElse(get$TextChannel());

        try {
            webhooks = channel.retrieveWebhooks(WEBHOOK_NAME);
            subreddit = Main.REDDIT_CLIENT.getSubreddit(getSubreddit());

            switch (getAction()) {
                case ADD:
                    addSubreddit();
                    break;
                case REMOVE:
                    removeSubreddit();
                    break;
            }
        } catch(InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private ACTION getAction (){
        for (Webhook webhook : webhooks) {
            if(webhook.containsSubreddits(getSubreddit()))
                return ACTION.REMOVE;
        }
        return ACTION.ADD;
    }

    private void removeSubreddit() throws IOException {
        for(Webhook webhook : webhooks){
            if(webhook.removeSubreddits(getSubreddit())){
                //Listener are unique with respect to the guild & webhook id.
                WebhookSubredditListener listener = new WebhookSubredditListener(get$Guild(), webhook);
                Main.REDDIT_OBSERVABLE.get(subreddit).removeListener(listener);

                Shard.write(get$Guild(), webhook);
            }
        }
        get$TextChannel().send("Submissions from r/%s will no longer be posted in %s.", subreddit.getDisplayName(), channel.getAsMention());
    }

    private void addSubreddit() throws IOException {
        //Cancel the command if the bot can't post them in the targeted channel
        if(!get$Guild().canInteract(get$Guild().retrieveSelfMember(), channel)){
            get$TextChannel().send("I can't interact with "+channel.getName());
            return;
        }

        //Has to be a supplier, otherwise a new webhook will always be created
        Webhook webhook = webhooks.stream().findAny().orElseGet(() -> channel.createWebhook(WEBHOOK_NAME));

        //Update the persistence file
        webhook.addSubreddits(getSubreddit());
        Shard.write(get$Guild(), webhook);

        //Register the new Reddit feed
        Main.REDDIT_OBSERVABLE.get(subreddit).addListener(new WebhookSubredditListener(get$Guild(), webhook));

        get$TextChannel().send("Submissions from r/%s will be posted in %s.", subreddit.getDisplayName(), channel.getAsMention());
    }

    private enum ACTION {
        ADD,
        REMOVE
    }
}
