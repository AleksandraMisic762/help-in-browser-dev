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
package de.rwthaachen.wzl.gt.nbm.nbhelp.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import javax.swing.JOptionPane;
import org.apache.lucene.queryparser.classic.ParseException;

public class HelpPageSearch {

    private static final StandardAnalyzer analyzer = new StandardAnalyzer();

    private final IndexReader reader;
    private final IndexSearcher searcher;
    private TopScoreDocCollector collector;

    private static String indexLocation = null;

    public HelpPageSearch() throws IOException {
        indexLocation = HelpPageIndexer.getIndexLocation();
        File indexDir = new File(indexLocation);
        if (!indexDir.exists() || indexDir.list().length == 0) {
            JOptionPane.showMessageDialog(null, "No index available!");
        }
        reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation).toPath()));
        searcher = new IndexSearcher(reader);
    }

    public Document[] search(String searchTerm) {
        try {
            Query q = new QueryParser("contents", analyzer).parse(searchTerm);
            collector = TopScoreDocCollector.create(30, 50);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            Document[] result = new Document[hits.length];
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                result[i] = d;
            }
            return result;
        } catch (IOException | ParseException e) {
            JOptionPane.showMessageDialog(null, "Error searching " + searchTerm + " : " + e.getMessage());
        }
        return null;
    }

    public static String getIndexLocation() {
        return indexLocation;
    }

    public static void setIndexLocation(String indexLocation) {
        HelpPageSearch.indexLocation = indexLocation;
    }

}
