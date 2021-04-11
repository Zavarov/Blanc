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

import de.monticore.prettyprint.IndentPrinter;
import zav.discord.blanc.parser._visitor.ParserVisitor;
import zav.discord.blanc.parser.ArithmeticArgument;
import zav.discord.blanc.parser.StringArgument;
import zav.mc.math.calculator.ArithmeticExpressionsValueCalculator;
import zav.mc.math.prettyprint.ArithmeticExpressionsPrettyPrinter;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

public class ASTExpressionArgument extends ASTExpressionArgumentTOP implements ArithmeticArgument, StringArgument {
    private static final ArithmeticExpressionsPrettyPrinter prettyPrinter = new ArithmeticExpressionsPrettyPrinter(new IndentPrinter());
    @Override
    public BigDecimal getValue() {
        return ArithmeticExpressionsValueCalculator.valueOf(getExpression()).orElseThrow();
    }

    @Override
    public String getContent() {
        return prettyPrinter.prettyprint(getExpression());
    }

    @Override
    public void accept(ParserVisitor visitor) {
        try {
            //The argument may be a string, so getValue() could cause an exception
            visitor.handle((ArithmeticArgument) this);
        }catch(NoSuchElementException ignored){}

        visitor.handle((StringArgument) this);
    }

    @Override
    public ASTExpressionArgument getRealThis() {
        return this;
    }
}
