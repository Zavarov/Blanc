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

package zav.discord.blanc.parser;

import net.dv8tion.jda.api.JDA;
import zav.discord.blanc.Guild;
import zav.discord.blanc.Message;
import zav.discord.blanc.Shard;
import zav.discord.blanc.TextChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class JDAMessageResolver extends AbstractJDAGuildResolver<net.dv8tion.jda.api.entities.Message, Message> {
    public JDAMessageResolver(@Nonnull Shard shard, @Nonnull JDA jda){
        super(shard, jda);
    }

    @Nonnull
    @Override
    protected Collection<net.dv8tion.jda.api.entities.Message> resolveByName(String name) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    protected net.dv8tion.jda.api.entities.Message resolveByNumber(Number number) {
        if(textChannel == null) return null;

        return textChannel.retrieveMessageById(number.longValue()).complete();
    }

    @Nonnull
    @Override
    protected Optional<Message> map(Guild guild, TextChannel textChannel, net.dv8tion.jda.api.entities.Message snowflake) {
        return textChannel.retrieveMessage(snowflake.getIdLong());
    }
}
