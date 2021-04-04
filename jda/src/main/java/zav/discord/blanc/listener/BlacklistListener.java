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

package zav.discord.blanc.listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import zav.discord.blanc._visitor.ArchitectureVisitor;
import zav.discord.blanc.*;
import zav.discord.blanc.JDAMessage;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlacklistListener extends AbstractCommandListener {
    public BlacklistListener(@Nonnull Shard shard){
        super(shard);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event){
        //Exclude the bot itself from the blacklist
        //Required in order to display all blacklisted words, for example.
        if(isSelfUser(event.getAuthor()))
            return;
        //Only proceed when the bot has the required permissions for deleting messages
        if(!hasRequiredPermissions(event.getGuild(), event.getChannel()))
            return;

        Guild guild = shard.retrieveGuild(event.getGuild().getIdLong()).orElseThrow();
        //Only proceed if a pattern has been declared for this guild
        guild.getPattern().ifPresent(pattern -> {
            Message message = JDAMessage.create(event.getMessage());
            if(MessageChecker.checkMessage(pattern, message))
                message.delete();
        });
    }

    private boolean isSelfUser(net.dv8tion.jda.api.entities.User author){
        return author.getIdLong() == shard.retrieveSelfUser().getId();
    }

    private boolean hasRequiredPermissions(net.dv8tion.jda.api.entities.Guild guild, net.dv8tion.jda.api.entities.TextChannel channel){
        return PermissionUtil.checkPermission(channel, guild.getSelfMember(), Permission.MESSAGE_MANAGE);
    }

    private static class MessageChecker implements ArchitectureVisitor {
        @Nonnull
        private final Pattern pattern;
        private boolean shouldDelete = false;

        private MessageChecker(@Nonnull Pattern pattern){
            this.pattern = pattern;
        }

        public static boolean checkMessage(@Nonnull Pattern pattern, @Nonnull Message message){
            MessageChecker checker = new MessageChecker(pattern);
            message.accept(checker);
            return checker.shouldDelete;
        }

        @Override
        public void visit(Field field){
            shouldDelete |= pattern.matcher(field.getTitle()).find();
            shouldDelete |= pattern.matcher(field.getContent().toString()).find();
        }

        @Override
        public void visit(Author author){
            shouldDelete |= pattern.matcher(author.getName()).find();
        }

        @Override
        public void visit(Title title){
            shouldDelete |= pattern.matcher(title.getName()).find();
        }

        @Override
        public void visit(Message message){
            shouldDelete |= message.getContent().map(pattern::matcher).map(Matcher::find).orElse(false);
        }
    }
}
