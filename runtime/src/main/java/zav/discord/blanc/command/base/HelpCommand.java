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

package zav.discord.blanc.command.base;

import java.io.IOException;

/**
 * This command submits the list with all supported commands.
 */
public class HelpCommand extends HelpCommandTOP{
    @Override
    public void run() throws IOException {
        StringBuilder stringBuilder = new StringBuilder()
                .append("For a list of all commands and their function, please visit:\n")
                .append("https://github.com/Zavarov/blanc/wiki");
        get$MessageChannel().send(stringBuilder);
    }
}