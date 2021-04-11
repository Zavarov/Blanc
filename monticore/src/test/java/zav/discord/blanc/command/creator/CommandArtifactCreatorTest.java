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

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zav.discord.blanc.command.BasicCommandTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandArtifactCreatorTest extends BasicCommandTest {
    CommandArtifactCreator cmd2cd;
    ASTCDCompilationUnit cdCompilationUnit;

    @BeforeEach
    public void setUp(){
        parseCommand("kick", "zav.discord.blanc.command.Guild");
        cmd2cd = new CommandArtifactCreator(new GlobalExtensionManagement(), cdGlobalScope);

        cdCompilationUnit = cmd2cd.decorate(cmdArtifact);
    }

    @Test
    public void testResolve(){
        assertThat(cdCompilationUnit.getPackageList()).containsExactly("zav", "discord", "blanc", "command");

        CDDefinitionSymbol cdDefinition = cdCompilationUnit.getCDDefinition().getSymbol();
        CDTypeSymbol cdClass = cdDefinition.getSpannedScope().resolveCDType("KickClass").orElseThrow();
        cdClass.getSpannedScope().resolveCDField("member").orElseThrow();
    }
}
