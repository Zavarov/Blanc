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

package zav.discord.blanc.activity;

import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.OnlineStatus;
import zav.discord.blanc._visitor.ArchitectureVisitor;
import zav.discord.blanc.Guild;
import zav.discord.blanc.TextChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Nonnull
public class JDAActivity extends Activity{
    @Nonnull
    private final net.dv8tion.jda.api.entities.Guild jdaGuild;

    public JDAActivity(@Nonnull net.dv8tion.jda.api.entities.Guild jdaGuild){
        this.jdaGuild = jdaGuild;
    }

    @Override
    public void update(@Nonnull Guild guild){
        putActivity(LocalDateTime.now(), new GuildVisitor().gather(guild));
        //Clear all messages from the current interval, so that they're excluded from future cycles.
        messages.clear();
    }

    @Nonnull
    private class GuildVisitor implements ArchitectureVisitor{
        @Nullable
        GuildActivity data;

        @Nonnull
        public GuildActivity gather(@Nonnull Guild guild){
            data = new GuildActivity();
            guild.accept(this);
            return data;
        }

        @Override
        public void visit(@Nonnull TextChannel channel){
            Preconditions.checkNotNull(data);
            data.putChannelActivity(channel, messages.getOrDefault(channel, 0L) / (double)ACTIVITY_RATE.toMinutes());
        }

        @Override
        public void endVisit(@Nonnull Guild guild){
            Preconditions.checkNotNull(data);
            data.setMembersOnline(jdaGuild.getMembers().stream().filter(member -> member.getOnlineStatus() == OnlineStatus.ONLINE).count());
            data.setMembersCount(jdaGuild.getMemberCount());
            data.setActivity(data.valuesChannelActivity().stream().mapToDouble(x -> x).sum());
        }

    }
}
