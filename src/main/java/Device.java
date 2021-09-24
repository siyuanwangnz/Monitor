public class Device {
    private static String EOL = "\r\n";
    private boolean enableEOL = false;

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