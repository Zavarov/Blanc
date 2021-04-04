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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import zav.discord.blanc.Guild;
import zav.discord.blanc.Shard;
import zav.discord.blanc.TextChannel;

import javax.annotation.Nonnull;

public class GuildMessageListener extends ListenerAdapter {
    @Nonnull
    private final Shard shard;

    public GuildMessageListener(@Nonnull Shard shard){
        this.shard = shard;
    }
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event){
        Guild guild = shard.retrieveGuild(event.getGuild().getIdLong()).orElseThrow();
        TextChannel textChannel = guild.retrieveTextChannel(event.getChannel().getIdLong()).orElseThrow();

        guild.getActivity().countMessage(textChannel);
    }
}
