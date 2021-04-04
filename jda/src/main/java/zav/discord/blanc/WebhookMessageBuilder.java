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

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Optional;

public final class WebhookMessageBuilder {
    private WebhookMessageBuilder(){}
    @Nonnull
    public static WebhookMessage buildMessage(@Nonnull User author, @Nonnull Message message){
        club.minnced.discord.webhook.send.WebhookMessageBuilder messageBuilder = new club.minnced.discord.webhook.send.WebhookMessageBuilder();

        //Set author
        messageBuilder.setUsername(author.getName());
        messageBuilder.setAvatarUrl(author.getAvatarUrl());

        //Set content
        message.ifPresentContent(messageBuilder::setContent);
        //TODO I'm pretty sure messages allow multiple embeds
        buildMessageEmbed(message).ifPresent(messageBuilder::addEmbeds);

        return messageBuilder.build();
    }

    @Nonnull
    public static Optional<WebhookEmbed> buildMessageEmbed(@Nonnull Message message){
        return message.streamMessageEmbeds().map(messageEmbed -> {
            WebhookEmbedBuilder embedBuilder = new WebhookEmbedBuilder();

            messageEmbed.getColor().map(Color::getRGB).ifPresent(embedBuilder::setColor);
            messageEmbed.ifPresentTimestamp(embedBuilder::setTimestamp);
            messageEmbed.ifPresentThumbnail(embedBuilder::setThumbnailUrl);

            messageEmbed.getContent()
                    .map(content -> StringUtils.abbreviate(content, net.dv8tion.jda.api.entities.MessageEmbed.TEXT_MAX_LENGTH))
                    .ifPresent(embedBuilder::setDescription);

            messageEmbed.ifPresentAuthor(author ->
                    embedBuilder.setAuthor(
                            new WebhookEmbed.EmbedAuthor(
                                StringUtils.abbreviate(author.getName(), net.dv8tion.jda.api.entities.MessageEmbed.TITLE_MAX_LENGTH),
                                null,
                                author.getUrl().orElse(null)
                            )
                    )
            );

            messageEmbed.ifPresentTitle(title ->
                    embedBuilder.setTitle(
                            new WebhookEmbed.EmbedTitle(
                                    StringUtils.abbreviate(title.getName(), MessageEmbed.TITLE_MAX_LENGTH),
                                    title.getUrl().orElse(null)
                            )
                    )
            );

            messageEmbed.getFields().forEach(field ->
                    embedBuilder.addField(
                            new WebhookEmbed.EmbedField(
                                    field.getInline(),
                                    field.getTitle(),
                                    field.getContent().toString()
                            )
                    )
            );

            return embedBuilder.build();
        }).findFirst();
    }
}
