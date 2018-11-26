package it.feio.android.omninotes.async.keepimporthelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class googleKeepZipExtractor {
    private String mPath;
    private ZipInputStream mZip;
    private ZipFile mZipToExtract;
    private ArrayList<Document> mNotesList = new ArrayList<>();

    // Creates a ZipInputStream from a given path
    public googleKeepZipExtractor(String path) {
        mPath = path;
        try {
            mZip = new ZipInputStream(new FileInputStream(mPath));
            mZipToExtract = new ZipFile(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Verifies that an instantiated ZIP file is a Google Keep generated ZIP file.
    public boolean verifyKeepFolder() throws IOException {
        ZipEntry notesFolder = mZip.getNextEntry();
        while (notesFolder != null) {
            String name = this.truncateFilePath(notesFolder);
            if ((name.length() >= 5) && (name.substring(0, 4).equals("Keep"))) {
                mZip.closeEntry();
                mZip.close();
                return true;
            }
            mZip.closeEntry();
            notesFolder = mZip.getNextEntry();
        }
        mZip.close();
        return false;
    }

    // Returns an ArrayList of Document objects to make notes out of.
    // minimum length of a valid HTML filename: "Keep/h.html" == 11
    public ArrayList<Document> returnHtmlKeepNotes() throws IOException {
        // This check has been moved to DataBackupInterntService but can be used
        // if this extractor is ever used as its own module.
//        if (!this.verifyKeepFolder()) {
//            return mNotesList;
//        }
        try {
            Enumeration<? extends ZipEntry> entries = mZipToExtract.entries();
            while (entries.hasMoreElements()) {
                ZipEntry noteEntry = entries.nextElement();
                String name = this.truncateFilePath(noteEntry);
                if ((name.length() >= 11)
                        && (name.substring(0, 4).equals("Keep"))
                        && (name.substring(name.length() - 5, name.length()).equals(".html"))) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(mZipToExtract.getInputStream(noteEntry))
                            );
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    String result = sb.toString();
                    if (result.length() != 0) {
                        Document doc = Jsoup.parse(result);

                        // This class resides inside ".heading" so has been removed.
                        doc.select(".meta-icons").remove();
                        mNotesList.add(doc);
                    }
                }
            }
        }
        finally {
            mZipToExtract.close();
        }
        return mNotesList;
    }

    // This method truncates the parent ZIP file's path from each file inside it
    private String truncateFilePath(ZipEntry entry) {
        return entry.getName().replaceFirst("\\w*/", "");
    }
}
