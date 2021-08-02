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

//@ServiceProvider
public class HelpPageIndexer {

    private static StandardAnalyzer analyzer = new StandardAnalyzer();

    private IndexWriter writer;
    private ArrayList<File> queue = new ArrayList<File>();

    public static void createIndex() throws IOException {
        System.out.println("Enter the path where the index will be created: (e.g. /tmp/index or c:\\temp\\index)");

        String indexLocation = null;
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));

        String s = "D:\\lucene-index-1";

        HelpPageIndexer indexer = null;
        try {
            indexLocation = s;
            indexer = new HelpPageIndexer(s);
        } catch (Exception ex) {
            System.out.println("Cannot create index..." + ex.getMessage());
            System.exit(-1);
        }

        try {
            indexer.indexFileOrDirectory("C:\\Users\\HP\\8. semestar\\SOK\\netbeans\\ide\\usersguide\\javahelp\\org\\netbeans\\modules\\usersguide");
        } catch (Exception e) {
            System.out.println("Error indexing " + s + " : " + e.getMessage());
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

        int originalNumDocs = writer.numRamDocs();
        for (File f : queue) {
            FileReader fr = null;
            try {
                Document doc = new Document();

                fr = new FileReader(f);
                doc.add(new TextField("contents", fr));
                doc.add(new StringField("path", f.getPath(), Field.Store.YES));
                doc.add(new StringField("filename", f.getName(), Field.Store.YES));

                writer.addDocument(doc);
                System.out.println("Added: " + f);
            } catch (Exception e) {
                System.out.println("Could not add: " + f);
            } finally {
                fr.close();
            }
        }

        int newNumDocs = writer.numRamDocs();
        System.out.println("");
        System.out.println("************************");
        System.out.println((newNumDocs - originalNumDocs) + " documents added.");
        System.out.println("************************");

        queue.clear();
    }

    private void addFiles(File file) {

        if (!file.exists()) {
            System.out.println(file + " does not exist.");
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                addFiles(f);
            }
        } else {
            String filename = file.getName().toLowerCase();
            
            if (filename.endsWith(".htm") || filename.endsWith(".html") || filename.endsWith(".txt")) {
                queue.add(file);
            } else {
                System.out.println("Skipped " + filename);
            }
        }
    }

    private void closeIndex() throws IOException {
        writer.close();
    }

}
