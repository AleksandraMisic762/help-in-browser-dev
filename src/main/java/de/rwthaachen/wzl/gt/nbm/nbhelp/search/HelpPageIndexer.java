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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.ArrayList;

public class HelpPageIndexer {

    private static final StandardAnalyzer analyzer = new StandardAnalyzer();

    private final IndexWriter writer;
    private final ArrayList<File> queue = new ArrayList<File>();

    private static String indexLocation = null;

    public static void createIndex(String helpPagesAddress, String indexAddress) throws IOException {
        indexLocation = indexAddress;

        HelpPageIndexer indexer = null;
        try {
            indexer = new HelpPageIndexer(indexLocation);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        try {
            indexer.indexFileOrDirectory(helpPagesAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexer.closeIndex();
    }

    HelpPageIndexer(String indexDir) throws IOException {
        FSDirectory dir = FSDirectory.open(new File(indexDir).toPath());

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        writer = new IndexWriter(dir, config);
    }

    private void indexFileOrDirectory(String fileName) throws IOException {
        addFiles(new File(fileName));

        for (File f : queue) {
            FileReader fr = null;
            try {
                Document doc = new Document();

                fr = new FileReader(f);
                BufferedReader titleReader = new BufferedReader(new FileReader(f));
                doc.add(new TextField("contents", fr));
                
                String title = titleReader.readLine();
                while (!title.contains("<h1>")) {
                    title = titleReader.readLine();
                }
                title = title.trim();
                title = title.replace("<h1>", "");
                title = title.replace("</h1>", "");
                
                doc.add(new StringField("title", title, Field.Store.YES));
                doc.add(new StringField("path", f.getPath(), Field.Store.YES));
                doc.add(new StringField("filename", f.getName(), Field.Store.YES));

                writer.addDocument(doc);
            } catch (IOException e) {
                System.out.println("Could not add: " + f);
            } finally {
                fr.close();
            }
        }
        queue.clear();
    }

    private void addFiles(File file) {

        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                addFiles(f);
            }
        } else {
            String filename = file.getName().toLowerCase();

            if (filename.endsWith(".htm") || filename.endsWith(".html") || filename.endsWith(".txt")) {
                queue.add(file);
            }
        }
    }

    private void closeIndex() throws IOException {
        writer.close();
    }

    public static String getIndexLocation() {
        return indexLocation;
    }
}
