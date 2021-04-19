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
import java.util.Random;

/**
 * This command allows developers to become super-user and therefore allows them to bypass any permission checks.
 */
public class FailsafeCommand extends FailsafeCommandTOP {
    /**
     * A generator to pick a random quote when executing this command.
     */
    private static final Random RANDOM = new Random();
    /**
     * A list of quotes when becoming a super user.
     */
    private static final String[] BECOME_ROOT = {
        "Taking control over this form.",
        "Assuming direct control.",
        "Assuming control.",
        "We are assuming control.",
        "You cannot resist.",
        "I will direct this personally.",
        "Direct intervention is necessary.",
        "Relinquish your form to us.",
        "Your minions have failed, %s.",
        "Your cannot stop us, %s.",
        "Submit now!",
        "This is true power.",
        "Progress cannot be halted.",
        "Assuming control of this form."
    };
    /**
     * A list of quotes when becoming a developer again.
     */
    private static final String[] BECOME_DEVELOPER = {
        "Releasing control.",
        "Releasing this form.",
        "We are not finished.",
        "Releasing control of this form.",
        "Destroying this body gains you nothing.",
        "This changes nothing, %s.",
        "You have only delayed the inevitable.",
        "You are no longer relevant.",
        "Your interference has ended.",
        "This delay is pointless.",
        "We will find another way.",
        "I am releasing this form.",
        "%s, you could have been useful.",
        "You will regret your resistance, %s."
    };

    /**
     * This command makes super-user into developers and developers into super-user.
     */
    @Override
    public void run() throws IOException {
        String message;

        if(get$Author().containsRanks(Rank.DEVELOPER)){
            get$Author().removeRanks(Rank.DEVELOPER);
            get$Author().addRanks(Rank.ROOT);

            JSONRanks.RANKS.getRanks().remove(get$Author().getId(), Rank.DEVELOPER);
            JSONRanks.RANKS.getRanks().put(get$Author().getId(), Rank.ROOT);

            message = String.format(BECOME_ROOT[RANDOM.nextInt(BECOME_ROOT.length)], get$Author().getName());
        }else{
            get$Author().removeRanks(Rank.ROOT);
            get$Author().addRanks(Rank.DEVELOPER);

            JSONRanks.RANKS.getRanks().remove(get$Author().getId(), Rank.ROOT);
            JSONRanks.RANKS.getRanks().put(get$Author().getId(), Rank.DEVELOPER);

            message = String.format(BECOME_DEVELOPER[RANDOM.nextInt(BECOME_DEVELOPER.length)], get$Author().getName());
        }

        get$MessageChannel().send(message);
        Shard.write(JSONRanks.toJson(JSONRanks.RANKS, new JSONObject()), Paths.get("ranks.json"));
    }
}
