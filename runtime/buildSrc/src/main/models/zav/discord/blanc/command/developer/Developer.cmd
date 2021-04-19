package zav.discord.blanc.command.developer;

import zav.discord.blanc.Architecture.Guild;
import zav.discord.blanc.Architecture.Message;
import zav.discord.blanc.Architecture.User;

group dev {
    command avatar requires Attachment{
             class : AvatarCommand
              rank : Developer
    }
    command delete requires Guild{
             class : DeleteCommand
         parameter : Message message
              rank : Developer
    }
    command reaction requires Guild{
             class : ReactionCommand
         parameter : Message message, String reaction
              rank : Developer
    }
    command reddit{
             class : RedditRankCommand
         parameter : User user
              rank : Developer
    }
    command failsafe{
             class : FailsafeCommand
              rank : Developer
    }
    command guild{
             class : GuildListCommand
              rank : Developer
    }
    command kill{
             class : KillCommand
              rank : Developer
    }
    command leave{
             class : LeaveCommand
         parameter : Guild guild
              rank : Developer
    }
    command nickname requires Guild{
             class : NicknameCommand
         parameter : String nickname?
              rank : Developer
    }
    command say{
             class : SayCommand
         parameter : String content
              rank : Developer
    }
    command status{
             class : StatusCommand
              rank : Developer
    }
    command user{
             class : UserCommand
         parameter : User user
              rank : Developer
    }
}
