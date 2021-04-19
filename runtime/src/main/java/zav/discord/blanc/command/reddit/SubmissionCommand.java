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

import chart.pie.JFreePieChart;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.chart.pie.$factory.NumberDatasetFactory;
import vartas.chart.pie.$factory.PieChartFactory;
import vartas.chart.pie.Dataset;
import vartas.chart.pie.PieChart;
import zav.discord.blanc.Main;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SubmissionCommand extends SubmissionCommandTOP {
    /*
    private static final String BOTH = "both";
    private static final String UNTAGGED = "untagged";
    private static final String SPOILER = "Spoiler";
    private static final String NSFW = "NSFW";

    private static final Map<String, Color> COLORS = new HashMap<>();

    private static final double ALPHA = 0.66;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final DiscreteDomain<LocalDate> domain = new JSONSubreddit.DiscreteLocalDateDomain();
    private Subreddit subreddit;
    private Range<LocalDate> range;

    static{
        COLORS.put(UNTAGGED, new Color(119,119,119,(int)(225*ALPHA)));
        COLORS.put(NSFW, new Color(255,0,0,(int)(225*ALPHA)));
        COLORS.put(SPOILER, new Color(0,0,0,(int)(225*ALPHA)));
        COLORS.put(BOTH, blend(Color.RED,Color.BLACK, ALPHA));
    }
    */
    @Override
    public void run() {
        throw new UnsupportedOperationException();
        /*
        subreddit = Main.REDDIT_CLIENT.getUncheckedSubreddits(getSubreddit());
        range = Range.closedOpen(getFrom(), getTo());

        try {
            PieChart chart;
            switch (type.toLowerCase(Locale.ENGLISH)) {
                case "tag":
                    chart = createTagChart();
                    break;
                case "flair":
                    chart = createFlairChart();
                    break;
                default:
                    throw new IllegalArgumentException(type + " is not a valid type.");
            }

            send$MessageChannel(chart.create(WIDTH, HEIGHT), getSubreddit());
        }catch(HttpResponseException | UnsuccessfulRequestException e){
            throw new IllegalStateException(e);
        }
         */
    }
    /*
    private PieChart createTagChart() throws UnsuccessfulRequestException, HttpResponseException {
        PieChart chart = PieChartFactory.create(JFreePieChart::new, "Submission tags over r/" + getSubreddit());
        Map<String, Long> data = countTags();
        createChart(chart, data, COLORS);
        return chart;
    }

    private Map<String, Long> countTags() throws HttpResponseException, UnsuccessfulRequestException {
        Map<String, Long> data = new HashMap<>();

        //Gather data
        for(LocalDate date : ContiguousSet.create(range, domain)){
            Instant inclusiveFrom = date.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant exclusiveTo = domain.next(date).atStartOfDay(ZoneOffset.UTC).toInstant();

            log.info("Requesting submissions over [{}, {})", inclusiveFrom, exclusiveTo);
            log.info(subreddit.getClass().toString());

            List<Submission> submissions = subreddit.getSubmissions(inclusiveFrom, exclusiveTo);

            for(Submission submission : submissions){
                if(submission.getNsfw() && submission.getSpoiler())
                    data.merge(BOTH, 1L, Long::sum);
                else if(submission.getNsfw())
                    data.merge(NSFW, 1L, Long::sum);
                else if(submission.getSpoiler())
                    data.merge(SPOILER, 1L, Long::sum);
                else
                    data.merge(UNTAGGED, 1L, Long::sum);
            }
        }

        return data;
    }

    private PieChart createFlairChart() throws UnsuccessfulRequestException, HttpResponseException {
        PieChart chart = PieChartFactory.create(JFreePieChart::new, "Submission flairs over r/"+getSubreddit());
        Map<String, Long> data = countFlairs();
        createChart(chart, data, new HashMap<>());
        return chart;
    }

    private Map<String, Long> countFlairs() throws HttpResponseException, UnsuccessfulRequestException {
        Map<String, Long> data = new HashMap<>();

        //Gather data
        for(LocalDate date : ContiguousSet.create(range, domain)){
            Instant inclusiveFrom = date.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant exclusiveTo = domain.next(date).atStartOfDay(ZoneOffset.UTC).toInstant();

            log.info("Requesting submissions over ({}, {}]", inclusiveFrom, exclusiveTo);

            List<Submission> submissions = subreddit.getSubmissions(inclusiveFrom, exclusiveTo);

            for(Submission submission : submissions){
                String label;
                label = submission.getLinkFlairText().orElse("unflaired");
                label = label.isBlank() ? "unflaired" : label;
                data.merge(label, 1L, Long::sum);
            }
        }

        return data;
    }

    private void createChart(PieChart chart, Map<String, Long> data, Map<String, Color> colors){
        data.forEach((key, value) -> {
            Color color = colors.computeIfAbsent(key, name -> new Color(name.hashCode() & 0xFFFFFF));
            Dataset dataset = NumberDatasetFactory.create(value, color);
            chart.putEntries(key, dataset);
        });
    }
    */
    /**
     * C = A*alpha - (1-alpha)*B
     */
    private static Color blend(Color A, Color B, double alpha){
        int r = (int)(A.getRed() * alpha + (1-alpha) * B.getRed());
        int b = (int)(A.getBlue() * alpha + (1-alpha) * B.getBlue());
        int g = (int)(A.getGreen()* alpha + (1-alpha) * B.getGreen());
        return new Color(r,g,b,(int)(alpha*255));
    }
}
