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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc._factory.MessageEmbedFactory;
import zav.discord.blanc._factory.MessageFactory;
import zav.jra.models.AbstractLink;
import zav.jra.models.AbstractSubreddit;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;

@Nonnull
public abstract class MessageChannel extends MessageChannelTOP{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageChannel.class);

    /**
     * Wraps the {@link AbstractLink} around a {@link Message} and submits them to the Discord server.
     * @param link the content of the submitted {@link Message}
     */
    @Override
    public void send(@Nonnull AbstractSubreddit subreddit, @Nonnull AbstractLink link) throws IOException {
        MessageEmbed messageEmbed = MessageEmbedFactory.create();

        messageEmbed.setTitle(AbstractLink.getQualifiedTitle(link), AbstractLink.getPermalink(link));
        messageEmbed.setAuthor("source", link.getUrl());
        messageEmbed.setTimestamp(link.getCreatedUtc().toInstant());

        if(link.getOver18()) {
            messageEmbed.setColor(Color.RED);
        }else if(link.getSpoiler()) {
            messageEmbed.setColor(Color.BLACK);
        }else {
            messageEmbed.setColor(new Color(link.getAuthor().hashCode()));
            link.ifPresentThumbnail(messageEmbed::setThumbnail);
            link.ifPresentSelftext(messageEmbed::setContent);
        }

        Message message = MessageFactory.create(0, Instant.now(), null);
        message.addMessageEmbeds(messageEmbed);

        String author = link.getAuthor().orElse(null);
        if(author == null)
            message.setContent(String.format("New submission in `r/%s`:\n\n<%s>", subreddit.getDisplayName(), AbstractLink.getShortLink(link)));
        else
            message.setContent(String.format("New submission from %s in `r/%s`:\n\n<%s>", author, subreddit.getDisplayName(), AbstractLink.getShortLink(link)));

        LOGGER.info("Send link with id {}.", link.getId());

        send(message);
    }

    /**
     * Wraps the {@link MessageEmbed} around a {@link Message} and submits them to the Discord server.
     * @param messageEmbed the content of the submitted {@link Message}
     */
    @Override
    public void send(@Nonnull MessageEmbed messageEmbed) throws IOException {
        Message message = MessageFactory.create(0, Instant.now(), null);

        message.addMessageEmbeds(messageEmbed);

        send(message);
    }

    @Override
    public void send(@Nonnull Guild guild) throws IOException {
        send(guild.toMessageEmbed());
    }

    @Override
    public void send(@Nonnull Role role) throws IOException {
        send(role.toMessageEmbed());
    }

    @Override
    public void send(@Nonnull Member member) throws IOException {
        send(member.toMessageEmbed());
    }

    @Override
    public void send(@Nonnull User user) throws IOException {
        send(user.toMessageEmbed());
    }

    @Override
    public void send(@Nonnull Object object) throws IOException {
        Message message = MessageFactory.create(0, Instant.now(), null);

        message.setContent(object.toString());

        send(message);
    }

    @Override
    public void send(@Nonnull BufferedImage image, @Nonnull String title) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", outputStream);
            byte[] bytes = outputStream.toByteArray();
            send(bytes, title+".png");
        }catch(IOException e){
            //ByteArrayOutputStream shouldn't be able to trigger an IO exception
            throw new RuntimeException(e);
        }
    }

    public void send(@Nonnull String format, @Nonnull Object... arguments) throws IOException {
        send(String.format(format, arguments));
    }
}
