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

package vartas.discord.blanc.command.cocos;

import de.se_rwth.commons.logging.Log;
import vartas.discord.blanc.command._ast.ASTPermission;
import vartas.discord.blanc.command._cocos.CommandASTPermissionCoCo;

public class PermissionIsValidCoCo implements CommandASTPermissionCoCo {
    @Override
    public void check(ASTPermission ast) {
        try{
            ast.getPermission();
        }catch(IllegalArgumentException e){
            //TODO Error Message
            Log.error(e.getMessage());
        }
    }
}
