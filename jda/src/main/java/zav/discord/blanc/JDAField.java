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

import zav.discord.blanc._factory.FieldFactory;

import javax.annotation.Nonnull;

public class JDAField extends Field{
    @Nonnull
    public static Field create(net.dv8tion.jda.api.entities.MessageEmbed.Field field){
        return FieldFactory.create(
                JDAField::new,
                field.getName(),
                field.getValue(),
                field.isInline()
        );
    }
}
