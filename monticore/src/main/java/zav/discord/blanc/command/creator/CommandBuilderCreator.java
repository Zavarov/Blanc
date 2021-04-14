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

package zav.discord.blanc.command.creator;

import com.google.common.base.Preconditions;
import de.monticore.cd.cd4analysis.CD4AnalysisMill;
import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDFieldSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbolSurrogate;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.monticore.utils.Names;
import de.se_rwth.commons.Joiners;
import org.apache.commons.lang3.StringUtils;
import zav.discord.blanc.command._ast.*;
import zav.discord.blanc.command._visitor.CommandVisitor;
import zav.mc.cd4code.CDGeneratorHelper;
import zav.mc.cd4code._symboltable.CD4CodeGlobalScope;
import zav.mc.cd4code._symboltable.CD4CodeSymbolTableCreatorDelegator;
import zav.discord.blanc.command._ast.ASTRestriction;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandBuilderCreator extends AbstractCreator<List<ASTCommandArtifact>, ASTCDCompilationUnit> implements CD4CodeInheritanceVisitor {
    private static final String CLASS_DIAGRAM = "zav.discord.blanc.monticore.MontiCoreCommandBuilder";
    private static final String CLASS = "MontiCoreCommandBuilder";
    private static final String MODEL = Joiners.DOT.join(CLASS_DIAGRAM, CLASS);
    private static final String COMMAND_BUILDER = "zav.discord.blanc.command.Command.CommandBuilder";
    private final CD4CodeGlobalScope globalScope;

    private Map<ASTCommandArtifact, ASTCDCompilationUnit> commands;

    private ASTCDCompilationUnit cdCompilationUnit;
    private ASTCDDefinition cdDefinition;


    public CommandBuilderCreator(GlobalExtensionManagement glex, CD4CodeGlobalScope globalScope){
        super(glex);
        this.globalScope = globalScope;
    }

    @Override
    public ASTCDCompilationUnit decorate(List<ASTCommandArtifact> commands) {
        processArguments(commands);

        createDefinition();
        createCommandBuilder();
        createCompilationUnit();
        createSymbolTable();

        //The Map<...> attribute can't be resolved in the local scope.
        rebindCommands();
        loadModelImports();
        includeCommands();

        return cdCompilationUnit;
    }

    private void processArguments(List<ASTCommandArtifact> commands){
        CommandArtifactCreator creator = new CommandArtifactCreator(glex, globalScope);
        this.commands = commands.stream().collect(Collectors.toMap(Function.identity(), creator::decorate));
    }

    private void createDefinition(){
        cdDefinition = CD4AnalysisMill.cDDefinitionBuilder().setName("GeneratedMontiCoreCommandBuilder").build();
    }

    private void createCommandBuilder(){
        ASTCDType cdBuilder = globalScope.resolveCDType(MODEL).orElseThrow().getAstNode().deepClone();
        cdBuilder.accept(getRealThis());
    }

    private void createCompilationUnit(){
        ASTCDCompilationUnitBuilder builder = CD4AnalysisMill.cDCompilationUnitBuilder();

        builder.setPackageList(Arrays.asList("zav","discord","blanc","monticore"));
        builder.setCDDefinition(cdDefinition);

        for(ASTCDCompilationUnit command : commands.values())
                builder.addAllMCImportStatements(command.getMCImportStatementList());

        cdCompilationUnit = builder.build();
    }

    private void createSymbolTable(){
        CD4CodeSymbolTableCreatorDelegator stc = new CD4CodeSymbolTableCreatorDelegator(globalScope);
        stc.createFromAST(cdCompilationUnit);
    }

    private void rebindCommands(){
        CDFieldSymbol commands = cdDefinition.getSpannedScope().resolveCDFieldDown(Joiners.DOT.join(CLASS,"commands")).orElseThrow();
        commands.setType(new CDTypeSymbolSurrogate(commands.getType().getName(), globalScope.resolveCDDefinition("java.util.Map").orElseThrow().getSpannedScope()));
    }

    private void loadModelImports(){
        CDDefinitionSymbol definitionSymbol = this.cdDefinition.getSymbol();
        String preconditions = Names.getQualifiedName(
                Preconditions.class.getCanonicalName(),
                Preconditions.class.getSimpleName()
        );

        definitionSymbol.addImport(preconditions);
        for(String cdImport : globalScope.resolveCDDefinition(CLASS_DIAGRAM).orElseThrow().getImports())
            definitionSymbol.addImport(cdImport);
    }

    private void includeCommands(){
        CommandLoader commandLoader = new CommandLoader();
        for(ASTCommandArtifact ast : commands.keySet())
            ast.accept(commandLoader);
    }

    @Override
    public void visit(ASTCDConstructor ast){
        glex.replaceTemplate(
                CDGeneratorHelper.CONSTRUCTOR_HOOK,
                ast,
                new TemplateHookPoint(
                        "command.builder.Constructor",
                        ast,
                        commands.keySet().stream().map(ASTCommandArtifact::getCommandList).flatMap(Collection::stream).collect(Collectors.toList())
                )
        );
    }

    @Override
    public void visit(ASTCDMethod ast){
        String templateName = Joiners.DOT.join("command","builder", StringUtils.capitalize(ast.getName()));

        glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, ast, new TemplateHookPoint(templateName, ast));
    }

    @Override
    public void visit(ASTCDClass ast){
        cdDefinition.addCDClass(ast);
    }

    private class CommandLoader implements CommandVisitor {
        private final MCFullGenericTypesPrettyPrinter printer = new MCFullGenericTypesPrettyPrinter(new IndentPrinter());
        private final ASTCDField commands;
        private final ASTCDField resolver;
        private String commandPackage;
        private String commandGroup;
        private String commandClass;
        private String command;
        private Map<ASTParameter, String> parameters;
        private boolean requiresGuild;

        public CommandLoader(){
            commands = globalScope.resolveCDField(Joiners.DOT.join(MODEL, "commands")).map(CDFieldSymbol::getAstNode).orElseThrow();
            resolver = globalScope.resolveCDField(Joiners.DOT.join(COMMAND_BUILDER, "typeResolver")).map(CDFieldSymbol::getAstNode).orElseThrow();
        }

        @Override
        public void visit(ASTCommandArtifact ast){
            commandPackage = Joiners.DOT.join(ast.getPackageList());
            commandGroup = ast.isPresentGroup() ? ast.getGroup().getQName() : "";
        }

        @Override
        public void visit(ASTCommand ast){
            command = commandGroup.isEmpty() ? ast.getName() : Joiners.DOT.join(commandGroup, ast.getName());
            requiresGuild = ast.getRestrictionList().contains(ASTRestriction.GUILD);
            parameters = new LinkedHashMap<>();
        }

        @Override
        public void visit(ASTClassAttribute ast){
            commandClass = Joiners.DOT.join(commandPackage, CDGeneratorHelper.FACTORY_PACKAGE, ast.getName() + "Factory");
        }

        @Override
        public void visit(ASTParameter ast){
            parameters.put(ast, printer.prettyprint(ast.getMCType()));
        }

        @Override
        public void endVisit(ASTCommand ast){
            glex.replaceTemplate(
                    "hook.Command",
                    ast,
                    new TemplateHookPoint(
                            "command.builder.CommandLoader",
                            commands,
                            resolver,
                            command,
                            commandClass,
                            parameters,
                            requiresGuild
                    )
            );
        }
    }
}
