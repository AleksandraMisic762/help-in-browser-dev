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

import de.rwthaachen.wzl.gt.nbm.nbhelp.HelpDisplayer;
import de.rwthaachen.wzl.gt.nbm.nbhelp.search.HelpPageSearch;
import de.rwthaachen.wzl.gt.nbm.nbhelp.search.views.SearchJavaHelpPagesDialog;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.apache.lucene.document.Document;

public class SearchJavaHelpPagesDialogController {

    private final SearchJavaHelpPagesDialog dialog;

    private final HelpPageSearch indexSearch;
    private Document[] searchResults = null;

    public SearchJavaHelpPagesDialogController() throws IOException {
        indexSearch = new HelpPageSearch();
        dialog = new SearchJavaHelpPagesDialog(null, true);
        prepareView();
        addListeners();
        dialog.getTblResults().setVisible(false);
        dialog.setVisible(true);
    }

    private void addListeners() {
        dialog.getBtnSearch().addActionListener((ActionEvent e) -> {
            String searchTerm = dialog.getTxtSearchTerm().getText().trim();
            if (searchTerm.equals("")) {
                JOptionPane.showMessageDialog(dialog, "Search term input required");
                return;
            }
            searchResults = indexSearch.search(searchTerm);
            ((ResultsTableModel) dialog.getTblResults().getModel()).setTableData(searchResults);
            dialog.getTblResults().setVisible(true);
        });
    }

    private void prepareView() {
        dialog.setTitle("Search JavaHelp Pages");

        dialog.getTblResults().setModel(new ResultsTableModel(searchResults));
        dialog.getTblResults().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        dialog.getTblResults().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (dialog.getTblResults().getSelectedRow() > -1) {
                    String location = searchResults[dialog.getTblResults().getSelectedRow()].get("path");
                    location = location.substring(location.lastIndexOf("org"), location.length());
                    HelpDisplayer.showPage(location);
                    dialog.getTblResults().clearSelection();
                }
            }
        });
    }

}

class ResultsTableModel extends AbstractTableModel {

    private Document[] tableDataResults;
    private final String[] columnLabels = {"Search Results"};

    public ResultsTableModel(Document[] tableDataResults) {
        this.tableDataResults = tableDataResults;
    }

    @Override
    public int getRowCount() {
        return (tableDataResults == null) ? 0 : tableDataResults.length;
    }

    @Override
    public int getColumnCount() {
        return columnLabels.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnLabels[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (tableDataResults == null) {
            return null;
        }

        Document value = tableDataResults[rowIndex];

        switch (columnIndex) {
            case 0:
                return value.get("title");
            default:
                return null;
        }
    }

    public void setTableData(Document[] tableData) {
        tableDataResults = tableData;
        fireTableDataChanged();
    }
}
