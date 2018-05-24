package cz.zcu.students.kiwi.snmp.routine;

public class Column {
    private int childOid;
    private String caption;
    private String fullName;

    private int width;

    private String valFormat;

    public Column(int childOid, String caption) {
        this(childOid, caption, caption.length());
    }

    public Column(int childOid, String caption, String fullName) {
        this(childOid, caption);
        this.setFullName(fullName);
    }

    public Column(int childOid, String caption, int width) {
        this.childOid = childOid;

        this.setCaption(caption);
        this.setWidth(width);
    }

    public Column(int childOid, String caption, int width, String fullName) {
        this(childOid, caption, width);
        this.setFullName(fullName);
    }

    private Column setFullName(String fullName) {
        this.fullName = fullName;

        return this;
    }

    public int getChildOid() {
        return childOid;
    }

    public Column setChildOid(int childOid) {
        this.childOid = childOid;
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public Column setCaption(String caption) {
        int oldLength = this.caption != null ? this.caption.length() : 0;
        this.caption = caption;

        if (this.width == oldLength) {
            this.setWidth(caption.length());
        }

        return this;
    }

    public Column setWidth(int width) {
        this.width = width;
        this.valFormat = "%" + width + "s";
        return this;
    }

    public int getWidth() {
        return this.width;
    }

    public String format(String... args) {
        return String.format(this.valFormat, (Object[]) args);
    }

    public String getFullName() {
        return this.fullName != null ? this.fullName : this.caption;
    }
}
