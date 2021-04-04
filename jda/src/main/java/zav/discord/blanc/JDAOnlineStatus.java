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

public class JDAOnlineStatus {
    private static final Map<net.dv8tion.jda.api.OnlineStatus, OnlineStatus> ONLINE_STATUS = new HashMap<>();

    static {
        ONLINE_STATUS.put(net.dv8tion.jda.api.OnlineStatus.ONLINE, OnlineStatus.ONLINE);
        ONLINE_STATUS.put(net.dv8tion.jda.api.OnlineStatus.IDLE, OnlineStatus.IDLE);
        ONLINE_STATUS.put(net.dv8tion.jda.api.OnlineStatus.DO_NOT_DISTURB, OnlineStatus.BUSY);
        ONLINE_STATUS.put(net.dv8tion.jda.api.OnlineStatus.OFFLINE, OnlineStatus.OFFLINE);
    }

    public static OnlineStatus transform(net.dv8tion.jda.api.OnlineStatus jdaOnlineStatus){
        return ONLINE_STATUS.getOrDefault(jdaOnlineStatus, OnlineStatus.UNKNOWN);
    }
}
