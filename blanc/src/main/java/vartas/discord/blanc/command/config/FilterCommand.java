/*
 * Copyright (C) 2018 u/Zavarov
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
package vartas.discord.blanc.command.config;

import net.dv8tion.jda.core.entities.Message;
import vartas.discord.bot.api.communicator.CommunicatorInterface;
import vartas.discord.bot.api.message.InteractiveMessage;
import vartas.discord.bot.command.entity._ast.ASTEntityType;

import java.util.List;

/**
 * This command lists all word that are blacklisted in the server.
 */
public class FilterCommand extends FilterCommandTOP{
    public FilterCommand(Message source, CommunicatorInterface communicator, List<ASTEntityType> parameters) throws IllegalArgumentException, IllegalStateException {
        super(source, communicator, parameters);
    }

    /**
     * Lists all blacklisted words, if they exists.
     */
    @Override
    public void run(){
        if(config.getFilter().isEmpty()){
            communicator.send(channel, "This guild doesn't filter any words.");
            return;
        }
        
        InteractiveMessage interactive = new InteractiveMessage.Builder(channel, author, communicator)
            .addDescription("Forbidden words:")
            .addLines(config.getFilter(), 10)
            .build();
        communicator.send(interactive);
    }
}