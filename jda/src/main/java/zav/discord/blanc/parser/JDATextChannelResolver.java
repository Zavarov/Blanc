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
import zav.discord.blanc.Shard;
import zav.discord.blanc.TextChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class JDATextChannelResolver extends AbstractJDAGuildResolver<net.dv8tion.jda.api.entities.TextChannel, TextChannel> {
    public JDATextChannelResolver(@Nonnull Shard shard, @Nonnull JDA jda) {
        super(shard, jda);
    }

    @Nonnull
    @Override
    protected Collection<net.dv8tion.jda.api.entities.TextChannel> resolveByName(String name) {
        if(guild == null) return Collections.emptyList();

        return guild.getTextChannelsByName(name, true);
    }

    @Nullable
    @Override
    protected net.dv8tion.jda.api.entities.TextChannel resolveByNumber(Number number) {
        if(guild == null) return null;

        return guild.getTextChannelById(number.longValue());
    }

    @Nonnull
    @Override
    protected Optional<TextChannel> map(Guild guild, TextChannel textChannel, net.dv8tion.jda.api.entities.TextChannel snowflake) {
        return guild.retrieveTextChannel(snowflake.getIdLong());
    }
}
