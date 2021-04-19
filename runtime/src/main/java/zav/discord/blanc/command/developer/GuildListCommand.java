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

package zav.discord.blanc.command.developer;

/**
 * This command returns a list of all guilds this bot is in.
 */
public class GuildListCommand extends GuildListCommandTOP {
    @Override
    public void run(){
        //TODO
        throw new UnsupportedOperationException();
        /*
        cluster.accept(new ShardVisitor());
        guilds.sort(Comparator.naturalOrder());

        InteractiveMessageBuilder builder = new InteractiveMessageBuilder(author, shard);
        builder.addLines(guilds, 10);
        shard.queue(channel, builder.build());
         */
    }
}
