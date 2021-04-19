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
package zav.discord.blanc.command.base;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * This command generates a plot of the activity in the respective guild.
 */
public class ActivityCommand extends ActivityCommandTOP{
    private static final Rectangle dimension = new Rectangle(1024, 768);

    @Override
    public void run() throws IOException {
        BufferedImage image = get$Guild().getActivity().create(get$Guild(), getChannels(), dimension);
        send$TextChannel(image, "Activity");
    }
}
