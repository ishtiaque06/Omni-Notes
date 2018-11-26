package it.feio.android.omninotes.async.keepimporthelper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class makeNoteFromHTMLTest {

    protected makeNoteFromHTML noteMaker1, noteMaker2, noteMaker3;
    ClassLoader classLoader = getClass().getClassLoader();
    URL noTitleHTMLNote = classLoader.getResource("Takeout/Keep/2018-11-25T02_22_31.261-05_00.html");
    URL TitledHTMLNote = classLoader.getResource("Takeout/Keep/New_Note_with_a_Title.html");

    public makeNoteFromHTMLTest() {
        TimeZone tz = TimeZone.getDefault();
        String zone = tz.getDisplayName(false, TimeZone.SHORT);
        noteMaker1 = new makeNoteFromHTML(null, zone);
        noteMaker2 = new makeNoteFromHTML(null,  zone);
        noteMaker3 = new makeNoteFromHTML(null, zone);
    }

    @Test
    public void test_checkTimeZoneAddition() throws IOException {
        File file1 = new File(noTitleHTMLNote.getPath());
        Document doc1 = Jsoup.parse(file1, "utf-8");
        doc1.select(".meta-icons").remove();
        File file2 = new File(TitledHTMLNote.getPath());
        Document doc2 = Jsoup.parse(file2, "utf-8");

        noteMaker1.setDoc(doc1);
        KeepNote note = noteMaker1.returnNote();
        assertEquals(note.getTitle(), "Nov 25, 2018, 2:22:31 AM");
        assertEquals(note.getContent(), "New note without a title!");
        Long creationTime = note.getCreationTime();
        Long actualTime = 1543130551000L;
        assertEquals(creationTime, actualTime);
        DateTime date = new DateTime(note.getCreationTime(), DateTimeZone.forID(noteMaker1.getTimeZone()));
        String actualDate = "2018-11-25T02:22:31.000-05:00";
        assertEquals(date.toString(), actualDate);


    }

}