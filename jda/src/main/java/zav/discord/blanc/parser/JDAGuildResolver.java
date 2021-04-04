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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

@Nonnull
public class JDAGuildResolver extends AbstractJDAResolver<net.dv8tion.jda.api.entities.Guild, Guild> {
    public JDAGuildResolver(@Nonnull Shard shard, @Nonnull JDA jda){
        super(shard, jda);
    }

    @Nonnull
    @Override
    protected Collection<net.dv8tion.jda.api.entities.Guild> resolveByName(String name) {
        return jda.getGuildsByName(name, true);
    }

    @Nullable
    @Override
    protected net.dv8tion.jda.api.entities.Guild resolveByNumber(Number number) {
        return jda.getGuildById(number.longValue());
    }

    @Nonnull
    @Override
    protected Optional<Guild> map(net.dv8tion.jda.api.entities.Guild snowflake) {
        return shard.retrieveGuild(snowflake.getIdLong());
    }
}
