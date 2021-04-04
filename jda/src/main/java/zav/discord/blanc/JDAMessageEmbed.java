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

import zav.discord.blanc._factory.AuthorFactory;
import zav.discord.blanc._factory.MessageEmbedFactory;
import zav.discord.blanc._factory.TitleFactory;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class JDAMessageEmbed extends MessageEmbed{
    @Nonnull
    public static MessageEmbed create(net.dv8tion.jda.api.entities.MessageEmbed messageEmbed){
        return MessageEmbedFactory.create(
                JDAMessageEmbed::new,
                Optional.ofNullable(messageEmbed.getColor()),
                Optional.ofNullable(messageEmbed.getThumbnail()).map(net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail::getUrl),
                Optional.ofNullable(messageEmbed.getTitle()).map(TitleFactory::create),
                Optional.ofNullable(messageEmbed.getDescription()),
                Optional.ofNullable(messageEmbed.getTimestamp()).map(OffsetDateTime::toInstant),
                Optional.ofNullable(messageEmbed.getAuthor()).map(net.dv8tion.jda.api.entities.MessageEmbed.AuthorInfo::getName).map(AuthorFactory::create),
                messageEmbed.getFields().stream().map(JDAField::create).collect(Collectors.toList())
        );
    }
}
