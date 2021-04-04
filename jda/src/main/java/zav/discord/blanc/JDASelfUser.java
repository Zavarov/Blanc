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

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.LoggerFactory;
import zav.discord.blanc._factory.SelfUserFactory;
import zav.discord.blanc.io._json.JSONRanks;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

public class JDASelfUser extends SelfUser{
    @Nonnull
    private final net.dv8tion.jda.api.entities.SelfUser selfUser;

    public JDASelfUser(@Nonnull net.dv8tion.jda.api.entities.SelfUser selfUser){
        this.selfUser = selfUser;
    }

    @Nonnull
    public static SelfUser create(net.dv8tion.jda.api.entities.SelfUser selfUser){
        SelfUser jdaSelfUser = SelfUserFactory.create(
                () -> new JDASelfUser(selfUser),
                OnlineStatus.UNKNOWN,
                selfUser.getIdLong(),
                selfUser.getName()
        );

        jdaSelfUser.setRanks(JSONRanks.RANKS.getRanks().get(selfUser.getIdLong()));

        return jdaSelfUser;
    }

    @Override
    public void modifyStatusMessage(String statusMessage) {
        ShardManager shardManager = selfUser.getJDA().getShardManager();
        if(shardManager != null)
            shardManager.setActivityProvider(i -> Activity.playing(statusMessage));
    }

    @Override
    public void modifyAvatar(@Nonnull InputStream avatar) {
        try {
            selfUser.getManager().setAvatar(Icon.from(avatar)).complete();
        } catch(IOException e){
            LoggerFactory.getLogger(this.getClass().getSimpleName()).error(e.toString());
        }
    }

    @Override
    public PrivateChannel retrievePrivateChannel() {
        return JDAPrivateChannel.create(selfUser.openPrivateChannel().complete());
    }

    @Override
    public String getAsMention(){
        return selfUser.getAsMention();
    }
}
