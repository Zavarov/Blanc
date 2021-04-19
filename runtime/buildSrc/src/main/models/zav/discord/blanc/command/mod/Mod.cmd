package zav.discord.blanc.command.mod;

import zav.discord.blanc.Architecture.TextChannel;
import zav.discord.blanc.Architecture.Role;

group mod {
    command config requires Guild{
             class : ConfigurationCommand
         parameter : String module
        permission : ADMINISTRATOR, MANAGE_MESSAGES, MANAGE_CHANNELS, MANAGE_ROLES
    }
    command blacklist requires Guild{
             class : BlacklistCommand
         parameter : String expression
        permission : MANAGE_MESSAGES
    }
    command prefix requires Guild{
             class : PrefixCommand
         parameter : String prefix
        permission : ADMINISTRATOR
    }
    command reddit requires Guild{
             class : RedditCommand
         parameter : String subreddit, TextChannel textChannel?
        permission : MANAGE_CHANNELS
    }
    command role requires Guild{
             class : SelfAssignableRoleCommand
         parameter : String group, Role role
        permission : MANAGE_ROLES
    }
}
