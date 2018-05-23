package cz.zcu.students.kiwi.snmp;

public class SnmpColumn {
    private int n;
    private String caption;
    private int width;

    public SnmpColumn(int n, String caption) {
        this(n, caption, caption.length());
    }

    public SnmpColumn(int n, String caption, int width) {
        this.n = n;
        this.caption = caption;
        this.width = width;
    }

    public int getN() {
        return n;
    }

    public SnmpColumn setN(int n) {
        this.n = n;
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public SnmpColumn setCaption(String caption) {
        if(this.width == this.caption.length()) {
            this.setWidth(caption.length());
        }

        this.caption = caption;

        return this;
    }

    public SnmpColumn setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getWidth() {
        return this.width;
    }
}
