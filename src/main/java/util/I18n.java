package util;

import com.ibm.icu.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    private static ResourceBundle bundle;
    private static Locale currentLocale;

    static {
        setLocale(new Locale("pt", "PT"));
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("i18n/messages", currentLocale);
    }

    public static String get(String key, Object... args) {
        try {
            String pattern = bundle.getString(key);
            return MessageFormat.format(pattern, args);
        } catch (Exception e) {
            return "!" + key + "!";
        }
    }
}
