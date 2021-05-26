/*
 * Copyright (c) 2021. Increff
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.increff.gmail;

import org.apache.commons.io.FileUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.increff.gmail.GmailClient.PATH;

public class GmailClientHelper {

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );

    // Number of days for which last emails to be searched upon
    public static int DAYS_EPOCH = 7;

    // Underlined protocol can be POP3 or IMAP.
    public static final String PROTOCOL = "imaps";

    // We are only interested in INBOX inboxFolder. This is made a constant for now
    public static final String MAIL_FOLDER = "INBOX";

    public static Folder getInboxFolder(Credential gmailCredentials) throws MessagingException {
        Session session = Session.getDefaultInstance(new Properties());

        Store store = session.getStore(PROTOCOL);
        store.connect(gmailCredentials.getHost(), gmailCredentials.getPort(),
                gmailCredentials.getUser(), gmailCredentials.getPassword());

        return store.getFolder(MAIL_FOLDER);
    }

    public static Email toEmail(Message message) throws MessagingException, IOException {
        checkAndCreateDir(PATH);
        Email email = new Email();
        List<File> files = new ArrayList<>();
        email.setMessageNumber(message.getMessageNumber());
        email.setFrom(((InternetAddress) ((message.getFrom()[0]))).getAddress());
        email.setSubject(message.getSubject());
        email.setContentType(message.getContentType());
        email.setDate(message.getSentDate().toString());
        if (email.getContentType().contains("multipart")) {
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    File file = new File(PATH + "/" + part.getFileName());
                    FileUtils.copyInputStreamToFile(part.getInputStream(), file);
                    files.add(file);
                } else {
                    email.setBody(part.getContent().toString());
                }
            }
            email.setAttchFiles(files);
        } else {
            email.setBody(message.getContent().toString());
        }
        return email;
    }

    private static void checkAndCreateDir(String path) throws IOException {
        File dir = new File(path);
        if(!(dir.exists()))
            dir.mkdir();
    }
}
