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

import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;
import java.util.Optional;

public final class MessageBuilder {
    private MessageBuilder(){}

    @Nonnull
    public static Message buildMessage(@Nonnull Exception exception){
        Message message = new Message();

        message.addMessageEmbeds(buildMessageEmbed(exception));

        return message;
    }

    @Nonnull
    public static MessageEmbed buildMessageEmbed(@Nonnull Exception exception){
        MessageEmbed messageEmbed = new MessageEmbed();

        messageEmbed.setColor(Color.RED);
        messageEmbed.setTimestamp(Instant.now());
        messageEmbed.addFields(exception.getClass().getSimpleName(), exception.getMessage());

        return messageEmbed;
    }

    @Nonnull
    public static net.dv8tion.jda.api.entities.Message buildMessage(@Nonnull Message message){
        net.dv8tion.jda.api.MessageBuilder messageBuilder = new net.dv8tion.jda.api.MessageBuilder(message.getContent().orElse(""));

        //TODO I'm pretty sure messages allow multiple embeds
        buildMessageEmbed(message).ifPresent(messageBuilder::setEmbed);

        return messageBuilder.build();
    }

    @Nonnull
    public static Optional<net.dv8tion.jda.api.entities.MessageEmbed> buildMessageEmbed(@Nonnull Message message){
        return message.streamMessageEmbeds().map(messageEmbed -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            messageEmbed.ifPresentColor(embedBuilder::setColor);
            messageEmbed.ifPresentTimestamp(embedBuilder::setTimestamp);
            messageEmbed.ifPresentThumbnail(embedBuilder::setThumbnail);

            messageEmbed.getContent()
                    .map(content -> StringUtils.abbreviate(content, net.dv8tion.jda.api.entities.MessageEmbed.TEXT_MAX_LENGTH))
                    .ifPresent(embedBuilder::setDescription);
            messageEmbed.ifPresentAuthor(author ->
                    embedBuilder.setAuthor(StringUtils.abbreviate(author.getName(), net.dv8tion.jda.api.entities.MessageEmbed.TITLE_MAX_LENGTH), author.getUrl().orElse(null)));
            messageEmbed.ifPresentTitle(title ->
                    embedBuilder.setTitle(StringUtils.abbreviate(title.getName(), net.dv8tion.jda.api.entities.MessageEmbed.TITLE_MAX_LENGTH), title.getUrl().orElse(null)));
            messageEmbed.getFields().forEach(field ->
                    embedBuilder.addField(field.getTitle(), field.getContent().toString(), field.getInline())
            );

            return embedBuilder.build();
        }).findFirst();
    }
}
