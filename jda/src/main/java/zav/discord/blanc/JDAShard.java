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

package zav.discord.blanc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class JDAShard extends Shard {
    private final JDA jda;

    public JDAShard (@Nonnull JDA jda) {
        this.jda = jda;
    }

    @Override
    public void shutdown(){
        jda.shutdownNow();
        super.shutdown();
    }

    @Override
    public SelfUser retrieveSelfUser() {
        return JDASelfUser.create(jda.getSelfUser());
    }

    @Override
    public Optional<User> retrieveUser(long id) {
        try{
            return Optional.of(JDAUser.create(jda.retrieveUserById(id).complete()));
        }catch(ErrorResponseException e){
            return Optional.empty();
        }
    }

    @Override
    public Collection<User> retrieveUsers() {
        return jda.getUserCache().stream().map(JDAUser::create).collect(Collectors.toList());
    }

    @Override
    public Optional<Guild> retrieveGuild(long id) {
        return Optional.ofNullable(jda.getGuildById(id)).map(JDAGuild::create);
    }

    @Override
    public Collection<Guild> retrieveGuilds() {
        return jda.getGuildCache().stream().map(JDAGuild::create).collect(Collectors.toList());
    }
}
