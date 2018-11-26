package it.feio.android.omninotes.async.keepimporthelper;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

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

        doc1.select(".meta-icons").remove();
        System.out.println(doc1.select(".heading").html());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM dd, yyyy, HH:mm:ss a");
        LocalDateTime dateTime = LocalDateTime.parse(doc1.select(".heading").html(), formatter);
        TimeZone tz = TimeZone.getDefault();
        System.out.println(tz.getDisplayName(false, TimeZone.SHORT));
        System.out.println(dateTime);
        System.out.println(doc1.select(".heading").html());
        System.out.println(doc1.title());
        System.out.println(doc1.select(".content").html());


        doc2.select(".meta-icons").remove();
        System.out.println(doc2.select(".heading").html());
        System.out.println(doc2.title());
        System.out.println(doc2.select(".content").html());

        assertEquals(notes.get(0).title(), doc2.title());
        assertEquals(notes.get(1).title(), doc1.title());
    }
}