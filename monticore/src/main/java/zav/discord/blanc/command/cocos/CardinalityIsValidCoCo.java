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

package zav.discord.blanc.command.cocos;

import de.monticore.cardinality._ast.ASTCardinality;
import de.monticore.cardinality._cocos.CardinalityASTCardinalityCoCo;
import de.se_rwth.commons.logging.Log;

public class CardinalityIsValidCoCo implements CardinalityASTCardinalityCoCo {
    @Override
    public void check(ASTCardinality ast) {
        //TODO Error Message
        if(ast.getLowerBound() > ast.getUpperBound())
            Log.error("error");
    }
}
