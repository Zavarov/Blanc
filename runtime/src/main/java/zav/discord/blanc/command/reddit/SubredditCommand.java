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

import chart.line.JFreeLineChart;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import vartas.chart.line.$factory.LineChartFactory;
import vartas.chart.line.$factory.NumberDatasetFactory;
import vartas.chart.line.LineChart;
import vartas.chart.line.Position;
import zav.discord.blanc.Main;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;

public class SubredditCommand extends SubredditCommandTOP implements SnowflakeCommand{
    /*
    private static final Rectangle dimension = new Rectangle(1024, 768);

    private final DiscreteDomain<LocalDate> domain = new JSONSubreddit.DiscreteLocalDateDomain();
    private Range<LocalDate> range;
    private Subreddit subreddit;
    */
    @Override
    public void run() {
        throw new UnsupportedOperationException();
        /*
        subreddit = Main.REDDIT_CLIENT.getUncheckedSubreddits(getSubreddit());
        range = Range.closedOpen(getFrom(), getTo());

        LineChart chart = LineChartFactory.create(
                JFreeLineChart::new,
                getGranularity(),
                "Time",
                "Count",
                String.format("Snowflake Chart over r/%s (per %s)", subreddit.getName(), getGranularity().name().toLowerCase())
        );

        for(String flag : get$Flags()){
            switch(flag.toLowerCase(Locale.ENGLISH)){
                case "submission":
                    addSubmissions(chart);
                    break;
                case "submitter":
                    addSubmitters(chart);
                    break;
                case "comment":
                    addComments(chart);
                    break;
                case "commenter":
                    addCommenters(chart);
                    break;
                default:
                    throw new IllegalArgumentException(flag + " is not a valid flag.");
            }
        }

        get$MessageChannel().send(chart.create(dimension.width, dimension.height), getSubreddit()+".png");
         */
    }
    /*
    private void addSubmissions(LineChart chart){
        for(LocalDate date : ContiguousSet.create(range, domain)) {
            Instant inclusiveFrom = date.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant exclusiveTo = domain.next(date).atStartOfDay(ZoneOffset.UTC).toInstant();
            Range<Instant> range = Range.closedOpen(inclusiveFrom, exclusiveTo);
            LocalDateTime day = date.atStartOfDay();

            chart.addEntries(NumberDatasetFactory.create(countSubmissions(subreddit, range), day, "#Submissions", Position.LEFT));
            chart.addEntries(NumberDatasetFactory.create(countNsfwSubmissions(subreddit, range), day, "#NSFW Submissions", Position.LEFT));
            chart.addEntries(NumberDatasetFactory.create(countSpoilerSubmissions(subreddit, range), day, "#Spoiler Submissions", Position.LEFT));
        }
    }

    private void addComments(LineChart chart){
        for(LocalDate date : ContiguousSet.create(range, domain)) {
            Instant inclusiveFrom = date.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant exclusiveTo = domain.next(date).atStartOfDay(ZoneOffset.UTC).toInstant();
            Range<Instant> range = Range.closedOpen(inclusiveFrom, exclusiveTo);
            LocalDateTime day = date.atStartOfDay();

            chart.addEntries(NumberDatasetFactory.create(countComments(subreddit, range), day, "#Comments", Position.LEFT));
        }
    }

    private void addSubmitters(LineChart chart){
        for(LocalDate date : ContiguousSet.create(range, domain)) {
            Instant inclusiveFrom = date.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant exclusiveTo = domain.next(date).atStartOfDay(ZoneOffset.UTC).toInstant();
            Range<Instant> range = Range.closedOpen(inclusiveFrom, exclusiveTo);
            LocalDateTime day = date.atStartOfDay();

            chart.addEntries(NumberDatasetFactory.create(countUniqueSubmitters(subreddit, range), day, "#Unique Submitters", Position.LEFT));

        }
    }

    private void addCommenters(LineChart chart){
        for(LocalDate date : ContiguousSet.create(range, domain)) {
            Instant inclusiveFrom = date.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant exclusiveTo = domain.next(date).atStartOfDay(ZoneOffset.UTC).toInstant();
            Range<Instant> range = Range.closedOpen(inclusiveFrom, exclusiveTo);
            LocalDateTime day = date.atStartOfDay();

            chart.addEntries(NumberDatasetFactory.create(countUniqueCommenters(subreddit, range), day, "#Unique Commenters", Position.LEFT));

        }
    }
     */
}
