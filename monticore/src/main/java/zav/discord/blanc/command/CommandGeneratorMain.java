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

package zav.discord.blanc.command;

import de.monticore.io.paths.ModelPath;
import zav.discord.blanc.command._ast.ASTCommandArtifact;
import zav.discord.blanc.command._symboltable.CommandGlobalScope;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandGeneratorMain {
    private static Path MODELS_PATH;
    private static Path TEMPLATES_PATH;
    private static Path CLASSES_PATH;
    private static Path SOURCES_PATH;
    private static Path OUTPUT_PATH;
    private static List<String> MODELS;

    private static CommandGlobalScope GLOBAL_SCOPE;
    private static ModelPath MODEL_PATH;
    private static ModelPath CLASS_PATH;

    public static void main(String[] args){
        processArguments(args);

        buildModelPath();
        buildClassPath();
        buildGlobalScope();

        generateCommands();
        generateCommandBuilder();
    }

    private static void processArguments(String[] args){
        assert args.length > 5;

        MODELS_PATH = Paths.get(args[0]);
        CLASSES_PATH = Paths.get(args[1]);
        TEMPLATES_PATH = Paths.get(args[2]);
        SOURCES_PATH = Paths.get(args[3]);
        OUTPUT_PATH = Paths.get(args[4]);
        MODELS = Arrays.asList(Arrays.copyOfRange(args, 5, args.length));
    }

    private static void buildModelPath(){
        assert MODELS_PATH != null;

        MODEL_PATH = new ModelPath();

        if(MODELS_PATH.toFile().exists())
            MODEL_PATH.addEntry(MODELS_PATH);
    }

    private static void buildClassPath(){
        assert CLASSES_PATH != null;

        CLASS_PATH = new ModelPath();

        if(MODELS_PATH.toFile().exists())
            CLASS_PATH.addEntry(CLASSES_PATH);
    }

    private static void buildGlobalScope(){
        assert MODEL_PATH != null;
        GLOBAL_SCOPE = new CommandGlobalScope(MODEL_PATH, "cmd");
    }

    private static void generateCommands(){
        assert MODELS != null;

        MODELS.forEach(CommandGeneratorMain::generateCommand);
    }

    private static void generateCommand(@Nonnull String model){
        assert GLOBAL_SCOPE != null;
        assert MODEL_PATH != null;
        assert CLASSES_PATH != null;
        assert TEMPLATES_PATH != null;
        assert SOURCES_PATH != null;
        assert OUTPUT_PATH != null;

        ASTCommandArtifact ast = parse(GLOBAL_SCOPE, MODEL_PATH, model);
        CommandGenerator.generate(CLASSES_PATH, TEMPLATES_PATH, SOURCES_PATH, OUTPUT_PATH, ast);
    }

    private static void generateCommandBuilder(){
        assert MODELS != null;
        assert GLOBAL_SCOPE != null;
        assert MODEL_PATH != null;
        assert CLASSES_PATH != null;
        assert TEMPLATES_PATH != null;
        assert SOURCES_PATH != null;
        assert OUTPUT_PATH != null;

        List<ASTCommandArtifact> asts = new ArrayList<>();

        for(String model : MODELS)
            asts.add(parse(GLOBAL_SCOPE, MODEL_PATH, model));

        CommandBuilderGenerator.generate(CLASSES_PATH, TEMPLATES_PATH, SOURCES_PATH, OUTPUT_PATH, asts);
    }

    public static ASTCommandArtifact parse(
            @Nonnull CommandGlobalScope scope,
            @Nonnull ModelPath modelPath,
            @Nonnull String qualifiedName
    )
    {
        List<ASTCommandArtifact> models = scope.getModelLoader().loadModelsIntoScope(qualifiedName, modelPath, scope);

        if(models.size() == 1)
            return models.get(0);
        else
            throw new IllegalArgumentException();
    }
}
