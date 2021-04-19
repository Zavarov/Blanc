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
import zav.discord.blanc._factory.MessageEmbedFactory;
import zav.discord.blanc.MessageEmbed;
import zav.jra.Comment;
import zav.jra.models.Submission;
import zav.jra.Subreddit;
import zav.discord.blanc.Main;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Map;

public class SnowflakeTableCommand extends SnowflakeTableCommandTOP implements SnowflakeCommand{
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
        MessageEmbed messageEmbed = MessageEmbedFactory.create();
        for(Submission submission : getTopSubmissions(subreddit, range, size)){
            String title = String.format("[%d] by u/%s", submission.getScore(), submission.getAuthor());
            String content = String.format("[%s](%s)", submission.getTitle(), submission.getShortLink());
            messageEmbed.addFields(title, content);
        }
        get$MessageChannel().send(messageEmbed);
    }

    private void printTopComments(Subreddit subreddit, Range<Instant> range){
        MessageEmbed messageEmbed = MessageEmbedFactory.create();
        for(Comment comment : getTopComments(subreddit, range, size)){
            String title = String.format("[%d] by u/%s", comment.getScore(), comment.getAuthor());
            String content = String.format("[%s](%s)", comment.getSubmission().getTitle(), comment.getPermaLink());
            messageEmbed.addFields(title, content);
        }
        get$MessageChannel().send(messageEmbed);
    }

    private void printTopSubmitters(Subreddit subreddit, Range<Instant> range){
        MessageEmbed messageEmbed = MessageEmbedFactory.create();
        for(Map.Entry<String, LimitedSubmissionSet> entry : getTopSubmitters(subreddit, range, size)){
            String title = String.format("u/%s [%d]", entry.getKey(), entry.getValue().getScore());
            for(Submission submission : entry.getValue()){
                String content = String.format("[%s](%s)[%d]", submission.getTitle(), submission.getShortLink(), submission.getScore());
                messageEmbed.addFields(title, content);
                //Only print the first submission to keep it short
                break;
            }
        }
        get$MessageChannel().send(messageEmbed);
    }

    private void printTopCommenters(Subreddit subreddit, Range<Instant> range){
        MessageEmbed messageEmbed = MessageEmbedFactory.create();
        for(Map.Entry<String, LimitedCommentSet> entry : getTopCommenters(subreddit, range, size)){
            String title = String.format("u/%s [%d]", entry.getKey(), entry.getValue().getScore());
            for(Comment comment : entry.getValue()){
                String content = String.format("[%s](%s)[%d]", comment.getSubmission().getTitle(), comment.getPermaLink(), comment.getScore());
                messageEmbed.addFields(title, content);
                //Only print the first comment to keep it short
                break;
            }
        }
        get$MessageChannel().send(messageEmbed);
    }

    private void printCore(Subreddit subreddit, Range<Instant> range){
        MessageEmbed messageEmbed = MessageEmbedFactory.create();
        messageEmbed.addFields("#Submissions", countSubmissions(subreddit, range), true);
        messageEmbed.addFields("#Comments", countComments(subreddit, range), true);
        messageEmbed.addFields("#Unique Submitters", countUniqueSubmitters(subreddit, range), true);
        messageEmbed.addFields("#Unique Commenters", countUniqueCommenters(subreddit, range), true);
        messageEmbed.addFields("Total Submission Score", countTotalSubmissionScore(subreddit, range), true);
        messageEmbed.addFields("Total Comment Score", countTotalCommentScore(subreddit, range), true);
        messageEmbed.addFields("#Submissions/day", countSubmissionsPerDay(subreddit, range), true);
        messageEmbed.addFields("#Comments/day", countCommentsPerDay(subreddit, range), true);
        get$MessageChannel().send(messageEmbed);
    }
     */
}
