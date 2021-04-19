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

import org.apache.commons.collections4.CollectionUtils;
import zav.discord.blanc.Role;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class SelfAssignableRoleCommand extends SelfAssignableRoleCommandTOP{

    /**
     * First, the roles are received. If the role is self-assignable, it is added
     * to the user who requested it, if the person doesn't already have the role.
     * Otherwise it is removed.
     */
    @Override
    public void run() throws IOException {
        synchronized (SelfAssignableRoleCommand.class) {
            if(getRole().isPresentGroup()){
                if(get$Author().retrieveRoles().contains(getRole())){
                    get$Author().modifyRoles(Collections.emptySet(), Collections.singleton(getRole()));
                    get$TextChannel().send("You no longer have the role %s from group %s.", getRole().getName(), getRole().getGroup().orElseThrow());
                }else{
                    Set<Role> rolesInGroup = get$Guild()
                            .retrieveRoles()
                            .stream()
                            .filter(role -> role.getGroup().equals(getRole().getGroup()))
                            .collect(Collectors.toSet());

                    Collection<Role> memberRoles = get$Author().retrieveRoles();

                    Collection<Role> conflictingRoles = CollectionUtils.intersection(rolesInGroup, memberRoles);

                    get$Author().modifyRoles(Collections.singleton(getRole()), conflictingRoles);

                    get$TextChannel().send("You now have the role %s from group %s.", getRole().getName(), getRole().getGroup().orElseThrow());
                }
            }else{
                get$TextChannel().send("The specified role isn't self-assignable.");
            }
        }
    }
}
