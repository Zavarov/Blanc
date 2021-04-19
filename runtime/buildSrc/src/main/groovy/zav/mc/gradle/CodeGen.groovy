package zav.mc.gradle

import zav.discord.blanc.command.CommandGeneratorMain
import java.nio.file.Path
import java.nio.file.Paths

Path CURRENT = Paths.get("").toAbsolutePath()  //runtime -> buildSrc
Path PARENT = CURRENT.getParent()                   //runtime

Path MODELS_PATH = Paths.get("src", "main", "models")
Path CLASSES_PATH = Paths.get("build", "codegen")
Path TEMPLATE_PATH = Paths.get("build", "codegen")
Path SOURCES_DIRECTORY = Paths.get("src", "main", "java")
Path TARGET_DIRECTORY = Paths.get("build", "src", "main", "java")

String[] args = new String[]{
        CURRENT.resolve(MODELS_PATH),       //runtime -> buildSrc -> src -> main -> models
        CURRENT.resolve(CLASSES_PATH),      //runtime -> buildSrc -> build -> codegen
        CURRENT.resolve(TEMPLATE_PATH),     //runtime -> buildSrc -> build -> codegen
        PARENT.resolve(SOURCES_DIRECTORY),  //runtime -> src -> main -> java
        CURRENT.resolve(TARGET_DIRECTORY),  //runtime -> buildSrc -> build -> src -> main -> java
        "zav.discord.blanc.command.base.Base",
        "zav.discord.blanc.command.developer.Developer",
        "zav.discord.blanc.command.legacy.Legacy",
        "zav.discord.blanc.command.mod.Mod",
        "zav.discord.blanc.command.reddit.Reddit"
}

CommandGeneratorMain.main(args)