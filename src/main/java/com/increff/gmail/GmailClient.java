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

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.*;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.increff.gmail.GmailClientHelper.*;

public class GmailClient {

    private Credential gmailCredential;

    public static String PATH = "attachments";

    public GmailClient(Credential gmailCredential) throws MessagingException {
        this.gmailCredential = gmailCredential;
    }

    // API to get the next unread email from a sender
    public Email getNextUnreadEmail(String from) throws MessagingException, IOException, ParseException {
        List<Email> emails = getAllUnreadEmails(from);

        if (!emails.isEmpty())
            return emails.get(0);

        return null;
    }

    // API to mark the email as read/unread after consumption
    public void markEmail(int messageNumber, boolean read) throws MessagingException {
        Folder inboxFolder = GmailClientHelper.getInboxFolder(gmailCredential);
        inboxFolder.open(Folder.READ_WRITE);
        Message message = inboxFolder.getMessage(messageNumber);
        inboxFolder.setFlags(new Message[]{message}, new Flags(Flags.Flag.SEEN), read);
    }

    public List<Email> getAllUnreadEmails(String from) throws MessagingException, IOException, ParseException {
        Instant start = Instant.now();

        Folder inboxFolder = GmailClientHelper.getInboxFolder(gmailCredential);

        List<Email> emails = new ArrayList<>();
        inboxFolder.open(Folder.READ_ONLY);

        Date startDate = dateFormatter.parse(LocalDate.now().minusDays(DAYS_EPOCH).toString());

        // Set search term to start with the last week date
        SearchTerm searchTerm = new FromTerm(new InternetAddress(from));
        // Add from search to the filter
        searchTerm = new AndTerm(searchTerm, new ReceivedDateTerm(ComparisonTerm.GE, startDate));
        // Add unseen flag to the filter
        searchTerm = new AndTerm(searchTerm, new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        List<Message> messages = Arrays.stream(inboxFolder.search(searchTerm))
                .sorted(new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return Integer.compare(o1.getMessageNumber(), o2.getMessageNumber());
                }
        }).collect(Collectors.toList());

        for (Message message : messages) {
            emails.add(toEmail(message));
        }

        System.out.println("Total time elapsed: " + Duration.between(start, Instant.now()).toMillis()/1000);

        return emails;
    }

    public static void changePath(String path){
        PATH = path;
    }
}