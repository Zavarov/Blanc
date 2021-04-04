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

import java.util.HashMap;
import java.util.Map;

public class JDAPermission {
    private static final Map<net.dv8tion.jda.api.Permission, Permission> PERMISSIONS = new HashMap<>();

    static {
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.CREATE_INSTANT_INVITE, Permission.CREATE_INSTANT_INVITE);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.BAN_MEMBERS, Permission.BAN_MEMBERS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.ADMINISTRATOR, Permission.ADMINISTRATOR);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MANAGE_CHANNEL, Permission.MANAGE_CHANNELS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MANAGE_SERVER, Permission.MANAGE_GUILD);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_ADD_REACTION, Permission.ADD_REACTIONS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.VIEW_AUDIT_LOGS, Permission.VIEW_AUDIT_LOG);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_READ, Permission.VIEW_CHANNEL);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_WRITE, Permission.SEND_MESSAGES);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_TTS, Permission.SEND_TTS_MESSAGES);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_MANAGE, Permission.MANAGE_MESSAGES);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_EMBED_LINKS, Permission.EMBED_LINKS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_ATTACH_FILES, Permission.ATTACH_FILES);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_HISTORY, Permission.READ_MESSAGE_HISTORY);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_MENTION_EVERYONE, Permission.MENTION_EVERYONE);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MESSAGE_EXT_EMOJI, Permission.USE_EXTERNAL_EMOJIS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.UNKNOWN, Permission.VIEW_GUILD_INSIGHTS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.VOICE_CONNECT, Permission.CONNECT);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.VOICE_SPEAK, Permission.SPEAK);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.VOICE_MUTE_OTHERS, Permission.MUTE_MEMBERS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.VOICE_DEAF_OTHERS, Permission.DEAFEN_MEMBERS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.VOICE_MOVE_OTHERS, Permission.MOVE_MEMBERS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.VOICE_USE_VAD, Permission.USE_VAD);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.PRIORITY_SPEAKER, Permission.PRIORITY_SPEAKER);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.VOICE_STREAM, Permission.STREAM);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.NICKNAME_CHANGE, Permission.CHANGE_NICKNAME);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.NICKNAME_MANAGE, Permission.MANAGE_NICKNAMES);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MANAGE_ROLES, Permission.MANAGE_ROLES);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MANAGE_WEBHOOKS, Permission.MANAGE_WEBHOOKS);
        PERMISSIONS.put(net.dv8tion.jda.api.Permission.MANAGE_EMOTES, Permission.MANAGE_EMOJIS);
    }

    public static Permission transform(net.dv8tion.jda.api.Permission jdaPermission){
        return PERMISSIONS.get(jdaPermission);
    }
}
