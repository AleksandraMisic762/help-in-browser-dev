/*
 * Copyright The string doesn't match the expected date/time/date-time format. The string to parse was: "26.04.2021.". The expected format was: "MMM d, y".
The nested reason given follows:
Unparseable date: "26.04.2021."

----
FTL stack trace ("~" means nesting-related):
	- Failed at: ${date?date?string("yyyy")}  [in template "Templates/Licenses/license-apache20.txt" at line 4, column 27]
	- Reached through: #include "${project.licensePath}"  [in template "Templates/NetBeansModuleDevelopment-files/actionListener.java" at line 24, column 1]
---- HP.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rwthaachen.wzl.gt.nbm.nbhelp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "de.rwthaachen.wzl.gt.nbm.nbhelp.HelpInBrowserAction"
)
@ActionRegistration(
        displayName = "#CTL_HelpInBrowserAction"
)
@ActionReference(path = "Menu/Help", position = 3333)
@Messages("CTL_HelpInBrowserAction=Help in Browser")
public final class HelpInBrowserAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        HelpDisplayer.showPage("");
    }
}
