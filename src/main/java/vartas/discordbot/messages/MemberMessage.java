/*
 * Copyright (C) 2018 u/Zavarov
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
package vartas.discordbot.messages;

import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.atteo.evo.inflector.English;
import vartas.discordbot.comm.Communicator;
import vartas.discordbot.messages.InteractiveMessage.Builder;

/**
 * This class creates a Discord message displaying the information of a Discord
 * user.
 * @author u/Zavarov
 */
public final class MemberMessage {
    /**
     * Never create instances of this class.
     */
    private MemberMessage(){}
    /**
     * The formatter for the dates.
     */
    protected static final DateTimeFormatter DATE = DateTimeFormatter.RFC_1123_DATE_TIME;
    /**
     * Sets the description of the current page of the message.
     * @param builder the message builder.
     * @param desc the description.
     */
    private static void addDescription(Builder builder, String desc){
        builder.addDescription(desc);
    }
    /**
     * Adds the full name of the member to the message.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addName(EmbedBuilder builder, Member member){
        builder.addField("Full Name",String.format("%s#%s",
                member.getUser().getName(),
                member.getUser().getDiscriminator()),false
        );
    }
    /**
     * Adds the id of the user account to the message.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addId(EmbedBuilder builder, Member member){
        builder.addField("ID",member.getUser().getId(),false);
    }
    /**
     * Adds the nickname of the member to the message, if he has one.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addNickname(EmbedBuilder builder, Member member){
        if(member.getNickname() != null){
            builder.addField("Nickname",member.getNickname(),false);
        }
        
    }
    /**
     * Adds the date when the account was created to the message.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addCreated(EmbedBuilder builder, Member member){
        int days = (int)DAYS.between(member.getUser().getCreationTime().toLocalDate(),LocalDate.now());
        builder.addField("Created",String.format("%s (%d %s ago)",
                DATE.format(member.getUser().getCreationTime()),
                days,
                English.plural("day", days)),false
        );
        
    }
    /**
     * Adds the date of when the user joined to the message.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addJoined(EmbedBuilder builder, Member member){
        int days = (int)DAYS.between(member.getJoinDate().toLocalDate(),LocalDate.now());
        builder.addField("Joined",String.format("%s (%d %s ago)",
                DATE.format(member.getJoinDate()),
                days,
                English.plural("day", days)),false
        );
    }
    /**
     * Adds the current color of the member name, if it is not the default one.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addColor(EmbedBuilder builder, Member member){
        Color c = member.getColor();
        if(c != null){
            builder.addField("Color",String.format("0x%02X%02X%02X",
                c.getRed(),
                c.getGreen(),
                c.getBlue()),false
            );
        }
    }
    /**
     * Adds the current game status to the message, if the user has a status message.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addGame(EmbedBuilder builder, Member member){
        if(member.getGame() != null){
            String type = member.getGame().getType().toString().toLowerCase(Locale.ENGLISH);
            //Transform the game type into an user friendly string
            if(member.getGame().getType()== Game.GameType.DEFAULT){
                type = "Playing";
            }else{
                type = type.substring(0,1).toUpperCase() + type.substring(1);
            }
            builder.addField(type,member.getGame().getName(),false);
        }
    }
    /**
     * Adds the total number of roles of the member to the message.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addRoleCount(EmbedBuilder builder, Member member){
        builder.addField("#Roles",Integer.toString(member.getRoles().size()),false);
    }
    /**
     * Adds all roles of the member to the message.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addRoles(Builder builder, Member member){
        member.getRoles()
                .stream()
                .map(role -> String.format("`%s` - %s", role.getId(), role.getName()))
                .forEach(builder::addLine);
    }
    /**
     * Adds the effective permissions of the member in the specific channel to the message.
     * @param builder the message builder.
     * @param member the member in question.
     * @param channel the channel the member is in.
     */
    private static void addPermissions(Builder builder, Member member, TextChannel channel){
        List<Permission> permissions = Permission.getPermissions(
                PermissionUtil.getEffectivePermission(
                        channel,
                        member)
        );
        Collections.sort(permissions,(o1,o2)->o1.name().compareTo(o2.name()));
        permissions.stream()
                .map(p -> p.getName())
                .forEach(builder::addLine);
        
    }
    /**
     * Adds the avatar of the user to the page, if it isn't the default one.
     * @param builder the message builder.
     * @param member the member in question.
     */
    private static void addThumbnail(Builder builder, Member member){
        String avatar = member.getUser().getAvatarUrl();
        if(avatar != null)
            builder.setThumbnail(avatar);
    }
    /**
     * Shows the information about the specified member. If the parameter are
     * empty, the user who executed the command is used.
     * @param author the user who caused this message.
     * @param member the member in question.
     * @param channel the channel the member is in.
     * @param comm the communicator in the shard the message is in.
     * @return an interactive message displaying the members information
     */
    public static InteractiveMessage create(User author, Member member, TextChannel channel, Communicator comm){
        InteractiveMessage.Builder builder = new InteractiveMessage.Builder(channel, author, comm);
        addThumbnail(builder,member);
        
        EmbedBuilder embed = new EmbedBuilder();
        embed.addField("Description", String.format("The basic information about %s", member.getAsMention()), false);
        addName(embed,member);
        addId(embed,member);
        addNickname(embed,member);
        addCreated(embed,member);
        addJoined(embed,member);
        addColor(embed,member);
        addGame(embed,member);
        addRoleCount(embed,member);
        builder.addPage(embed);
        
        addDescription(builder, "All assigned roles");
        addRoles(builder,member);
        builder.nextPage();
        
        addDescription(builder, String.format("All of %s permissions in %s",member.getAsMention(), channel.getAsMention()));
        addPermissions(builder, member, channel);
        return builder.build();
    }
}