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

package zav.discord.blanc.callable._ast;

import zav.discord.blanc.parser.Argument;
import zav.discord.blanc.parser.IntermediateCommand;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ASTCallable extends ASTCallableTOP implements IntermediateCommand {

    @Override
    public Optional<String> getPrefix() {
        return isPresentAstPrefix() ? Optional.of(super.getAstPrefix()) : Optional.empty();
    }

    @Override
    public String getName() {
        return super.getQualifiedName();
    }

    @Override
    public List<String> getFlags() {
        return getAstFlagList().stream().map(ASTFlag::getName).collect(Collectors.toList());
    }

    @Override
    public List<? extends Argument> getArguments() {
        return super.getAstArgumentList();
    }
}
