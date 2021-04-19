package zav.discord.blanc.command.reddit;

import java.time.temporal.ChronoUnit.ChronoUnit;
import java.time.LocalDate.LocalDate;

group reddit {
    command user{
             class : AccountCommand
         parameter : String subreddit, LocalDate from, LocalDate to, String account
              rank : REDDIT
    }
    command table{
             class : SnowflakeTableCommand
         parameter : String subreddit, LocalDate from, LocalDate to, String type
              rank : REDDIT
    }
    command markdown{
             class : MarkdownTableCommand
         parameter : String subreddit, LocalDate from, LocalDate to, String type
              rank : REDDIT
    }
    command submission{
             class : SubmissionCommand
         parameter : String subreddit, LocalDate from, LocalDate to, String type
              rank : REDDIT
    }
    command subreddit{
             class : SubredditCommand
         parameter : String subreddit, LocalDate from, LocalDate to, ChronoUnit granularity
              rank : Reddit
    }
}
