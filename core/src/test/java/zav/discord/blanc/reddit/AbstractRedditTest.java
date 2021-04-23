package zav.discord.blanc.reddit;

import org.junit.jupiter.api.BeforeEach;
import zav.discord.blanc.AbstractTest;
import zav.discord.blanc.mock.ClientMock;
import zav.discord.blanc.mock.LinkMock;
import zav.discord.blanc.mock.SubredditMock;
import zav.jra.Client;
import zav.jra.Link;

public abstract class AbstractRedditTest extends AbstractTest {
    public ClientMock client;
    public SubredditMock redditdev;
    public SubredditMock modnews;
    public Link link;

    @BeforeEach
    public void setUpReddit() {
        client = new ClientMock();
        redditdev = new SubredditMock("redditdev");
        modnews = new SubredditMock("modnews");
        link = new LinkMock(1);

        redditdev.links.add(link);

        client.subreddits.put("redditdev", redditdev);
        client.subreddits.put("modnews", modnews);
    }
}
