/*
 * Copyright (C) 2018 u/Zavarov
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
package vartas.discordbot.command;

import java.util.Arrays;
import net.dv8tion.jda.core.Permission;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author u/Zavarov
 */
public class MissingPermissionExceptionTest {
    MissingPermissionException exception;
    @Before
    public void setUp(){
        exception = new MissingPermissionException(Arrays.asList(Permission.ADMINISTRATOR,Permission.BAN_MEMBERS));
    }
    @Test
    public void checkMessageTest(){
        assertEquals(exception.getMessage(),"You need the following permissions to use the command:\nAdministrator\nBan Members");
    }
}