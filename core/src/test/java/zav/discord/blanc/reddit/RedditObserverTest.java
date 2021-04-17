package zav.discord.blanc.reddit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zav.discord.blanc.exceptions.InsufficientPermissionException;
import zav.discord.blanc.io._json.JSONCredentials;
import zav.discord.blanc.mock.TextChannelMock;
import zav.jra.exceptions.NotFoundException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RedditObserverTest extends AbstractRedditTest{
    RedditObserver observer;
    RedditListener listener;
    RedditListener dummy;

    Path targetDirectory = Paths.get("target", "test", "resources");
    Path jsonDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        jsonDirectory = JSONCredentials.CREDENTIALS.getJsonDirectory();
        JSONCredentials.CREDENTIALS.setJsonDirectory(targetDirectory);

        observer = new RedditObserver(redditdev);
        listener = new TextChannelSubredditListener(guild, textChannel);
        dummy = new TextChannelSubredditListener(guild, new TextChannelMock());

        observer.addListener(listener);
        observer.addListener(dummy);
        observer.notifyAllListener(); //Initialize head of the requester


    }

    @AfterEach
    public void tearDown(){
        JSONCredentials.CREDENTIALS.setJsonDirectory(jsonDirectory);
    }

    @Test
    public void testNotifyListener() throws IOException {
        observer.notifyListener(listener);

        assertThat(observer.size()).isEqualTo(2);
        assertThat(textChannel.containsSubreddits("redditdev")).isTrue();
    }

    @Test
    public void testNotifyListenerDiscordError() throws IOException {
        textChannel.sendLinkException = new InsufficientPermissionException();
        observer.notifyListener(listener);

        assertThat(observer.size()).isEqualTo(1);
        assertThat(textChannel.isEmptySubreddits()).isTrue();
    }

    @Test
    public void testNotifyListenerRedditError() throws IOException {
        redditdev.getNewLinksException = new NotFoundException();
        observer.notifyListener(listener);

        assertThat(observer.size()).isEqualTo(1);
        assertThat(textChannel.isEmptySubreddits()).isTrue();
    }

    @Test
    public void testNotifyListenerNoMoreListener(){
        textChannel.sendLinkException = new InsufficientPermissionException();
        observer.removeListener(dummy);

        assertThatThrownBy(() -> observer.notifyAllListener()).isInstanceOf(InvalidObserverException.class);
    }
}
