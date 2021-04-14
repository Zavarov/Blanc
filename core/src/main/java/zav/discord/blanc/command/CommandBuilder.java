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

package zav.discord.blanc.command;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc.*;
import zav.discord.blanc.command._visitor.CommandVisitor;
import zav.discord.blanc.parser.AbstractTypeResolver;
import zav.discord.blanc.parser.IntermediateCommand;
import zav.discord.blanc.parser.Parser;
import zav.discord.blanc.prettyprint.ArgumentPrettyPrinter;
import zav.discord.blanc.Guild;
import zav.discord.blanc.MessageChannel;
import zav.discord.blanc.Shard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * This class is responsible for transforming a received text {@link Message Message} into a {@link Command}. The
 * transformation itself is split into two parts. First, the {@link Message} will be parsed and turned into an
 * {@link IntermediateCommand}. In this state, the syntactical correctness of the command is verified.
 * <p>
 * In the next step, assuming that the verification was successful, the concrete command instance is calculated.
 * @see IntermediateCommand
 * @see Command
 */
@Nonnull
public abstract class CommandBuilder extends CommandBuilderTOP {
    /**
     * This class' logger.
     */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    /**
     * Responsible for creating the {@link IntermediateCommand}.
     */
    @Nonnull
    private final Parser parser;
    /**
     * The command prefix accepted in all situations.
     */
    @Nonnull
    private final String globalPrefix;
    /**
     * Provides the type resolver for the command arguments. The type resolver is guild-sensitive, so for each
     * command, an individual resolver has to be created.
     */
    @Nonnull
    private final BiFunction<? super Guild, ? super TextChannel, ? extends AbstractTypeResolver> typeResolverFunction;

    /**
     * Initializes the builder.
     * @param parser The {@link Parser} used for processing the messages.
     * @param globalPrefix The global command prefix.
     */
    public CommandBuilder(
            @Nonnull BiFunction<? super Guild, ? super TextChannel, ? extends AbstractTypeResolver> typeResolverFunction,
            @Nonnull Shard shard,
            @Nonnull Parser parser,
            @Nonnull String globalPrefix
    )
    {
        super(typeResolverFunction, shard, parser, globalPrefix);
        this.parser = parser;
        this.globalPrefix = globalPrefix;
        this.typeResolverFunction = typeResolverFunction;
        super.shard = shard;
    }

    /**
     * Part of the visitor pattern to grant access to the explicit implementation of the individual types.
     * @return The current instance.
     */
    @Override
    public CommandBuilder getRealThis(){
        return this;
    }

    /**
     * Processes the received {@link Message} from an arbitrary {@link MessageChannel} and attempts to transform
     * it into an executable {@link Command}. The command will be created if and only if the message starts with a
     * valid prefix and if a {@link Command} with the specified name exists.
     * <p>
     * Every command returned by this method is considered to be made outside a guild.
     * @param message The received {@link Message}.
     * @param channel the {@link MessageChannel} from which the {@link Message} was received.
     * @return An {@link Optional} containing the {@link Command} associated with the {@link Message}. If the command
     *         is couldn't be created due to an unspecified reason, {@link Optional#empty()} is returned.
     */
    @Nonnull
    @Override
    public Optional<Command> build(@Nonnull Message message, @Nonnull MessageChannel channel){
        Optional<? extends IntermediateCommand> commandOpt = parser.parse(message);

        //Message is not a command
        if(commandOpt.isEmpty())
            return Optional.empty();

        IntermediateCommand command = commandOpt.get();

        log.info("Received command {} : {} {}",
                command.getPrefix(),
                command.getName(),
                command.getArguments().stream().map(ArgumentPrettyPrinter::printPretty).collect(Collectors.toList())
        );

        typeResolver = typeResolverFunction.apply(null, null);

        if(comparePrefix(command))
            return build(command.getName(), command.getArguments(), command.getFlags())
                    .map(c -> provideContext(c, message, channel));
        else
            return Optional.empty();
    }


    /**
     * Processes the received {@link Message} from an arbitrary {@link TextChannel} and attempts to transform
     * it into an executable {@link Command}. The command will be created if and only if the message starts with a
     * valid prefix and if a {@link Command} with the specified name exists. The prefix can either be the global prefix
     * or a guild-specific prefix.
     * <p>
     * Every command returned by this method is considered to be made inside a guild.
     * @param message the received {@link Message}.
     * @param guild the {@link Guild} associated with the {@link TextChannel} from which the {@link Message} was received.
     * @param textChannel the {@link TextChannel} from which the {@link Message} was received.
     * @return the {@link Command} instance associated with the {@link Message}.
     */
    @Nonnull
    @Override
    public Optional<Command> build(@Nonnull Message message, @Nonnull Guild guild, @Nonnull TextChannel textChannel){
        Optional<? extends IntermediateCommand> commandOpt = parser.parse(message);

        //Message is not a command
        if(commandOpt.isEmpty())
            return Optional.empty();

        IntermediateCommand command = commandOpt.get();

        log.info("Received command {} : {} {}",
                command.getPrefix(),
                command.getName(),
                command.getArguments().stream().map(ArgumentPrettyPrinter::printPretty).collect(Collectors.toList())
        );

        typeResolver = typeResolverFunction.apply(guild, textChannel);

        if(comparePrefix(command) || comparePrefix(command, guild))
            return build(command.getName(), command.getArguments(), command.getFlags())
                    .map(c -> provideContext(c, message, guild, textChannel));
        else
            return Optional.empty();
    }

