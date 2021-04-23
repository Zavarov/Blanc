package zav.discord.blanc.reddit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zav.discord.blanc.exceptions.InsufficientPermissionException;
import zav.discord.blanc.io._json.JSONCredentials;
import zav.discord.blanc.mock.LinkMock;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class RedditObservableTest extends AbstractRedditTest{
    RedditObservable observable;
    RedditObserver observer;
    RedditListener listener;
    RedditListener dummy;

    Path targetDirectory = Paths.get("target", "test", "resources");
    Path jsonDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        jsonDirectory = JSONCredentials.CREDENTIALS.getJsonDirectory();
        JSONCredentials.CREDENTIALS.setJsonDirectory(targetDirectory);

        observable = new RedditObservable();
        observer = new RedditObserver(redditdev);
        listener = new TextChannelSubredditListener(guild, textChannel);
        dummy = new TextChannelSubredditListener(guild, textChannel);

        observer.addListener(listener);
        observer.addListener(dummy);
        observable.addObserver(observer);
        observable.notifyAllObservers(); //Initialize heads for all requester
    }

    @AfterEach
    public void tearDown(){
        JSONCredentials.CREDENTIALS.setJsonDirectory(jsonDirectory);
    }

    @Test
    public void testGet(){
        assertThat(observable.get(redditdev) == observer).isTrue();
    }

    @Test
    public void testGetAndAdd(){
        assertThat(observable.size()).isEqualTo(1);
        observable.get(modnews);
        assertThat(observable.size()).isEqualTo(2);
    }

    @Test
    public void testAddObserver(){
        assertThat(observable.addObserver(observer)).isFalse();
    }

    @Test
    public void testRemoveObserver(){
        assertThat(observable.removeObserver(observer)).isTrue();
    }

    @Test
    public void testNotifyObserver() throws IOException {
        observable.notifyObserver(observer);

        assertThat(observable.size()).isEqualTo(1);
        assertThat(textChannel.containsSubreddits("redditdev")).isTrue();
    }

    @Test
    public void testNotifyObserverNoMoreObserver() throws IOException {
        textChannel.sendLinkException = new InsufficientPermissionException();
        redditdev.links.add(0, new LinkMock(2)); //Create a link to post

        observable.notifyObserver(observer);
        assertThat(observable.size()).isEqualTo(0);
    }
}
