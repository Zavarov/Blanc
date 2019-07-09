package vartas.discord.bot.io.permission;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Files;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.JDALogger;
import org.slf4j.Logger;
import vartas.discord.bot.io.permission._ast.ASTPermissionArtifact;
import vartas.discord.bot.io.permission.prettyprint.PermissionPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.Semaphore;

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

/**
 * This class grants access to the internal permission file for Discord users.
 */
public class PermissionConfiguration {
    /**
     * The logger for any error messages.
     */
    protected Logger log = JDALogger.getLog(this.getClass().getSimpleName());
    /**
     * The mutex that ensures that only a single thread is allowed to modify this permission file.
     */
    protected Semaphore mutex = new Semaphore(1);
    /**
     * The respective file containing the permissions.
     */
    protected File reference;
    /**
     * The permissions for each user.
     */
    protected Multimap<Long, PermissionType> permissions;

    /**
     * Creats a new instance and extracts the data from the AST node.
     * The reason why we don't stick to the AST tree is because we are allowed to modify the entries of the
     * permission file.
     * @param ast the AST instance of the permission file.
     * @param reference the target file where any update will be written into.
     */
    public PermissionConfiguration(ASTPermissionArtifact ast, File reference){
        this.reference = reference;
        this.permissions = HashMultimap.create(ast.getPermissions());

        update();
    }

    /**
     * @param user the user instace.
     * @return true if the user has the Root rank.
     */
    public boolean hasRootRank(User user){
        return permissions.get(user.getIdLong()).contains(PermissionType.ROOT);
    }

    /**
     * @param user the user instace.
     * @return true if the user has the Developer rank.
     */
    public boolean hasDeveloperRank(User user){
        return permissions.get(user.getIdLong()).contains(PermissionType.DEVELOPER);
    }

    /**
     * @param user the user instace.
     * @return true if the user has the Reddit rank.
     */
    public boolean hasRedditRank(User user){
        return permissions.get(user.getIdLong()).contains(PermissionType.REDDIT);
    }

    /**
     * Adds the Root rank to a user.
     * @param user the user instace.
     */
    public void addRootRank(User user){
        mutex.acquireUninterruptibly();
        permissions.put(user.getIdLong(), PermissionType.ROOT);
        mutex.release();

        update();
    }


    /**
     * Adds the Developer to from a user.
     * @param user the user instace.
     */
    public void addDeveloperRank(User user){
        mutex.acquireUninterruptibly();
        permissions.put(user.getIdLong(), PermissionType.DEVELOPER);
        mutex.release();

        update();
    }


    /**
     * Adds the Reddit rank to a user.
     * @param user the user instace.
     */
    public void addRedditRank(User user){
        mutex.acquireUninterruptibly();
        permissions.put(user.getIdLong(), PermissionType.REDDIT);
        mutex.release();

        update();
    }

    /**
     * Removes the Root rank from a user.
     * @param user the user instace.
     */
    public void removeRootRank(User user){
        mutex.acquireUninterruptibly();
        permissions.remove(user.getIdLong(), PermissionType.ROOT);
        mutex.release();

        update();
    }

    /**
     * Removes the Developer rank from a user.
     * @param user the user instace.
     */
    public void removeDeveloperRank(User user){
        mutex.acquireUninterruptibly();
        permissions.remove(user.getIdLong(), PermissionType.DEVELOPER);
        mutex.release();

        update();
    }

    /**
     * Removes the Reddit rank from a user.
     * @param user the user instace.
     */
    public void removeRedditRank(User user){
        mutex.acquireUninterruptibly();
        permissions.remove(user.getIdLong(), PermissionType.REDDIT);
        mutex.release();

        update();
    }
    public Multimap<Long, PermissionType> getPermissions(){
        return Multimaps.unmodifiableMultimap(permissions);
    }
    /**
     * Stores the current permissions on the disc and overwrites any previous file.
     */
    private void update(){
        try {
            mutex.acquireUninterruptibly();
            reference.getParentFile().mkdirs();
            reference.createNewFile();
            String content = new PermissionPrettyPrinter(new IndentPrinter()).prettyprint(this);
            Files.writeToTextFile(new StringReader(content), reference);
        }catch(IOException e){
            log.error(e.getMessage());
        }finally {
            mutex.release();
        }
    }
}
