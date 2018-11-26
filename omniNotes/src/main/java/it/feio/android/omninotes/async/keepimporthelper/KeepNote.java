package it.feio.android.omninotes.async.keepimporthelper;

public class KeepNote {
    private String mTitle;
    private Long mCreationTime;
    private String mContent;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Long getCreationTime() {
        return mCreationTime;
    }

    public void setCreationTime(Long creationTime) {
        mCreationTime = creationTime;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public KeepNote() {
        this.setCreationTime(null);
        this.setContent(null);
        this.setTitle(null);
    }

    public KeepNote(String title, Long creationTime, String content) {
        this.setTitle(title);
        this.setContent(content);
        this.setCreationTime(creationTime);
    }
}
