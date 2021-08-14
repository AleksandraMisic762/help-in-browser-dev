/*
 * Copyright 2021 HP.
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
package de.rwthaachen.wzl.gt.nbm.nbhelp.search.views.controller;

import de.rwthaachen.wzl.gt.nbm.nbhelp.search.HelpPageIndexer;
import de.rwthaachen.wzl.gt.nbm.nbhelp.search.views.CreateIndexDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class CreateIndexDialogController {

    private CreateIndexDialog dialog;

    public CreateIndexDialogController() {
        dialog = new CreateIndexDialog(null, true);
        prepareView();
        addListeners();
        dialog.setVisible(true);
    }

    private void addListeners() {

        dialog.getBtnCreate().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(dialog.getTxtIndexLocation().getText().equals("")){
                        JOptionPane.showMessageDialog(dialog, "No index address provided");
                        return;
                    }
                    HelpPageIndexer.createIndex(dialog.getTxtIndexLocation().getText().trim());
                    JOptionPane.showMessageDialog(dialog, "Index successfully created");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "Unable to create index at given address");
                    dialog.dispose();
                }
                
            }
        });

        dialog.getBtnClose().addActionListener((ActionEvent e) -> {
            dialog.dispose();
        });
    }

    private void prepareView() {
        dialog.setTitle("Create Index");
    }
}
