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

package zav.discord.blanc.command.developer;

import zav.discord.blanc.Attachment;

import java.io.IOException;

/**
 * This command changes the avatar of the bot to the one that was attached to
 * the message that executed this command.
 */
public class AvatarCommand extends AvatarCommandTOP{
    @Override
    public void run() throws IOException {
        Attachment attachment = get$Message().getAttachments(0);
        if(attachment.isImage()){
            get$Shard().retrieveSelfUser().modifyAvatar(attachment.retrieveContent());
            get$MessageChannel().send("Avatar updated.");
        }else{
            get$MessageChannel().send("Please make sure that you've attached an image.");
        }
    }
}
