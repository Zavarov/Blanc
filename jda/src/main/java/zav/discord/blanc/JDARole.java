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
import org.atteo.evo.inflector.English;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc._factory.MessageEmbedFactory;
import zav.discord.blanc._factory.RoleFactory;
import zav.discord.blanc._json.JSONRole;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

public class JDARole extends Role{
    private static final Cache<Long, Role> ROLES = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(1)).build();

    private static final Logger log = LoggerFactory.getLogger(JDAGuild.class.getSimpleName());
    /**
     * The date pretty printer.
     */
    protected static final DateTimeFormatter DATE = DateTimeFormatter.RFC_1123_DATE_TIME;

    @Nonnull
    public static Role create(@Nonnull net.dv8tion.jda.api.entities.Role jdaRole){
        Role role = ROLES.getIfPresent(jdaRole.getIdLong());

        //Role is cached?
        if(role != null)
            return role;

        role = RoleFactory.create(
                () -> new JDARole(jdaRole),
                jdaRole.getIdLong(),
                jdaRole.getName()
        );

        try{
            Guild guild = JDAGuild.create(jdaRole.getGuild());
            JSONRole.fromJson(role, guild, jdaRole.getIdLong());
            log.info("Successfully loaded the JSON file for the role {}.", jdaRole.getName());
        }catch(IOException e){
            log.warn("Failed loading the JSON file for the role {} : {}", jdaRole.getName(), e.toString());
        }finally{
            ROLES.put(jdaRole.getIdLong(), role);
        }

        return role;
    }
    @Nonnull
    private final net.dv8tion.jda.api.entities.Role role;

    @Nonnull
    private JDARole(@Nonnull net.dv8tion.jda.api.entities.Role role){
        this.role = role;
    }

    @Override
    public String getAsMention(){
        return role.getAsMention();
    }

    //------------------------------------------------------------------------------------------------------------------
    //
    //      Printable
    //
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public MessageEmbed toMessageEmbed(){
        MessageEmbed messageEmbed = MessageEmbedFactory.create();

        setTitle(messageEmbed);
        addId(messageEmbed);
        addCreated(messageEmbed);
        addPosition(messageEmbed);
        addMembersCount(messageEmbed);
        addColor(messageEmbed);

        return messageEmbed;
    }

    private void setTitle(MessageEmbed messageEmbed){
        messageEmbed.setTitle(role.getName());
    }

    private void addId(MessageEmbed messageEmbed){
        messageEmbed.addFields("ID", role.getId());
    }

    private void addCreated(MessageEmbed messageEmbed){
        OffsetDateTime created = role.getTimeCreated();
        String date = DATE.format(created);
        int days = (int)DAYS.between(created.atZoneSameInstant(ZoneOffset.UTC).toLocalDate(), LocalDate.now(ZoneOffset.UTC));

        messageEmbed.addFields("Created", String.format("%s (%d %s ago)", date, days, English.plural("day", days)));
    }

    private void addPosition(MessageEmbed messageEmbed){
        messageEmbed.addFields("Position", role.getPosition());
    }

    private void addMembersCount(MessageEmbed messageEmbed){
        messageEmbed.addFields("#Members", role.getGuild().getMembersWithRoles(role).size());
    }

    private void addColor(MessageEmbed messageEmbed){
        Color color = role.getColor();

        if(color != null){
            messageEmbed.addFields("Color", String.format("0x%02X%02X%02X",
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue())
            );
        }
    }
}