    /**
     * Checks if the {@link Command} starts with the ${@link Guild} prefix.
     * @param command The {@link Command} associated with the received {@link Message}.
     * @param guild The {@link Guild} associated with the received {@link Message}.
     * @return <code>true</code> if the {@link Command} prefix is matches the {@link Guild} prefix. In case the
     *         {@link Guild} doesn't have a prefix specified, <code>false</code> is returned.
     * @see Guild#getPrefix()
     */
    private boolean comparePrefix(@Nonnull IntermediateCommand command, @Nonnull Guild guild){
        return guild.getPrefix().equals(command.getPrefix()) && command.getPrefix().isPresent();
    }

    /**
     * Checks if the {@link Command} starts with the global prefix.
     * @param command The {@link Command} associated with the received {@link Message}.
     * @return <code>true</code> if the {@link Command} prefix is matches the global prefix.
     */
    private boolean comparePrefix(@Nonnull IntermediateCommand command){
        return command.getPrefix().map(globalPrefix::equals).orElse(false);
    }

    /**
     * Parameters such as the received {@link Message}, which are not required for the parsing processing and as such
     * aren't initialized upon creation. They are set by this method in order to provide them during execution.
     * @param command The {@link Command} created by the received {@link Message}.
     * @param message The {@link Message} corresponding to the {@link Command}.
     * @param guild The {@link Guild} the {@link Message} was received in. May be <code>null</code> in case the
     *              {@link Message} wasn't received inside a guild.
     * @param messageChannel The {@link MessageChannel} the {@link Message} was received in.
     * @return The {@link Command} provided as an argument. Upon return, the attributes of the {@link Command} matching
     *         the remaining arguments have been properly initialized.
     */
    @Nonnull
    private Command provideContext
            (
                    @Nonnull Command command,
                    @Nonnull Message message,
                    @Nullable Guild guild,
                    @Nonnull MessageChannel messageChannel
            )
    {
        command.accept(new ContextProvider(message, guild, messageChannel));
        return command;
    }

    /**
     * Parameters such as the received {@link Message}, which are not required for the parsing processing and as such
     * aren't initialized upon creation. They are set by this method in order to provide them during execution.
     * @param command The {@link Command} created by the received {@link Message}.
     * @param message The {@link Message} corresponding to the {@link Command}.
     * @param messageChannel The {@link MessageChannel} the {@link Message} was received in.
     * @return The {@link Command} provided as an argument. Upon return, the attributes of the {@link Command} matching
     *         the remaining arguments have been properly initialized.
     */
    @Nonnull
    private Command provideContext
            (
                    @Nonnull Command command,
                    @Nonnull Message message,
                    @Nonnull MessageChannel messageChannel
            )
    {
        return provideContext(command, message, null, messageChannel);
    }

    /**
     * This visitor is used to break up the encapsulation of the commands. Depending on what type of {@link Command} was
     * created, the attributes that weren't set during creation are initialized properly.
     */
    @Nonnull
    private class ContextProvider implements CommandVisitor{
        /**
         * The {@link Guild} the visited {@link Command} was received in. Null if the {@link Message} wasn't received
         * inside a {@link Guild}.
         */
        @Nullable
        private final Guild guild;
        /**
         * The {@link MessageChannel} in which the visited {@link Command} was received in.
         */
        @Nonnull
        private final MessageChannel messageChannel;
        /**
         * The {@link User} who issued the visited {@link Command}.
         */
        @Nonnull
        private final User author;
        /**
         * The {@link Message} corresponding to the visited {@link Command}.
         */
        @Nonnull
        private final Message message;

        /**
         * Creates a fresh visitor instance.
         * @param message The {@link Message} corresponding to the visited {@link Command}.
         * @param guild The {@link Guild} the {@link Message} was received in. May be <code>null</code> if the
         *              {@link Message} wasn't received inside a guild.
         * @param messageChannel The {@link MessageChannel} the {@link Message} was received in.
         */
        @Nonnull
        public ContextProvider(@Nonnull Message message, @Nullable Guild guild, @Nonnull MessageChannel messageChannel){
            this.message = message;
            this.author = message.getAuthor();
            this.guild = guild;
            this.messageChannel = messageChannel;
        }

        /**
         * Completes the initialization of a {@link Command} that can be executed without a {@link Guild}.
         * @param command The {@link Command} associated with the provided attributes.
         */
        @Override
        public void visit(@Nonnull MessageCommand command){
            command.set$Author(author);
            command.set$MessageChannel(messageChannel);
            command.set$Shard(shard);
            command.set$Message(message);
        }

        /**
         * Completes the initialization of a {@link Command} that can only be executed inside a {@link Guild}.
         * @param command The {@link Command} associated with the provided attributes.
         * @throws NullPointerException If {@link #guild} is <code>null</code>.
         */
        @Override
        public void visit(@Nonnull GuildCommand command) throws NullPointerException{
            //A guild command needs a guild
            Preconditions.checkNotNull(guild);

            command.set$Author(guild.retrieveMember(author.getId()).orElseThrow());
            command.set$TextChannel(guild.retrieveTextChannel(messageChannel.getId()).orElseThrow());
            command.set$Guild(guild);
            command.set$Shard(shard);
            command.set$Message(message);
        }
    }
}