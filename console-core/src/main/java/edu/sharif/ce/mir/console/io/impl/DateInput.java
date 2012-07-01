package edu.sharif.ce.mir.console.io.impl;

import edu.sharif.ce.mir.console.io.Input;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 3:21)
 */
public class DateInput implements Input {

    private Date time;

    @Override
    public boolean accepts(String string) {
        return string.matches("\\d{4}(\\-|/)\\d{2}\\1\\d{2}") || string.matches("\\d{2}(\\-|/)\\d{2}\\1\\d{2}");
    }

    @Override
    public void setValue(String value) {
        if (value.length() == 8) { //yy-mm-dd
            if (Integer.parseInt(Character.toString(value.charAt(0))) < 3) {
                value = "20" + value;
            } else {
                value = "19" + value;
            }
        }
        final Matcher matcher = Pattern.compile("^(\\d+?)(\\-|/)(\\d+?)\\2(\\d+?)$").matcher(value);
        matcher.find();
        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(3));
        int day = Integer.parseInt(matcher.group(4));
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        time = calendar.getTime();
    }

    public Date getDate() {
        return time;
    }
}
