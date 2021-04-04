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
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc._factory.TextChannelFactory;
import zav.discord.blanc._json.JSONTextChannel;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class JDATextChannel extends TextChannel{
    private static final Cache<Long, TextChannel> TEXT_CHANNELS = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(1)).build();

    private static final Logger log = LoggerFactory.getLogger(JDATextChannel.class.getSimpleName());

    public static TextChannel create(net.dv8tion.jda.api.entities.TextChannel jdaTextChannel){
        TextChannel textChannel = TEXT_CHANNELS.getIfPresent(jdaTextChannel.getIdLong());

        //Text channel cached?
        if(textChannel != null)
            return textChannel;

        textChannel = TextChannelFactory.create(
                () -> new JDATextChannel(jdaTextChannel),
                jdaTextChannel.getIdLong(),
                jdaTextChannel.getName()
        );

        try{
            Guild guild = JDAGuild.create(jdaTextChannel.getGuild());
            JSONTextChannel.fromJson(textChannel, guild, jdaTextChannel.getIdLong());
            log.info("Successfully loaded the JSON file for the text channel {}.", jdaTextChannel.getName());
        }catch(IOException e){
            log.warn("Failed loading the JSON file for the text channel {} : {}", jdaTextChannel.getName(), e.toString());
        }finally {
            TEXT_CHANNELS.put(jdaTextChannel.getIdLong(), textChannel);
        }

        return textChannel;
    }

    @Nonnull
    private final net.dv8tion.jda.api.entities.TextChannel textChannel;

    @Nonnull
    private JDATextChannel(@Nonnull net.dv8tion.jda.api.entities.TextChannel textChannel){
        this.textChannel = textChannel;
    }

    @Override
    public Optional<Message> retrieveMessage(long id) {
        return Optional.of(JDAMessage.create(textChannel.retrieveMessageById(id).complete()));
    }

    @Override
    public Collection<Message> retrieveMessages() {
        try {
            return textChannel.getHistory().getRetrievedHistory().stream().map(JDAMessage::create).collect(Collectors.toList());
        }catch(PermissionException e){
            return Collections.emptyList();
        }
    }

    @Override
    public void send(Message message) {
        textChannel.sendMessage(MessageBuilder.buildMessage(message)).complete();
    }

    @Override
    public void send(byte[] bytes, String qualifiedName) {
        textChannel.sendFile(bytes, qualifiedName).complete();
    }

    @Override
    public Webhook createWebhook(String name) {
        return JDAWebhook.create(textChannel.createWebhook(name).complete());
    }

    @Override
    public Collection<Webhook> retrieveWebhooks(String name) {
        return retrieveWebhooks().stream().filter(webhook -> webhook.getName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public Collection<Webhook> retrieveWebhooks() {
        try {
            return textChannel.retrieveWebhooks().complete().stream().map(JDAWebhook::create).collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public String getAsMention(){
        return textChannel.getAsMention();
    }
}
