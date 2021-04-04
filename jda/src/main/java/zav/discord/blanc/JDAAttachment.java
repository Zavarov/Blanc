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

package zav.discord.blanc;


import org.slf4j.LoggerFactory;
import zav.discord.blanc._factory.AttachmentFactory;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

public class JDAAttachment extends Attachment{
        public static Attachment create(net.dv8tion.jda.api.entities.Message.Attachment attachment){
        return AttachmentFactory.create(() -> new JDAAttachment(attachment));
    }

    private final net.dv8tion.jda.api.entities.Message.Attachment attachment;

    private JDAAttachment(net.dv8tion.jda.api.entities.Message.Attachment attachment){
        this.attachment = attachment;
    }

    @Override
    public InputStream retrieveContent() {
        try {
            return attachment.retrieveInputStream().get();
        }catch(InterruptedException | ExecutionException e){
            LoggerFactory.getLogger(this.getClass().getSimpleName()).error(e.toString());
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean isImage() {
        return attachment.isImage();
    }
}
