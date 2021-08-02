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

public class HelpPageSearch {

    String indexLocation = "D:\\lucene-index-1";

    private static StandardAnalyzer analyzer = new StandardAnalyzer();

    private IndexReader reader;
    private IndexSearcher searcher;
    private TopScoreDocCollector collector;

    public HelpPageSearch() throws IOException {
        reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation).toPath()));
        searcher = new IndexSearcher(reader);
        collector = TopScoreDocCollector.create(5, 30);
    }

    public ScoreDoc[] search(String searchTerm) {
        try {
            Query q = new QueryParser("contents", analyzer).parse(searchTerm);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            // 4. display results
            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
            }
            return hits;
        } catch (Exception e) {
            System.out.println("Error searching " + searchTerm + " : " + e.getMessage());
        }
        return null;
    }

}
