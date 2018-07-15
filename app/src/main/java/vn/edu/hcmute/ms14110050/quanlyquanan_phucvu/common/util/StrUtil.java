package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class StrUtil {
    public static boolean isEmpty(String string) {
        return string == null || string.equals("");
    }

    public static String deAccent(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCOMBINING_DIACRITICAL_MARKS}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(s);
        calendar.setTime(date);
        return calendar;
    }

    public static String getAbsoluteImgUrl(String relativeUrl){
        return relativeUrl.replace("localhost", SOCKET.SERVER_IP);
    }

}
