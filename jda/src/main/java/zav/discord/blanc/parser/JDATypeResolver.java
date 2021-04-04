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

package zav.discord.blanc.parser;

import net.dv8tion.jda.api.JDA;
import zav.discord.blanc.*;
import zav.discord.blanc.prettyprint.ArgumentPrettyPrinter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;

@Nonnull
public class JDATypeResolver extends AbstractTypeResolver{
    @Nonnull
    private final Shard shard;
    @Nonnull
    private final JDA jda;

    public JDATypeResolver(@Nonnull Shard shard, @Nonnull JDA jda){
        this(shard, jda, null, null);
    }

    public JDATypeResolver(@Nonnull Shard shard, @Nonnull JDA jda, @Nullable Guild guild, @Nullable TextChannel textChannel){
        super(guild, textChannel);
        this.shard = shard;
        this.jda = jda;
    }

    /**
     * Attempts to transform the provided {@link Argument} into a {@link Guild}.<br>
     * @param argument the {@link Argument} associated with the {@link Guild}.
     * @return the {@link Guild} associated with the {@link Argument}.
     * @throws NoSuchElementException if the {@link Argument} can't be resolved as a {@link Guild}.
     */
    @Nonnull
    @Override
    public Guild resolveGuild(@Nonnull Argument argument) throws NoSuchElementException {
        return new JDAGuildResolver(shard, jda).apply(argument).orElseThrow(
                () -> TypeResolverException.of(Errors.UNKNOWN_ENTITY, ArgumentPrettyPrinter.printPretty(argument))
        );
    }

    /**
     * Attempts to transform the provided {@link Argument} into a {@link TextChannel}.<br>
     * @param argument the {@link Argument} associated with the {@link TextChannel}.
     * @return the {@link TextChannel} associated with the {@link Argument}.
     * @throws NoSuchElementException if the {@link Argument} can't be resolved as a {@link TextChannel}.
     */
    @Nonnull
    @Override
    public TextChannel resolveTextChannel(@Nonnull Argument argument) throws NoSuchElementException{
        return new JDATextChannelResolver(shard, jda).apply(guild, textChannel, argument).orElseThrow(
                () -> TypeResolverException.of(Errors.UNKNOWN_ENTITY, ArgumentPrettyPrinter.printPretty(argument))
        );
    }

    /**
     * Attempts to transform the provided {@link Argument} into an {@link User}.<br>
     * @param argument the {@link Argument} associated with the {@link User}.
     * @return the {@link User} associated with the {@link Argument}.
     * @throws NoSuchElementException if the {@link Argument} can't be resolved as a {@link User}.
     */
    @Nonnull
    @Override
    public User resolveUser(@Nonnull Argument argument) throws NoSuchElementException{
        return new JDAUserResolver(shard, jda).apply(argument).orElseThrow(
                () -> TypeResolverException.of(Errors.UNKNOWN_ENTITY, ArgumentPrettyPrinter.printPretty(argument))
        );
    }

    /**
     * Attempts to transform the provided {@link Argument} into a {@link Role}.<br>
     * @param argument the {@link Argument} associated with the {@link Role}.
     * @return the {@link Role} associated with the {@link Argument}.
     * @throws NoSuchElementException if the {@link Argument} can't be resolved as a {@link Role}.
     */
    @Nonnull
    @Override
    public Role resolveRole(@Nonnull Argument argument) throws NoSuchElementException{
        return new JDARoleResolver(shard, jda).apply(guild, textChannel, argument).orElseThrow(
                () -> TypeResolverException.of(Errors.UNKNOWN_ENTITY, ArgumentPrettyPrinter.printPretty(argument))
        );
    }

    /**
     * Attempts to transform the provided {@link Argument} into a {@link Member}.<br>
     * @param argument the {@link Argument} associated with the {@link Member}.
     * @return the {@link Member} associated with the {@link Argument}.
     * @throws NoSuchElementException if the {@link Argument} can't be resolved as a {@link Member}.
     */
    @Nonnull
    @Override
    public Member resolveMember(@Nonnull Argument argument) throws NoSuchElementException{
        return new JDAMemberResolver(shard, jda).apply(guild, textChannel, argument).orElseThrow(
                () -> TypeResolverException.of(Errors.UNKNOWN_ENTITY, ArgumentPrettyPrinter.printPretty(argument))
        );
    }

    @Override
    public Message resolveMessage(Argument argument) throws NoSuchElementException {
        return new JDAMessageResolver(shard, jda).apply(guild, textChannel, argument).orElseThrow(
                () -> TypeResolverException.of(Errors.UNKNOWN_ENTITY, ArgumentPrettyPrinter.printPretty(argument))
        );
    }
}
