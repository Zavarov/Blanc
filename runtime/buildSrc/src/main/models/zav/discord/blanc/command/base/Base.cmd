package zav.discord.blanc.command.base;

import java.math.BigDecimal.BigDecimal;
import java.util.List.List;

{
    command assign requires Guild{
             class : SelfAssignableRoleCommand
         parameter : Role role
    }
    command activity requires Guild{
             class : ActivityCommand
         parameter : TextChannel channels*
    }
    command guild requires Guild{
             class : GuildInfoCommand
    }
    command help{
             class : HelpCommand
    }
    command invite{
             class : InviteCommand
    }
    command math{
             class : MathCommand
         parameter : BigDecimal value
    }
    command member requires Guild{
             class : MemberInfoCommand
         parameter : Member member?
    }
    command ping{
             class : PingCommand
    }
    command role requires Guild{
             class : RoleInfoCommand
         parameter : Role role
    }
    command support{
             class : SupportCommand
    }
}
