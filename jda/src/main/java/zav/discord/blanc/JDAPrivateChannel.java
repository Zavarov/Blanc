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
import zav.discord.blanc._factory.PrivateChannelFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class JDAPrivateChannel extends PrivateChannel{
    private static final Logger log = LoggerFactory.getLogger(JDATextChannel.class.getSimpleName());

    public static PrivateChannel create(net.dv8tion.jda.api.entities.PrivateChannel privateChannel){
        return PrivateChannelFactory.create(
                () -> new JDAPrivateChannel(privateChannel),
                privateChannel.getIdLong(),
                privateChannel.getName()
        );
    }

    @Nonnull
    private final net.dv8tion.jda.api.entities.PrivateChannel privateChannel;

    @Nonnull
    private JDAPrivateChannel(@Nonnull net.dv8tion.jda.api.entities.PrivateChannel privateChannel){
        this.privateChannel = privateChannel;
    }

    @Override
    public Optional<Message> retrieveMessage(long id) {
        try {
            return Optional.of(JDAMessage.create(privateChannel.retrieveMessageById(id).complete()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Collection<Message> retrieveMessages() {
        try {
            return privateChannel.getHistory().getRetrievedHistory().stream().map(JDAMessage::create).collect(Collectors.toList());
        }catch(Exception e){
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void send(Message message) {
        try {
            privateChannel.sendMessage(MessageBuilder.buildMessage(message)).complete();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Override
    public void send(byte[] bytes, String qualifiedName) {
        try {
            privateChannel.sendFile(bytes, qualifiedName).complete();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
