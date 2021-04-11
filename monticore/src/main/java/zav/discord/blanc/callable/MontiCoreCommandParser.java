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

package zav.discord.blanc.callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.discord.blanc.Message;
import zav.discord.blanc.callable._parser.CallableParser;
import zav.discord.blanc.parser.IntermediateCommand;
import zav.discord.blanc.parser.Parser;

import java.io.IOException;
import java.util.Optional;

public class MontiCoreCommandParser implements Parser {
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    private final CallableParser parser = new CallableParser();

    @Override
    public Optional<? extends IntermediateCommand> parse(Message message) {
        try {
            Optional<String> content = message.getContent();

            //images/files-only messages might not have any text content
            if(content.isEmpty())
                return Optional.empty();
            else
                return parser.parse_String(content.get());
        }catch(IOException e){
            //TODO Error message
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
