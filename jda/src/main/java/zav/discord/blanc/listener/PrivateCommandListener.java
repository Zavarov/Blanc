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

package zav.discord.blanc.listener;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import zav.discord.blanc.JDAMessage;
import zav.discord.blanc.Message;
import zav.discord.blanc.PrivateChannel;
import zav.discord.blanc.Shard;
import zav.discord.blanc.command.CommandBuilder;

import javax.annotation.Nonnull;

public class PrivateCommandListener extends AbstractCommandListener {
    @Nonnull
    private final CommandBuilder commandBuilder;

    public PrivateCommandListener(@Nonnull CommandBuilder commandBuilder, @Nonnull Shard shard){
        super(shard);
        this.commandBuilder = commandBuilder;
    }

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event){
        if(event.getAuthor().isBot())
            return;

        Message message = JDAMessage.create(event.getMessage());
        PrivateChannel channel = message.getAuthor().retrievePrivateChannel();

        submit(channel, () -> commandBuilder.build(message, channel));
    }
}
