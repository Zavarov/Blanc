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

package zav.discord.blanc.command.reddit;

import com.google.common.collect.Range;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import zav.discord.blanc._factory.MessageEmbedFactory;
import zav.discord.blanc.MessageEmbed;
import zav.jra.Comment;
import zav.jra.models.Submission;
import zav.jra.Subreddit;
import zav.discord.blanc.Main;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;

public class AccountCommand extends AccountCommandTOP implements SnowflakeCommand{
    private static final int size = 5;

    private final MessageEmbed messageEmbed = MessageEmbedFactory.create();
    private final Table.Builder markdown = new Table.Builder();

    private Range<Instant> range ;
    private Subreddit subreddit;

    @Override
    public void run() {
        throw new UnsupportedOperationException();
        /*
        Instant inclusiveFrom = getFrom().atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant exclusiveTo = getTo().atStartOfDay(ZoneOffset.UTC).toInstant();
        range = Range.closedOpen(inclusiveFrom, exclusiveTo);
        subreddit = Main.REDDIT_CLIENT.getUncheckedSubreddits(getSubreddit());

        buildTitle();
        buildSubmissions();
        buildSubmissionsPerDay();
        buildComments();
        buildCommentsPerDay();
        buildTopSubmissions();
        buildTopComments();

        get$MessageChannel().send(messageEmbed);
        get$MessageChannel().send(markdown.build().toString().getBytes(StandardCharsets.UTF_8),getAccount()+".md");
         */
    }

    /*
    private void buildTitle(){
        String name = "u/"+getAccount();
        String link = "https://www.reddit.com/u/"+getAccount();

        messageEmbed.setAuthor(name, link);
        markdown.addRow(new Link(name, link), null);
    }

    private void buildSubmissions(){
        long count = countSubmissions(subreddit, range, getAccount());

        messageEmbed.addFields("#Submissions", count, true);
        markdown.addRow(new BoldText("#Submissions"), count);
    }

    private void buildSubmissionsPerDay(){
        double count = countSubmissionsPerDay(subreddit, range, getAccount());

        messageEmbed.addFields("#Submissions/day", count, true);
        markdown.addRow(new BoldText("#Submissions/day"), count);
    }

    private void buildComments(){
        long count = countComments(subreddit, range, getAccount());

        messageEmbed.addFields("#Comments", count, true);
        markdown.addRow(new BoldText("#Comments"), count);
    }

    private void buildCommentsPerDay(){
        double count = countCommentsPerDay(subreddit, range, getAccount());

        messageEmbed.addFields("#Comments/day", count, true);
        markdown.addRow(new BoldText("#Comments/day"), count);
    }

    private void buildTopSubmissions(){
        StringBuilder stringBuilder = new StringBuilder();
        Set<Submission> submissions = getTopSubmissions(subreddit, range, getAccount(), size);

        markdown.addRow(new BoldText("Top Submission"), new BoldText("Score"));
        for(Submission submission : submissions){
            Link link = new Link(submission.getTitle(), submission.getShortLink());

            markdown.addRow(link, submission.getScore());
            stringBuilder.append(link).append("[").append(submission.getScore()).append("]").append("\n");
        }

        messageEmbed.addFields("Top Submissions", stringBuilder.toString());
    }

    private void buildTopComments(){
        StringBuilder stringBuilder = new StringBuilder();
        Set<Comment> comments = getTopComments(subreddit, range, getAccount(), size);

        markdown.addRow(new BoldText("Top Comments"), new BoldText("Score"));
        for(Comment comment : comments){
            Link link = new Link(comment.getSubmission().getTitle(), comment.getPermaLink());

            markdown.addRow(link, comment.getScore());
            stringBuilder.append(link).append("[").append(comment.getScore()).append("]").append("\n");
        }

        messageEmbed.addFields("Top Comments", stringBuilder.toString());
    }
     */
}
