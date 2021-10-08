package main.java;

public class Device {
    private static String EOL = "\r\n";
    private boolean enableEOL = true;

    public void setEnableEOL(boolean enable) {
        enableEOL = enable;
    }

    public String getEOL() {
        if (enableEOL) {
            return EOL;
        } else {
            return "";
        }

    }

}
