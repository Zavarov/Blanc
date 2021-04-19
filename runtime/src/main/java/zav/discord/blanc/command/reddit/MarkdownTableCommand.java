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
import net.steppschuh.markdowngenerator.list.ListBuilder;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText;
import zav.jra.Comment;
import zav.jra.models.Submission;
import zav.jra.Subreddit;
import zav.discord.blanc.Main;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

public class MarkdownTableCommand extends MarkdownTableCommandTOP implements SnowflakeCommand{
    private static final int size = 10;

    @Override
    public void run() {
        throw new UnsupportedOperationException();
        /*
        Instant inclusiveFrom = getFrom().atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant exclusiveTo = getTo().atStartOfDay(ZoneOffset.UTC).toInstant();
        Range<Instant> range = Range.closedOpen(inclusiveFrom, exclusiveTo);

        Subreddit subreddit = Main.REDDIT_CLIENT.getUncheckedSubreddits(getSubreddit());

        switch(getType().toLowerCase(Locale.ENGLISH)){
            case "submission":
                printTopSubmissions(subreddit, range);
                break;
            case "submitter":
                printTopSubmitters(subreddit, range);
                break;
            case "comment":
                printTopComments(subreddit, range);
                break;
            case "commenter":
                printTopCommenters(subreddit, range);
                break;
            case "core":
                printCore(subreddit, range);
                break;
            default:
                throw new IllegalArgumentException(type + " is not a valid type.");
        }
         */
    }
    /*
    private void printTopSubmissions(Subreddit subreddit, Range<Instant> range){
        ListBuilder markdown = new ListBuilder();

        for(Submission submission : getTopSubmissions(subreddit, range, size)){
            Link link = new Link(submission.getTitle(), submission.getPermaLink());
            markdown.append(link + " " + new ItalicText("by") + " " + submission.getAuthor() + "(" + submission.getScore() + ")");
        }

        get$MessageChannel().send(markdown.build().toString().getBytes(StandardCharsets.UTF_8), getType()+".md");
    }

    private void printTopComments(Subreddit subreddit, Range<Instant> range){
        ListBuilder markdown = new ListBuilder();

        for(Comment comment : getTopComments(subreddit, range, size)){
            Link link = new Link(comment.getSubmission().getTitle(), comment.getPermaLink());
            markdown.append(new ItalicText("in") + " " + link + " " + new ItalicText("by") + " " + comment.getAuthor() + "(" + comment.getScore() + ")");
        }

        get$MessageChannel().send(markdown.build().toString().getBytes(StandardCharsets.UTF_8), getType()+".md");
    }

    private void printTopSubmitters(Subreddit subreddit, Range<Instant> range){
        Table.Builder markdown = new Table.Builder();

        for(Map.Entry<String, SnowflakeCommand.LimitedSubmissionSet> entry : getTopSubmitters(subreddit, range, size)){
            List<String> title = new ArrayList<>();
            title.add(String.format("u/%s (%d)", entry.getKey(), entry.getValue().getScore()));
            title.addAll(Collections.nCopies(10, null));

            markdown.addRow(title.toArray());

            markdown.addRow(entry.getValue().stream().map(submission -> {
                Link link = new Link(submission.getTitle(), submission.getPermaLink());
                return link + " (" + submission.getScore() + ")";
            }).toArray());
        }

        get$MessageChannel().send(markdown.build().toString().getBytes(StandardCharsets.UTF_8), getType()+".md");
    }

    private void printTopCommenters(Subreddit subreddit, Range<Instant> range){
        Table.Builder markdown = new Table.Builder();

        for(Map.Entry<String, SnowflakeCommand.LimitedCommentSet> entry : getTopCommenters(subreddit, range, size)){
            List<String> title = new ArrayList<>();
            title.add(String.format("u/%s (%d)", entry.getKey(), entry.getValue().getScore()));
            title.addAll(Collections.nCopies(10, null));

            markdown.addRow(title.toArray());

            markdown.addRow(entry.getValue().stream().map(comment -> {
                Link link = new Link(comment.getSubmission().getTitle(), comment.getPermaLink());
                return link + " (" + comment.getScore() + ")";
            }).toArray());
        }

        get$MessageChannel().send(markdown.build().toString().getBytes(StandardCharsets.UTF_8), getType()+".md");
    }

    private void printCore(Subreddit subreddit, Range<Instant> range){
        Table.Builder markdown = new Table.Builder().withAlignments(Table.ALIGN_LEFT, Table.ALIGN_LEFT);

        markdown.addRow(
                "Accumulated Values",
                null,
                null,
                null
        );
        markdown.addRow(
                new BoldText("#Submissions"),
                countSubmissions(subreddit, range),
                new BoldText("#Comments"),
                countComments(subreddit, range)
        );
        markdown.addRow(
                new BoldText("Unique Submitters"),
                countUniqueSubmitters(subreddit, range),
                new BoldText("Unique Commenters"),
                countUniqueCommenters(subreddit, range)
        );
        markdown.addRow(
                new BoldText("Total Submission Score"),
                countTotalSubmissionScore(subreddit, range),
                new BoldText("Total Comment Score"),
                countTotalCommentScore(subreddit, range)
        );
        markdown.addRow(
                new BoldText("#Submissions/day"),
                countSubmissionsPerDay(subreddit, range),
                new BoldText("#Comments/day"),
                countCommentsPerDay(subreddit, range)
        );

        get$MessageChannel().send(markdown.build().toString().getBytes(StandardCharsets.UTF_8), getType()+".md");
    }
     */
}
