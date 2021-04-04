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
import zav.discord.blanc._factory.SelfMemberFactory;
import zav.discord.blanc.io._json.JSONRanks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class JDASelfMember extends SelfMember{
    @Nonnull
    private final net.dv8tion.jda.api.entities.Member member;

    public JDASelfMember(@Nonnull net.dv8tion.jda.api.entities.Member member){
        this.member = member;
    }

    @Nonnull
    public static SelfMember create(net.dv8tion.jda.api.entities.Member member){
        SelfMember selfMember = SelfMemberFactory.create(
                () -> new JDASelfMember(member),
                JDAOnlineStatus.transform(member.getOnlineStatus()),
                member.getIdLong(),
                member.getUser().getName()
        );
        selfMember.setRanks(JSONRanks.RANKS.getRanks().get(member.getIdLong()));
        return selfMember;
    }

    @Nonnull
    @Override
    public Set<Permission> getPermissions(@Nonnull TextChannel textChannel) {
        net.dv8tion.jda.api.entities.TextChannel jdaTextChannel = member.getGuild().getTextChannelById(textChannel.getId());

        if(jdaTextChannel == null)
            return Collections.emptySet();
        else
            return member.getPermissions(jdaTextChannel).stream().map(JDAPermission::transform).collect(Collectors.toSet());
    }

    @Override
    public void modifyNickname(@Nullable String nickname){
        member.modifyNickname(nickname).complete();
    }

    @Override
    public List<Role> retrieveRoles(){
        return member.getRoles().stream().map(JDARole::create).collect(Collectors.toList());
    }

    @Override
    public void modifyRoles(Collection<Role> rolesToAdd, Collection<Role> rolesToRemove) {
        List<net.dv8tion.jda.api.entities.Role> jdaRolesToAdd = rolesToAdd.stream()
                .map(Snowflake::getId)
                .map(member.getGuild()::getRoleById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<net.dv8tion.jda.api.entities.Role> jdaRolesToRemove = rolesToRemove.stream()
                .map(Snowflake::getId)
                .map(member.getGuild()::getRoleById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        member.getGuild().modifyMemberRoles(member, jdaRolesToAdd, jdaRolesToRemove).complete();
    }

    @Override
    public void modifyStatusMessage(String statusMessage) {
        ShardManager shardManager = member.getJDA().getShardManager();
        if(shardManager != null)
            shardManager.setActivityProvider(i -> Activity.playing(statusMessage));
    }

    @Override
    public void modifyAvatar(@Nonnull InputStream avatar) {
        try {
            member.getJDA().getSelfUser().getManager().setAvatar(Icon.from(avatar)).complete();
        } catch(IOException e){
            LoggerFactory.getLogger(this.getClass().getSimpleName()).error(e.toString());
        }
    }

    @Override
    public Optional<String> retrieveNickname(){
        return Optional.ofNullable(member.getNickname());
    }

    @Override
    public PrivateChannel retrievePrivateChannel() {
        return JDAPrivateChannel.create(member.getUser().openPrivateChannel().complete());
    }

    @Override
    public String getAsMention(){
        return member.getAsMention();
    }
}
