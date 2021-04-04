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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import net.dv8tion.jda.api.OnlineStatus;
import org.atteo.evo.inflector.English;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc._factory.GuildFactory;
import zav.discord.blanc._json.JSONGuild;
import zav.discord.blanc.activity.JDAActivity;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Nonnull
public class JDAGuild extends Guild{
    private static final Cache<Long, Guild> GUILDS = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(1)).build();

    private static final Logger log = LoggerFactory.getLogger(JDAGuild.class.getSimpleName());
    /**
     * A moderator is every user that has as least one of the listed permissions.
     */
    protected static final Set<net.dv8tion.jda.api.Permission> MODERATOR_PERMISSIONS = Sets.newHashSet(
            net.dv8tion.jda.api.Permission.BAN_MEMBERS,
            net.dv8tion.jda.api.Permission.KICK_MEMBERS,
            net.dv8tion.jda.api.Permission.MANAGE_CHANNEL,
            net.dv8tion.jda.api.Permission.MANAGE_PERMISSIONS,
            net.dv8tion.jda.api.Permission.MANAGE_ROLES,
            net.dv8tion.jda.api.Permission.MANAGE_SERVER
    );
    /**
     * An administrator is every user that has all of the listed permissions.
     */
    protected static final Set<net.dv8tion.jda.api.Permission> ADMINISTRATOR_PERMISSIONS = Sets.newHashSet(
            net.dv8tion.jda.api.Permission.ADMINISTRATOR
    );
    /**
     * The date pretty printer.
     */
    protected static final DateTimeFormatter DATE = DateTimeFormatter.RFC_1123_DATE_TIME;

    @Nonnull
    public static Guild create(@Nonnull net.dv8tion.jda.api.entities.Guild jdaGuild){
        Guild guild = GUILDS.getIfPresent(jdaGuild.getIdLong());

        //Guild is still cached?
        if(guild != null)
            return guild;

        //Load a new guild instance into cache
        guild = GuildFactory.create(
                () -> new JDAGuild(jdaGuild),
                new JDAActivity(jdaGuild),
                jdaGuild.getIdLong(),
                jdaGuild.getName()
        );

        //Load additional parameter from the corresponding JSON file.
        try{
            JSONGuild.fromJson(guild, jdaGuild.getIdLong());
            log.info("Successfully loaded the JSON file for the guild {}.", jdaGuild.getName());
        }catch(IOException e){
            log.warn("Failed loading the JSON file for the guild {} : {}", jdaGuild.getName(), e.toString());
        }finally{
            //Update cache
            GUILDS.put(jdaGuild.getIdLong(), guild);
        }

        return guild;
    }

    @Nonnull
    private final net.dv8tion.jda.api.entities.Guild guild;

    private JDAGuild(@Nonnull net.dv8tion.jda.api.entities.Guild guild){
        this.guild = guild;
    }

    @Override
    public Optional<Member> retrieveMember(long id) {
        return Optional.ofNullable(guild.getMemberById(id)).map(JDAMember::create);
    }

    @Override
    public Collection<Member> retrieveMembers() {
        return guild.getMemberCache().stream().map(JDAMember::create).collect(Collectors.toList());
    }

    @Override
    public Optional<TextChannel> retrieveTextChannel(long id) {
        return Optional.ofNullable(guild.getTextChannelById(id)).map(JDATextChannel::create);
    }

    @Override
    public Collection<TextChannel> retrieveTextChannels() {
        return guild.getTextChannelCache().stream().map(JDATextChannel::create).collect(Collectors.toList());
    }

    @Override
    public Optional<Role> retrieveRole(long id) {
        return Optional.ofNullable(guild.getRoleById(id)).map(JDARole::create);
    }

    @Override
    public Collection<Role> retrieveRoles() {
        return guild.getRoleCache().stream().map(JDARole::create).collect(Collectors.toList());
    }

    @Override
    public SelfMember retrieveSelfMember() {
        return JDASelfMember.create(guild.getSelfMember());
    }

    @Override
    public void leave(){
        guild.leave().complete();
    }

    @Override
    public boolean canInteract(@Nonnull Member member, @Nonnull TextChannel textChannel){
        net.dv8tion.jda.api.entities.Member jdaMember = guild.getMemberById(member.getId());
        net.dv8tion.jda.api.entities.TextChannel jdaTextChannel = guild.getTextChannelById(textChannel.getId());

        if(jdaMember == null || jdaTextChannel == null)
            return false;
        else
            return jdaTextChannel.canTalk(jdaMember);
    }

    @Override
    public boolean canInteract(@Nonnull Member member, @Nonnull Role role){
        net.dv8tion.jda.api.entities.Member jdaMember = guild.getMemberById(member.getId());
        net.dv8tion.jda.api.entities.Role jdaRole = guild.getRoleById(role.getId());

        if(jdaMember == null || jdaRole == null)
            return false;
        else
            return jdaMember.canInteract(jdaRole);
    }

    //------------------------------------------------------------------------------------------------------------------
    //
    //      Printable
    //
    //------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public MessageEmbed toMessageEmbed(){
        MessageEmbed messageEmbed = new MessageEmbed();

        setTitle(messageEmbed);
        setThumbnail(messageEmbed);
        addOwner(messageEmbed);
        addRegion(messageEmbed);
        addTextChannels(messageEmbed);
        addVoiceChannels(messageEmbed);
        addAdministrators(messageEmbed);
        addModerators(messageEmbed);
        addMembers(messageEmbed);
        addRoles(messageEmbed);
        addCreated(messageEmbed);

        return messageEmbed;
    }

    private void setTitle(MessageEmbed messageEmbed){
        messageEmbed.setTitle(guild.getName());
    }

    private void setThumbnail(MessageEmbed messageEmbed){
        //Icon may be null if the guild uses the default icon
        if(guild.getIconUrl() != null)
            messageEmbed.setThumbnail(guild.getIconUrl());
    }

    private void addOwner(MessageEmbed messageEmbed){
        //Owner may be null if the account was suspended
        if(guild.getOwner() != null)
            messageEmbed.addFields("Owner",guild.getOwner().getUser().getAsMention(), true);
    }

    private void addRegion(MessageEmbed messageEmbed){
        messageEmbed.addFields("Region", guild.getRegion().getName(), true);
    }

    private void addTextChannels(MessageEmbed messageEmbed){
        int size = guild.getTextChannels().size();
        messageEmbed.addFields(English.plural("#TextChannel",size), size, true);
    }

    private void addVoiceChannels(MessageEmbed messageEmbed){
        int size = guild.getVoiceChannels().size();
        messageEmbed.addFields(English.plural("#VoiceChannel",size), size, true);
    }

    private void addAdministrators(MessageEmbed messageEmbed){
        //The owner is always an administrator, however, that account may be suspended
        List<net.dv8tion.jda.api.entities.Member> administrators = guild.getMembers()
                .stream()
                .filter(m -> !m.getUser().isBot())
                .filter(m -> m.getPermissions().containsAll(ADMINISTRATOR_PERMISSIONS))
                .sorted(Comparator.comparingLong(u -> u.getUser().getIdLong()))
                .collect(Collectors.toList());

        administrators.stream()
                .map(net.dv8tion.jda.api.entities.Member::getAsMention)
                .reduce((u, v) -> u + "\n" + v)
                .ifPresent(content -> messageEmbed.addFields(English.plural("Admin", administrators.size()), content));

    }

    private void addModerators(MessageEmbed messageEmbed){
        List<net.dv8tion.jda.api.entities.Member> moderators = guild.getMembers().stream()
                .filter(m -> !m.getUser().isBot())
                .filter(m -> !m.getPermissions().containsAll(ADMINISTRATOR_PERMISSIONS))
                .filter(m -> !Collections.disjoint(m.getPermissions(), MODERATOR_PERMISSIONS))
                .collect(Collectors.toList());

        moderators.stream()
                .map(net.dv8tion.jda.api.entities.Member::getAsMention)
                .reduce((u, v) -> u + "\n" + v)
                .ifPresent(content -> messageEmbed.addFields(English.plural("Moderator", moderators.size()), content));
    }

    private void addMembers(MessageEmbed messageEmbed){
        int online = (int)guild.getMembers().stream()
                .filter(e -> !e.getOnlineStatus().equals(OnlineStatus.OFFLINE))
                .count();

        int total = guild.getMembers().size();

        messageEmbed.addFields(English.plural("#Member",total), String.format("%d / %d",online,total), true);

    }

    private void addRoles(MessageEmbed messageEmbed){
        int size = guild.getRoles().size();
        messageEmbed.addFields(English.plural("#Role",size), size, true);
    }

    private void addCreated(MessageEmbed messageEmbed){
        messageEmbed.addFields("Created", DATE.format(guild.getTimeCreated()), true);
    }
}
