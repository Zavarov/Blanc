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

package zav.discord.blanc.reddit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc.Guild;
import zav.discord.blanc.Shard;
import zav.discord.blanc.TextChannel;
import zav.discord.blanc.Webhook;
import zav.discord.blanc._visitor.ArchitectureVisitor;
import zav.jra.Client;
import zav.jra.Subreddit;
import zav.jra.exceptions.ForbiddenException;
import zav.jra.exceptions.NotFoundException;
import zav.jra.models.AbstractSubreddit;
import zav.jra.models.Submission;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * This visitor traverses through all guilds and their corresponding text channels. If a Reddit feed is assigned to one
 * of the text channels, the latest submissions of the corresponding {@link AbstractSubreddit} are fetched and then
 * posted in the channel itself. The visitor has to be called periodically, in order to retrieve new submissions in
 * real-time.
 * <p>
 * Internally, an {@link Instant} is used to keep track of which submissions have already been posted. It contains the
 * last time the visitor has been executed, which means that every {@link Submission}, that is newer than this time step
 * is new and has not been posted before.
 */
@Nonnull
public class RedditVisitor implements ArchitectureVisitor {
    /**
     * This class's {@link Logger}, logging the individual phases of the request.
     */
    @Nonnull
    private static final Logger LOGGER = LoggerFactory.getLogger(RedditVisitor.class);

    private final Map<String, Subreddit> cache = new HashMap<>();
    private final RedditObservable observable;
    private final Client client;

    public RedditVisitor(RedditObservable observable, Client client){
        this.observable = observable;
        this.client = client;
    }

    @Override
    public void visit(@Nonnull Shard shard){
        LOGGER.trace("Visiting shard {}.", shard.getId());
    }


    @Override
    public void handle(@Nonnull Guild guild){
        LOGGER.trace("Visiting guild {}", guild.getName());
        for(TextChannel textChannel : guild.retrieveTextChannels()) {
            LOGGER.trace("Visiting text channel {}", textChannel.getName());
            for (String subredditName : textChannel.getSubreddits()) {
                createTextChannelListener(subredditName, guild, textChannel);
            }
            for(Webhook webhook : textChannel.retrieveWebhooks()) {
                LOGGER.trace("Visiting webhook {}", webhook.getName());
                for (String subredditName : webhook.getSubreddits()) {
                    createWebhookListener(subredditName, guild, webhook);
                }
            }
        }
    }

    private void createTextChannelListener(String subredditName, Guild guild, TextChannel channel){
        RedditListener listener = new TextChannelSubredditListener(guild, channel);
        Subreddit subreddit = loadSubreddit(subredditName);

        if (subreddit != null) {
            LOGGER.info("Register listener for webhook {} in guild {}.", channel.getName(), guild.getName());
            observable.get(subreddit).addListener(listener);
        }
    }

    private void createWebhookListener(String subredditName, Guild guild, Webhook webhook){
        RedditListener listener = new WebhookSubredditListener(guild, webhook);
        Subreddit subreddit = loadSubreddit(subredditName);


        if (subreddit != null) {
            LOGGER.info("Register listener for webhook {} in guild {}.", webhook.getName(), guild.getName());
            observable.get(subreddit).addListener(listener);
        }
    }

    @Nullable
    private Subreddit loadSubreddit(String subredditName) throws RuntimeException {
        Subreddit subreddit = cache.getOrDefault(subredditName, null);

        if (subreddit != null) {
            return subreddit;
        } else {
            try{
                subreddit = client.getSubreddit(subredditName);
                cache.put(subredditName, subreddit);
                return subreddit;
            } catch(NotFoundException | ForbiddenException e) {
                LOGGER.error(e.getMessage(), e);
                return null;
            } catch(IOException | InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
}
