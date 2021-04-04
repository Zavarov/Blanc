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
import zav.discord.blanc.Shard;
import zav.discord.blanc.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

@Nonnull
public class JDAUserResolver extends AbstractJDAResolver<net.dv8tion.jda.api.entities.User, User> {
    public JDAUserResolver(@Nonnull Shard shard, @Nonnull JDA jda){
        super(shard, jda);
    }

    @Nonnull
    @Override
    protected Collection<net.dv8tion.jda.api.entities.User> resolveByName(String name) {
        return jda.getUsersByName(name, true);
    }

    @Nullable
    @Override
    protected net.dv8tion.jda.api.entities.User resolveByNumber(Number number) {
        net.dv8tion.jda.api.entities.User user;
        return (user = jda.getUserById(number.longValue())) != null ? user : jda.retrieveUserById(number.longValue()).complete();
    }

    @Nonnull
    @Override
    protected Optional<User> map(net.dv8tion.jda.api.entities.User snowflake) {
        return shard.retrieveUser(snowflake.getIdLong());
    }
}
