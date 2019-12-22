package vartas.discord.bot.entities.offline;

import vartas.reddit.Client;
import vartas.reddit.Submission;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class OfflineClient implements Client {
    @Override
    public void logout(){}

    @Override
    public Optional<? extends Collection<Submission>> requestSubmission(String subreddit, LocalDateTime start, LocalDateTime end, int retries){
        return Optional.of(Collections.emptyList());
    }
}
