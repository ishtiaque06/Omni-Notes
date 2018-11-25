package it.feio.android.omninotes.async.keepimporthelper;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class googleKeepZipExtractor {
    private String mPath;
    private ZipInputStream mZip;
    private ArrayList<Document> mNotesList;

    // Creates a ZipInputStream from a given path
    public googleKeepZipExtractor(String path) {
        mPath = path;
        try {
            mZip = new ZipInputStream(new FileInputStream(mPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    // Verifies that an instantiated ZIP file is a Google Keep generated ZIP file.
    public boolean verifyKeepFolder() throws IOException {
        ZipEntry notesFolder = mZip.getNextEntry();
        while (notesFolder != null) {
            String name = notesFolder.getName().replaceFirst("\\w*/", "");
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
//    public ArrayList<Document> returnHtmlKeepNotes() throws IOException {
//        ZipEntry notesFolder = mZip.getNextEntry();
//
//    }
}
