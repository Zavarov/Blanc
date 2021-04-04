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

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc.MessageBuilder;
import zav.discord.blanc.MessageChannel;
import zav.discord.blanc.Shard;
import zav.discord.blanc.command.Command;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractCommandListener extends ListenerAdapter {
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Nonnull
    protected final Shard shard;

    protected AbstractCommandListener(@Nonnull Shard shard){
        this.shard = shard;
    }

    protected void submit(@Nonnull MessageChannel messageChannel, @Nonnull Supplier<Optional<? extends Command>> commandSupplier){
        shard.submit(() -> {
            try {
                Optional<? extends Command> commandOpt = commandSupplier.get();
                commandOpt.ifPresent(command -> {
                        command.validate();
                        command.run();
                });
            }catch(Exception e){
                e.printStackTrace();
                log.error(e.toString(), e);

                try {
                    messageChannel.send(MessageBuilder.buildMessage(e));
                } catch (IOException ioe){
                    log.error(ioe.getMessage(), ioe);
                }
            }
        });
    }
}
