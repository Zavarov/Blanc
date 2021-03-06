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

package vartas.discord.blanc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import vartas.discord.blanc.$json.JSONGuild;
import vartas.discord.blanc.$json.JSONRole;
import vartas.discord.blanc.$json.JSONTextChannel;
import vartas.discord.blanc.$json.JSONWebhook;
import vartas.discord.blanc.io.$json.JSONCredentials;
import vartas.discord.blanc.visitor.ActivityVisitor;
import vartas.discord.blanc.visitor.RedditVisitor;

import javax.annotation.Nonnull;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.*;

@Nonnull
public abstract class Shard extends ShardTOP{
    @Nonnull
    protected final ExecutorService worker;
    @Nonnull
    protected final ScheduledExecutorService executor;
    @Nonnull
    protected final static Semaphore MUTEX = new Semaphore(1);
    @Nonnull
    public static final java.time.Duration ACTIVITY_RATE = java.time.Duration.ofMinutes(30);

    protected static boolean MODIFIES_FILE = false;

    @Nonnull
    public Shard(){
        this.executor = Executors.newScheduledThreadPool(
                2,
                new ThreadFactoryBuilder().setNameFormat("Shard#%d").build()
        );
        this.worker = Executors.newCachedThreadPool(
                new ThreadFactoryBuilder().setNameFormat("Worker#%d").build()
        );
        //Update guild activity every 30 minutes
        this.executor.scheduleAtFixedRate(() -> this.accept(new ActivityVisitor()), ACTIVITY_RATE.toMinutes(), ACTIVITY_RATE.toMinutes(), TimeUnit.MINUTES);
    }

    @Nonnull
    public Shard(@Nonnull RedditVisitor redditVisitor){
        this();
        //Request submissions every minute with one minute initial delay
        this.executor.scheduleAtFixedRate(() -> this.accept(redditVisitor), 1, 1, TimeUnit.MINUTES);
    }

    @Nonnull
    public Shard(@Nonnull RedditVisitor redditVisitor, @Nonnull StatusMessageRunnable statusMessageRunnable){
        this(redditVisitor);
        this.executor.scheduleAtFixedRate(statusMessageRunnable, 0, JSONCredentials.CREDENTIALS.getStatusMessageUpdateInterval(), TimeUnit.MINUTES);
    }

    public void submit(Runnable runnable){
        worker.submit(runnable);
    }

    @Override
    public void shutdown() {
        //Prevents any further IO operations to avoid data corruption
        //Even if the threads block, they'll be terminated by System.exit()
        if(MODIFIES_FILE || MUTEX.availablePermits() > 0)
            MUTEX.acquireUninterruptibly();
        executor.shutdown();
    }

    @Override
    public Shard getRealThis() {
        return this;
    }

    public static void write(Guild guild){
        String fileName = JSONGuild.getFileName(guild);
        JSONObject jsonGuild = JSONGuild.toJson(guild, new JSONObject());

        Path fileDirectory = JSONCredentials.CREDENTIALS.getJsonDirectory().resolve(Long.toString(guild.getId()));
        Path filePath = fileDirectory.resolve(fileName);

        write(jsonGuild, filePath);
    }

    public static void write(Guild guild, TextChannel channel){
        String fileName = JSONTextChannel.getFileName(channel);
        JSONObject jsonChannel = JSONTextChannel.toJson(channel, new JSONObject());

        Path fileDirectory = JSONCredentials.CREDENTIALS.getJsonDirectory().resolve(Long.toString(guild.getId()));
        Path filePath = fileDirectory.resolve(fileName);

        write(jsonChannel, filePath);
    }

    public static void write(Guild guild, Role role){
        String fileName = JSONRole.getFileName(role);
        JSONObject jsonRole = JSONRole.toJson(role, new JSONObject());

        Path fileDirectory = JSONCredentials.CREDENTIALS.getJsonDirectory().resolve(Long.toString(guild.getId()));
        Path filePath = fileDirectory.resolve(fileName);

        write(jsonRole, filePath);
    }

    public static void write(Guild guild, Webhook webhook){
        String fileName = JSONWebhook.getFileName(webhook);
        JSONObject jsonWebhook = JSONWebhook.toJson(webhook, new JSONObject());

        Path fileDirectory = JSONCredentials.CREDENTIALS.getJsonDirectory().resolve(Long.toString(guild.getId()));
        Path filePath = fileDirectory.resolve(fileName);

        write(jsonWebhook, filePath);
    }

    public static void write(@Nonnull JSONObject jsonObject, @Nonnull Path target){
        try{
            MUTEX.acquireUninterruptibly();
            MODIFIES_FILE = true;
            FileWriter writer = new FileWriter(target.toFile());
            jsonObject.write(writer, 4, 0);
            writer.close();
        }catch(IOException e){
            LoggerFactory.getLogger(Shard.class.getSimpleName()).error(Errors.INVALID_FILE.toString(), e.toString());
        }finally {
            MODIFIES_FILE = false;
            MUTEX.release();
        }
    }
}
