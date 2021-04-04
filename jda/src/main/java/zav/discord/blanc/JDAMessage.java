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

import zav.discord.blanc._factory.MessageFactory;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Collectors;

public class JDAMessage extends Message {
    @Nonnull
    public static Message create(net.dv8tion.jda.api.entities.Message message){
        return MessageFactory.create(
            () -> new JDAMessage(message),
            message.getIdLong(),
            message.getTimeCreated().toInstant(),
            JDAUser.create(message.getAuthor()),
            message.getContentRaw().isEmpty() ? Optional.empty() : Optional.of(message.getContentRaw()),
            message.getEmbeds().stream().map(JDAMessageEmbed::create).collect(Collectors.toList()),
            message.getAttachments().stream().map(JDAAttachment::create).collect(Collectors.toList())
        );
    }

    private final net.dv8tion.jda.api.entities.Message jdaMessage;

    private JDAMessage(net.dv8tion.jda.api.entities.Message jdaMessage){
        this.jdaMessage = jdaMessage;
    }

    @Override
    public void delete(){
        jdaMessage.delete().complete();
    }

    @Override
    public void react(String emote){
        jdaMessage.addReaction(emote).complete();
    }
}
