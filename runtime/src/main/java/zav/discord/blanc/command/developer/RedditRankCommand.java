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

import org.json.JSONObject;
import zav.discord.blanc.Rank;
import zav.discord.blanc.Shard;
import zav.discord.blanc.io._json.JSONRanks;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * This command assigns and removes the Reddit rank.
 */
public class RedditRankCommand extends RedditRankCommandTOP{
    @Override
    public void run() throws IOException {
        if(get$Author().containsRanks(Rank.REDDIT)){
            get$Author().removeRanks(Rank.REDDIT);

            JSONRanks.RANKS.getRanks().remove(get$Author().getId(), Rank.REDDIT);

            get$MessageChannel().send(String.format("Removed Reddit rank from %s.", getUser().getName()));
        }else{
            get$Author().addRanks(Rank.REDDIT);

            JSONRanks.RANKS.getRanks().put(get$Author().getId(), Rank.REDDIT);

            get$MessageChannel().send(String.format("Granted Reddit rank to %s.", getUser().getName()));
        }

        Shard.write(JSONRanks.toJson(JSONRanks.RANKS, new JSONObject()), Paths.get("ranks.json"));
    }
}