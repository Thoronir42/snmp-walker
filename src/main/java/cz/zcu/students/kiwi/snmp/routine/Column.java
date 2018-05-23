package cz.zcu.students.kiwi.snmp.routine;

public class Column {
    private int childOid;
    private String caption;
    private int width;

    private String valFormat;

    public Column(int childOid, String caption) {
        this(childOid, caption, caption.length());
    }

    public Column(int childOid, String caption, int width) {
        this.childOid = childOid;

        this.setCaption(caption);
        this.setWidth(width);
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
}
