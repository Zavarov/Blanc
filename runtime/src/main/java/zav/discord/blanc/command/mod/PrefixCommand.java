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
package zav.discord.blanc.command.mod;

import zav.discord.blanc.Shard;

import java.io.IOException;
import java.util.Optional;

/**
 * This command allows to set a custom prefix for a server.
 */
public class PrefixCommand extends PrefixCommandTOP{
    @Override
    public void run() throws IOException {
        if(prefix.isEmpty()){
            get$Guild().setPrefix(Optional.empty());
            get$TextChannel().send("Removed the custom prefix.");
        }else{
            get$Guild().setPrefix(getPrefix());
            get$TextChannel().send("Set the custom prefix to '"+getPrefix()+"'.");
        }
        Shard.write(get$Guild());
    }
}
