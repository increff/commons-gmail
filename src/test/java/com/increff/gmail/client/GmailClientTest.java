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

package com.increff.gmail.client;

import com.increff.gmail.GmailClient;
import com.increff.gmail.Credential;
import com.increff.gmail.Email;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.*;
import java.text.ParseException;
import java.util.List;


public class GmailClientTest {


    public static GmailClient gmailClient;
    public static Credential gmailCredential = new Credential();

    public static final String HOST = "imap.googlemail.com";
    public static final int PORT = 993;


    @BeforeClass
    public static void loadClient() throws MessagingException {
        gmailCredential.setHost(HOST);
        gmailCredential.setPort(PORT);
        gmailCredential.setUser("tsetmailservice@gmail.com");
        gmailCredential.setPassword("@123456qw");

        gmailClient = new GmailClient(gmailCredential);
    }

    @Test
    public void testUnreadEmails() throws IOException, MessagingException, ParseException {
        List<Email> emails = gmailClient.getAllUnreadEmails("aman.singh@nextscm.com");
        System.out.println("Total mails to read: " + emails.size());
//        printFileContents(emails.get(1).getAttchFiles().get(0));
    }

    private void printFileContents(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

    }


}
