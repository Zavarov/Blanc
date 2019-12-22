/*
 * Copyright (c) 2019 Zavarov
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

package vartas.discord.bot.listener;

import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import vartas.discord.bot.entities.Configuration;
import vartas.discord.bot.entities.Shard;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * This listener applies the blacklist pattern on all received guild messages.
 * If a message matches the pattern, the program will attempt to remove it automatically.
 * An exception to this are messages sent by this program, which will be ignored.
 */
@Nonnull
public class BlacklistListener extends ListenerAdapter {
    /**
     * The shard associated with the listener.
     * It is required for scheduling the removal.
     */
    @Nonnull
    private final Shard shard;

    /**
     * Initializes a fresh listener.
     * @param shard the shard associated with the listener
     * @throws NullPointerException if {@code shard} is null
     */
    public BlacklistListener(@Nonnull Shard shard) throws NullPointerException{
        Preconditions.checkNotNull(shard);
        this.shard = shard;
    }

    /**
     * Checks if the message contains any blacklisted words.
     * If the message was sent from the author, nothing happens. Otherwise the pattern from the configuration
     * is retrieved, to assure that it isn't outdated, and compared to the message content.
     * On a match, the message is attempted to be deleted.
     * @param event the corresponding event.
     */
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) throws NullPointerException{
        Preconditions.checkNotNull(event);
        Message message = event.getMessage();
        SelfUser self = event.getJDA().getSelfUser();
        User author = event.getAuthor();
        Guild guild = event.getGuild();
        long guildId = guild.getIdLong();
        //Ignore everything this bot posts
        if(self.equals(author))
            return;

        Configuration configuration = shard.guild(event.getGuild());
        Optional<Pattern> patternOpt = configuration.getPattern();
        //Delete the message on a match
        patternOpt.ifPresent(pattern -> {
            if(pattern.matcher(message.getContentRaw()).matches())
                shard.queue(message.delete());
        });
    }

    public void accept(Visitor visitor){
        visitor.handle(this);
    }

    public interface Visitor {
        default void visit(@Nonnull BlacklistListener blacklistListener){}

        default void traverse(@Nonnull BlacklistListener blacklistListener) {}

        default void endVisit(@Nonnull BlacklistListener blacklistListener){}

        default void handle(@Nonnull BlacklistListener blacklistListener) throws NullPointerException{
            Preconditions.checkNotNull(blacklistListener);
            visit(blacklistListener);
            traverse(blacklistListener);
            endVisit(blacklistListener);
        }
    }
}
