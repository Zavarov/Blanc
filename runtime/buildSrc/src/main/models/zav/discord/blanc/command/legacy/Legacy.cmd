package zav.discord.blanc.command.legacy;

import zav.discord.blanc.Architecture.TextChannel;
import zav.discord.blanc.Architecture.Role;

group legacy {
    command reddit requires Guild{
             class : RedditCommand
         parameter : String subreddit, TextChannel textChannel?
        permission : MANAGE_CHANNELS, MANAGE_WEBHOOKS
    }
}
