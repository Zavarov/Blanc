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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.monticore.cd.cd4analysis.CD4AnalysisMill;
import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnitBuilder;
import de.monticore.cd.cd4analysis._ast.ASTCDDefinitionBuilder;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbolSurrogate;
import de.monticore.cd.cd4code.CD4CodeMill;
import de.monticore.cd.cd4code._symboltable.ICD4CodeArtifactScope;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.Joiners;
import de.se_rwth.commons.Splitters;
import zav.discord.blanc.command._ast.ASTCommand;
import zav.discord.blanc.command._ast.ASTCommandArtifact;
import zav.discord.blanc.command._visitor.CommandVisitor;
import zav.mc.cd4code._symboltable.CD4CodeGlobalScope;
import zav.mc.cd4code._symboltable.CD4CodeSymbolTableCreatorDelegator;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandArtifactCreator extends AbstractCreator<ASTCommandArtifact, ASTCDCompilationUnit> implements CommandVisitor {
    private ASTCDCompilationUnitBuilder cdCompilationUnitBuilder;
    private ASTCDDefinitionBuilder cdDefinitionBuilder;
    private final CD4CodeGlobalScope globalScope;

    public CommandArtifactCreator(GlobalExtensionManagement glex, CD4CodeGlobalScope globalScope){
        super(glex);
        this.globalScope = globalScope;
    }

    @Override
    public ASTCDCompilationUnit decorate(ASTCommandArtifact ast) {
        cdCompilationUnitBuilder = createCDCompilationUnitBuilder(ast);
        cdDefinitionBuilder = createCDDefinitionBuilder(ast);

        ast.accept(this);

        ASTCDCompilationUnit cdCompilationUnit = cdCompilationUnitBuilder.build();
        //A symbol table is required for generation
        createSymbolTable(ast, cdCompilationUnit);
        return cdCompilationUnit;
    }

    @Override
    public void visit(ASTCommand ast){
        cdDefinitionBuilder.addCDClass(new CommandCreator(glex).decorate(ast));
    }

    @Override
    public void endVisit(ASTCommandArtifact ast){
        cdCompilationUnitBuilder.setCDDefinition(cdDefinitionBuilder.build());
    }

    private ASTCDDefinitionBuilder createCDDefinitionBuilder(ASTCommandArtifact ast){
        return CD4AnalysisMill.cDDefinitionBuilder()
                .setName(ast.isPresentGroup() ? ast.getGroup().getQName() : "");
    }

    private void createSymbolTable(ASTCommandArtifact ast, ASTCDCompilationUnit cdCompilationUnit){
        CDDefinitionSymbol commandSymbol = globalScope.resolveCDDefinition("zav.discord.blanc.command.Command").orElseThrow();
        CD4CodeSymbolTableCreatorDelegator stc = new CD4CodeSymbolTableCreatorDelegator(globalScope);
        ICD4CodeArtifactScope artifactScope = stc.createFromAST(cdCompilationUnit);

        for(CDDefinitionSymbol cdDefinition : artifactScope.getLocalCDDefinitionSymbols()) {
            //Doesn't matter what we name it. As long as it's not a qualified name.
            cdDefinition.getAstNode().setName("CommandArtifact");
            //The super classes are declared in a different scope/class diagram
            for (CDTypeSymbol cdType : cdDefinition.getTypes())
                cdType.setSuperClass(new CDTypeSymbolSurrogate(cdType.getSuperClass().getName(), commandSymbol.getSpannedScope()));
        }
    }

    private ASTCDCompilationUnitBuilder createCDCompilationUnitBuilder(ASTCommandArtifact ast){
        return CD4AnalysisMill.cDCompilationUnitBuilder()
                .setPackageList(ast.getPackageList())
                .addAllMCImportStatements(getDistinctImports(ast));
    }

    private Set<ASTMCImportStatement> getDistinctImports(ASTCommandArtifact ast){
        Set<String> imports = Sets.newHashSet();
        imports.addAll(ast.getImportList().stream().map(ASTMCImportStatement::getQName).collect(Collectors.toList()));
        imports.addAll(getArchitectureImports());
        imports.addAll(getCommandImports());

        return imports.stream().map(this::createImport).collect(Collectors.toSet());
    }

    private ASTMCImportStatement createImport(String qualifiedImport){
        return createImport(Splitters.DOT.split(qualifiedImport));
    }

    private ASTMCImportStatement createImport(Iterable<String> importParts){
        return createImport(Lists.newArrayList(importParts));
    }

    private ASTMCImportStatement createImport(List<String> importParts){
        return createImport(CD4CodeMill.mCQualifiedNameBuilder().setPartsList(importParts).build());
    }

    private ASTMCImportStatement createImport(ASTMCQualifiedName ast){
        return CD4CodeMill.mCImportStatementBuilder().setMCQualifiedName(ast).build();
    }

    private Collection<String> getArchitectureImports(){
        return getImports(globalScope.resolveCDDefinition("zav.discord.blanc.Architecture").orElseThrow());
    }

    private Collection<String> getCommandImports(){
        return getImports(globalScope.resolveCDDefinition("zav.discord.blanc.command.Command").orElseThrow());
    }

    private Collection<String> getImports(CDDefinitionSymbol symbol){
        Collection<String> imports = Lists.newArrayList();

        imports.addAll(symbol.getImports());
        for(CDTypeSymbol type : symbol.getTypes())
            imports.add(Joiners.DOT.join(symbol.getPackageName(), symbol.getName(), type.getName()));

        return imports;
    }
}