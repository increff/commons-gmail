# commons-gmail
Commons gmail library is created keeping in mind instances where we might need to consume a file that comes in the mail.

## Overview
There are 2 APIs that are being supported right now.

1. Reading the next or all emails from a given address
   `getNextUnreadEmail(String from);`
   `getAllUnreadEmails(String from);`

2. Marking a mail read or unread
   `markEmail(Email email, boolean read);`

In the future we can think of supporting multiple use-cases as they arise.
commons-lang provides a host of helper utilities for dealing with Collections, DateTime formats and Files among other things

## License
Copyright (c) Increff

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
or implied. See the License for the specific language governing permissions and limitations under
the License.
