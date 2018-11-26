package it.feio.android.omninotes.async.keepimporthelper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/*
* This class generates a KeepNote object containing timezoned note details
* from a JSoup Document object.
* */
public class makeNoteFromHTML {
    private Document mDoc;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    private String mTimeZone;
    private KeepNote mNote;

    public Document getDoc() {
        return mDoc;
    }

    public void setDoc(Document doc) {
        mDoc = doc;
    }

    public KeepNote getNote() {
        return mNote;
    }

    public void setNote(KeepNote note) {
        mNote = note;
    }

    public makeNoteFromHTML() {
        this.setDoc(null);
        this.setNote(null);
    }

    public makeNoteFromHTML(Document doc, String timeZone) {
        this.setDoc(doc);
        this.setTimeZone(timeZone);
    }

    /*
    * This parses the data present in the Keep HTML note. For now, the important
    * parts are the title, the ".heading" class and ".content" class, with ".heading"
    * containing when the note was first created.
    * @return: KeepNote
    * */
    public KeepNote buildKeepNote() {
        if (mDoc != null) {
            Long timeInMillis;
            String title = mDoc.title();
            String creationTime = mDoc.select(".heading").html();
            if (creationTime.length() == 0) {
                DateTime dateTime = new DateTime();
                timeInMillis = dateTime.getMillis();
            }
            else {
                DateTimeFormatter formatter = DateTimeFormat
                        .forPattern("MMM dd, yyyy, HH:mm:ss a")
                        .withZone(DateTimeZone.forID(mTimeZone));
                DateTime dateTime = DateTime.parse(creationTime, formatter);
                timeInMillis = dateTime.getMillis();
            }
            String content = mDoc.select(".content").html();

            // Remove HTML linebreaks and paragraphs and replace them with
            // Actual newlines
            content = content.replaceAll("<br>", "\n");
            content = content.replaceAll("<p>", "\n");
            KeepNote note = new KeepNote(title, timeInMillis, content);
            return note;
        }
        else {
            return null;
        }
    }
}
