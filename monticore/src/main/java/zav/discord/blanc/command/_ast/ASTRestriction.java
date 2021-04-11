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

package zav.discord.blanc.command._ast;

import zav.discord.blanc.command.CommandMill;

import java.util.Objects;

public class ASTRestriction extends ASTRestrictionTOP{
    public static final ASTRestriction GUILD = CommandMill.restrictionBuilder().setName("Guild").build();
    public static final ASTRestriction ATTACHMENT = CommandMill.restrictionBuilder().setName("Attachment").build();

    @Override
    public int hashCode(){
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ASTRestriction)
            return Objects.equals(((ASTRestriction)o).getName(), getName());
        else return false;
    }
}
