package it.feio.android.omninotes.async.keepimporthelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class googleKeepZipExtractorTest {
    protected googleKeepZipExtractor extractor1, extractor2, extractor3;

    // Loading the ZIP file(s) for testing
    ClassLoader classLoader = getClass().getClassLoader();
    URL notKeep = classLoader.getResource("dummyZipNotKeep.zip");
    URL Keep = classLoader.getResource("dummyZipKeep.zip");
    URL keepWithData = classLoader.getResource("keepTakeoutTest.zip");
    URL noTitleHTMLNote = classLoader.getResource("Takeout/Keep/2018-11-25T02_22_31.261-05_00.html");
    URL TitledHTMLNote = classLoader.getResource("Takeout/Keep/New_Note_with_a_Title.html");

    public googleKeepZipExtractorTest() {
        extractor1 = new googleKeepZipExtractor(notKeep.getPath());
        extractor2 = new googleKeepZipExtractor(Keep.getPath());
        extractor3 = new googleKeepZipExtractor(keepWithData.getPath());
    }

    // Tests to verify valid Keep Notes ZIP files
    @Test
    public void test_keepVerify() throws IOException {
        assertFalse(extractor1.verifyKeepFolder());
        assertTrue(extractor2.verifyKeepFolder());
        assertTrue(extractor3.verifyKeepFolder());
    }

    // Extracts actual Keep Notes and matches the titles with manually created Keep Note HTML docs.
    @Test
    public void test_keepNotePresence() throws IOException {
        File file1 = new File(noTitleHTMLNote.getPath());
        Document doc1 = Jsoup.parse(file1, "utf-8");
        File file2 = new File(TitledHTMLNote.getPath());
        Document doc2 = Jsoup.parse(file2, "utf-8");
        extractor1.returnHtmlKeepNotes();
        extractor2.returnHtmlKeepNotes();

        ArrayList<Document> notes = extractor3.returnHtmlKeepNotes();
        assertEquals(notes.get(0).title(), doc2.title());
        assertEquals(notes.get(1).title(), doc1.title());
    }
}