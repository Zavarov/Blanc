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
 * This command allows to modify the nickname of the bot in a specified guild.
 */
public class NicknameCommand extends NicknameCommandTOP{
    /**
     * If the new nickname is empty, the old one will be removed. Otherwise it is overwritten.
     */
    @Override
    public void run(){
        get$Guild().retrieveSelfMember().modifyNickname(getNickname().orElse(""));
    }
}
