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
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class CreateIndexDialogController {

    private final CreateIndexDialog dialog;
    private final JFileChooser fileChooser;
    private String indexLocation = "";
    private String helpPageLocation = "";

    public CreateIndexDialogController() throws IOException {
        
        dialog = new CreateIndexDialog(null, true);
        prepareView();
        addListeners();
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dialog.setVisible(true);
    }

    private void addListeners() {
        dialog.getBtnHelpPagesLocationBrowse().addActionListener((ActionEvent e) -> {
            fileChooser.setCurrentDirectory(new File("C:\\Users\\HP"));

            int returnVal = fileChooser.showOpenDialog(fileChooser);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                helpPageLocation = file.getAbsolutePath();
                dialog.getTxtHelpPagesLocation().setText(helpPageLocation);
            }
        });

        dialog.getBtnIndexLocationBrowse().addActionListener((ActionEvent e) -> {
            fileChooser.setCurrentDirectory(new File("C:\\Users\\HP"));

            int returnVal = fileChooser.showOpenDialog(fileChooser);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                indexLocation = file.getAbsolutePath();
                dialog.getTxtIndexLocation().setText(indexLocation);
            }
        });

        dialog.getBtnCreate().addActionListener((ActionEvent e) -> {
            try {
                String helpPagesLocation = dialog.getTxtHelpPagesLocation().getText().trim();
                String indexLocation1 = dialog.getTxtIndexLocation().getText().trim();
                if (indexLocation1.equals("") || helpPagesLocation.equals("")) {
                    JOptionPane.showMessageDialog(dialog, "No index address provided");
                    return;
                }
                HelpPageIndexer.createIndex(helpPagesLocation, indexLocation1);
                JOptionPane.showMessageDialog(dialog, "Index successfully created");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dialog, "Unable to create index at given address");
                dialog.dispose();
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
