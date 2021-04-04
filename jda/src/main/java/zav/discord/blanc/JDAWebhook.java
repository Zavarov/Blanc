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

package zav.discord.blanc;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc._factory.WebhookFactory;
import zav.discord.blanc._json.JSONWebhook;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class JDAWebhook extends Webhook{
    private static final Cache<Long, Webhook> WEBHOOKS = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(1)).build();

    private static final Logger log = LoggerFactory.getLogger(JDAWebhook.class.getSimpleName());
    @Nonnull
    public static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(
            0,
            new ThreadFactoryBuilder().setNameFormat("Webhook#%d").build()
    );

    @Nonnull
    public static Webhook create(@Nonnull net.dv8tion.jda.api.entities.Webhook jdaWebhook){
        Webhook webhook = WEBHOOKS.getIfPresent(jdaWebhook.getIdLong());

        if(webhook != null)
            return webhook;

        webhook = WebhookFactory.create(
                () -> new JDAWebhook(jdaWebhook),
                jdaWebhook.getIdLong(),
                jdaWebhook.getName()
        );

        try{
            Guild guild = JDAGuild.create(jdaWebhook.getGuild());
            JSONWebhook.fromJson(webhook, guild, jdaWebhook.getIdLong());
            log.info("Successfully loaded the JSON file for the webhook {}.", jdaWebhook.getName());
        }catch(IOException e){
            log.warn("Failed loading the JSON file for the webhook {} : {}", jdaWebhook.getName(), e.toString());
        }finally {
            WEBHOOKS.put(jdaWebhook.getIdLong(), webhook);
        }

        return webhook;
    }
    @Nonnull
    private final net.dv8tion.jda.api.entities.Webhook webhook;
    @Nonnull
    private final WebhookClient webhookClient;

    @Nonnull
    private JDAWebhook(@Nonnull net.dv8tion.jda.api.entities.Webhook webhook){
        this.webhook = webhook;
        //Avoid using a executor for each webhook instance to reduce performance issues.
        this.webhookClient = new WebhookClientBuilder(webhook.getUrl())
                .setExecutorService(EXECUTOR_SERVICE)
                .build();
    }

    @Override
    public Optional<Message> retrieveMessage(long id) {
        try {
            return Optional.of(JDAMessage.create(webhook.getChannel().retrieveMessageById(id).complete()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Collection<Message> retrieveMessages() {
        try {
            return webhook.getChannel().getHistory().getRetrievedHistory().stream().map(JDAMessage::create).collect(Collectors.toList());
        }catch(Exception e){
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void send(Message message) {
        try {
            webhookClient.send(WebhookMessageBuilder.buildMessage(webhook.getJDA().getSelfUser(), message));
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    @Override
    public void send(byte[] bytes, String qualifiedName) {
        try {
            webhookClient.send(bytes, qualifiedName);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    @Override
    public void shutdown(){
        EXECUTOR_SERVICE.shutdown();
    }
}
