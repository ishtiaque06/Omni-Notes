package it.feio.android.omninotes.async.keepimporthelper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Document;

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

    public KeepNote returnNote() {
        if (mDoc != null) {
            String title = mDoc.title();
            String creationTime = mDoc.select(".heading").html();
            DateTimeFormatter formatter = DateTimeFormat
                    .forPattern("MMM dd, yyyy, HH:mm:ss a")
                    .withZone(DateTimeZone.forID(mTimeZone));
            DateTime dateTime = DateTime.parse(creationTime, formatter);
            Long timeInMillis = dateTime.getMillis();
            String content = mDoc.select(".content").html();
            KeepNote note = new KeepNote(title, timeInMillis, content);
            return note;
        }
        else {
            return null;
        }
    }
}
