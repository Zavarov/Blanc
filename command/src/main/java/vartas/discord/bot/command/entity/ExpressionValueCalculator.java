package vartas.discord.bot.command.entity;

import de.monticore.MCLiteralsDecoder;
import de.monticore.ast.ASTNode;
import de.monticore.expressions.commonexpressions._ast.*;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.prettyprint2.CommonExpressionsPrettyPrinter;
import de.monticore.mcbasicliterals._ast.ASTSignedBasicDoubleLiteral;
import de.monticore.mcbasicliterals._ast.ASTSignedBasicFloatLiteral;
import de.monticore.mcbasicliterals._ast.ASTSignedBasicLongLiteral;
import de.monticore.mcbasicliterals._ast.ASTSignedNatLiteral;
import de.monticore.prettyprint.IndentPrinter;
import vartas.discord.bot.command.entity._ast.*;
import vartas.discord.bot.command.entity._visitor.EntityVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/*
 * Copyright (C) 2019 Zavarov
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
public class ExpressionValueCalculator implements EntityVisitor {
    /**
     * The maximum number of rolls that are allowed at once.
     */
    public static long MAX_EYE = 1024;
    /**
     * The maximum number of eyes a die can have.
     */
    public static long MAX_DICE = 256;
    /**
     * The number generator for the dice.
     */
    private static Random rng = new Random();

    protected Map<ASTNode, BigDecimal> values = new HashMap<>();

    protected ExpressionValueCalculator(){}

    public Optional<BigDecimal> getValue(ASTExpression expression){
        return values.containsKey(expression) ? Optional.of(values.get(expression)) : Optional.empty();
    }

    public static BigDecimal valueOf(ASTExpression expression){
        ExpressionValueCalculator calculator = new ExpressionValueCalculator();
        expression.accept(calculator);

        return calculator.getValue(expression).orElseThrow(() -> new IllegalStateException("The value of the expression couldn't be determined"));
    }

    //***------------------------------------------------------------------------------------------------------------***
    //***---------------------------------------------  Literals  ---------------------------------------------------***
    //***------------------------------------------------------------------------------------------------------------***
    @Override
    public void visit(ASTSignedNatLiteral node) {
        values.put(node, BigDecimal.valueOf(MCLiteralsDecoder.decodeNat((node.isNegative() ? "-" : "") + node.getDigits())));
    }

    @Override
    public void visit(ASTSignedBasicLongLiteral node) {
        values.put(node, BigDecimal.valueOf(MCLiteralsDecoder.decodeLong((node.isNegative() ? "-" : "") + node.getDigits() + "L")));
    }

    @Override
    public void visit(ASTSignedBasicFloatLiteral node) {
        values.put(node, BigDecimal.valueOf(MCLiteralsDecoder.decodeFloat((node.isNegative() ? "-" : "") + node.getPre() + "." + node.getPost() + "F")));
    }

    @Override
    public void visit(ASTSignedBasicDoubleLiteral node) {
        values.put(node, BigDecimal.valueOf(MCLiteralsDecoder.decodeDouble((node.isNegative() ? "-" : "") + node.getPre() + "." + node.getPost() + "D")));
    }
    //***------------------------------------------------------------------------------------------------------------***
    //***------------------------------------------  Expressions  ---------------------------------------------------***
//***------------------------------------------------------------------------------------------------------------***
    @Override
    public void visit(ASTBooleanNotExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTLogicalNotExpression node) {
        throw new IllegalArgumentException("Logical operators are not a allowed.");
    }

    @Override
    public void visit(ASTLessEqualExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTGreaterEqualExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTLessThanExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTGreaterThanExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTEqualsExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTNotEqualsExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTBooleanAndOpExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTBooleanOrOpExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTConditionalExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTSimpleAssignmentExpression node) {
        throw new IllegalArgumentException("Assignments are not a allowed.");
    }

    @Override
    public void visit(ASTCallExpression node) {
        CommonExpressionsPrettyPrinter prettyPrinter = new CommonExpressionsPrettyPrinter(new IndentPrinter());
        throw new IllegalArgumentException("The call " + prettyPrinter.prettyprint(node) + " is not defined.");
    }

    @Override
    public void visit(ASTQualifiedNameExpression node) {
        throw new IllegalArgumentException("Qualified names are not a allowed.");
    }

    @Override
    public void visit(ASTNameExpression name) {
        switch (name.getName().toLowerCase(Locale.ENGLISH)) {
            case "pi": {
                values.put(name, BigDecimal.valueOf(Math.PI));
                break;
            }
            case "e": {
                values.put(name, BigDecimal.valueOf(Math.E));
                break;
            }
            default: {
                throw new IllegalArgumentException("There is no variable with name " + name.getName());
            }
        }
    }

    @Override
    public void endVisit(ASTMultExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.multiply(right));
    }

    @Override
    public void endVisit(ASTDivideExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.divide(right, RoundingMode.HALF_EVEN));
    }

    @Override
    public void endVisit(ASTModuloExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.remainder(right));
    }

    @Override
    public void endVisit(ASTPlusExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.add(right));
    }

    @Override
    public void endVisit(ASTMinusExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.subtract(right));
    }

    @Override
    public void endVisit(ASTLiteralExpression node) {
        checkArgument(values.containsKey(node.getExtLiteral()));

        values.put(node, values.get(node.getExtLiteral()));
    }

    @Override
    public void endVisit(ASTPowExpression node) {
        checkArgument(values.containsKey(node.getExpression()));

        BigDecimal value = values.get(node.getExpression());

        values.put(node, value.pow(node.getAmount().getValue()));
    }

    @Override
    public void endVisit(ASTRandomNumberExpression node) {
        checkArgument(values.containsKey(node.getDice()));
        checkArgument(values.containsKey(node.getEyes()));
        checkArgument(values.get(node.getDice()).intValueExact() <= MAX_DICE, "You can at most roll " + MAX_DICE + " dice.");
        checkArgument(values.get(node.getEyes()).intValueExact() <= MAX_EYE, "A die can have at most " + MAX_EYE + " eyes.");
        checkArgument(values.get(node.getDice()).intValueExact() > 0, "You need to roll at least one die");
        checkArgument(values.get(node.getEyes()).intValueExact() > 0, "A die needs to have at least one eye");

        int dice = values.get(node.getDice()).intValueExact();
        int eyes = values.get(node.getEyes()).intValueExact();
        BigDecimal result = BigDecimal.ZERO;

        for (int i = 0; i < dice; ++i) {
            //+1 since rng returns values from [0,eyes)
            result = result.add(BigDecimal.valueOf(rng.nextInt(eyes) + 1));
        }

        values.put(node, result);
    }

    @Override
    public void endVisit(ASTAbsExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, value.abs());
    }

    @Override
    public void endVisit(ASTACosExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.acos(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTASinExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.asin(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTATanExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.atan(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTCeilExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.ceil(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTCosExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.cos(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTFloorExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.floor(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTLogExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.log10(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTLnExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.log(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTMaxExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.max(right));
    }

    @Override
    public void endVisit(ASTMinExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.min(right));
    }

    @Override
    public void endVisit(ASTSinExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.sin(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTSqrtExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.sqrt(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTTanExpression node) {
        checkArgument(values.containsKey(node.getArgument().getExpression()));

        BigDecimal value = values.get(node.getArgument().getExpression());

        values.put(node, BigDecimal.valueOf(Math.tan(value.doubleValue())));
    }

    @Override
    public void endVisit(ASTExtLiteral node) {
        checkArgument(values.containsKey(node.getSignedLiteral()));

        values.put(node, values.get(node.getSignedLiteral()));
    }
}