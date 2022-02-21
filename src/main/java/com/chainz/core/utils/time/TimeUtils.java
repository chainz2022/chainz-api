package com.chainz.core.utils.time;

import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60000;
    private static final int HOUR_MILLIS = 3600000;
    private static final int DAY_MILLIS = 86400000;

    public static String getPrettyTime(Timestamp time) {
        PrettyTime p = new PrettyTime(new Locale("es"));
        return p.formatDuration(new Date(time.getTime()));
    }

    public static String getPrettyTimeDate(Date date) {
        PrettyTime p = new PrettyTime(new Locale("es"));
        return p.format(date);
    }

    public static String formatSeconds(long seconds) {
        String time_formated;
        int days = (int) TimeUnit.SECONDS.toDays(seconds);
        int hours = (int) TimeUnit.SECONDS.toHours(seconds) - (int) TimeUnit.DAYS.toHours(days);
        int mins = (int) TimeUnit.SECONDS.toMinutes(seconds) - (int) TimeUnit.DAYS.toMinutes(days) - (int) TimeUnit.HOURS.toMinutes(hours);
        int secs = (int) TimeUnit.SECONDS.toSeconds(seconds) - (int) TimeUnit.DAYS.toSeconds(days) - (int) TimeUnit.HOURS.toSeconds(hours) - (int) TimeUnit.MINUTES.toSeconds(mins);
        String daysForm = "", hoursForm = "", minsForm = "", secsForm = "";
        if (days <= 0 && hours <= 0 && mins <= 0) {
            if (String.valueOf(secs).length() == 2 && String.valueOf(secs).startsWith("0")) {
                secsForm = String.valueOf(secs);
            } else {
                secsForm = String.valueOf(secs);
            }
            time_formated = secsForm + "s";
        } else if (days <= 0 && hours <= 0) {
            if (String.valueOf(secs).length() == 2 && String.valueOf(secs).startsWith("0")) {
                secsForm = String.valueOf(secs);
            } else {
                secsForm = String.valueOf(secs);
            }
            if (String.valueOf(mins).length() == 2 && String.valueOf(mins).startsWith("0")) {
                secsForm = String.valueOf(mins);
            } else {
                minsForm = String.valueOf(mins);
            }
            time_formated = minsForm + "m " + secsForm + "s";
        } else if (days <= 0) {
            if (String.valueOf(secs).length() == 2 && String.valueOf(secs).startsWith("0")) {
                secsForm = String.valueOf(secs);
            } else {
                secsForm = String.valueOf(secs);
            }
            if (String.valueOf(mins).length() == 2 && String.valueOf(mins).startsWith("0")) {
                secsForm = String.valueOf(mins);
            } else {
                minsForm = String.valueOf(mins);
            }
            if (String.valueOf(hours).length() == 2 && String.valueOf(hours).startsWith("0")) {
                hoursForm = String.valueOf(hours);
            } else {
                hoursForm = String.valueOf(hours);
            }
            time_formated = hoursForm + "h " + minsForm + "m " + secsForm + "s";
        } else {
            if (String.valueOf(secs).length() == 2 && String.valueOf(secs).startsWith("0")) {
                secsForm = String.valueOf(secs);
            } else {
                secsForm = String.valueOf(secs);
            }
            if (String.valueOf(mins).length() == 2 && String.valueOf(mins).startsWith("0")) {
                secsForm = String.valueOf(mins);
            } else {
                minsForm = String.valueOf(mins);
            }
            if (String.valueOf(hours).length() == 2 && String.valueOf(hours).startsWith("0")) {
                hoursForm = String.valueOf(hours);
            } else {
                hoursForm = String.valueOf(hours);
            }
            if (String.valueOf(days).length() == 2 && String.valueOf(days).startsWith("0")) {
                daysForm = String.valueOf(days);
            } else {
                daysForm = String.valueOf(days);
            }
            time_formated = daysForm + "d " + hoursForm + "h " + minsForm + "m " + secsForm + "s";
        }
        return time_formated;
    }

    public static String getlongtoAgo(long createdAt) {
        DateFormat userDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = null;
        date = new Date(createdAt);
        String crdate1 = dateFormatNeeded.format(date);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        crdate1 = (new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(date);
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());
        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate1);
            current = dateFormat.parse(currenttime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = current.getTime() - CreatedAt.getTime();
        long diffSeconds = diff / 1000L;
        long diffMinutes = diff / 60000L % 60L;
        long diffHours = diff / 3600000L % 24L;
        long diffDays = diff / 86400000L;
        String time = null;
        if (diffDays > 0L) {
            if (diffDays == 1L) {
                time = diffDays + " día";
            } else {
                time = diffDays + " días";
            }
        } else if (diffHours > 0L) {
            if (diffHours == 1L) {
                time = diffHours + " hora";
            } else {
                time = diffHours + " horas";
            }
        } else if (diffMinutes > 0L) {
            if (diffMinutes == 1L) {
                time = diffMinutes + " minuto";
            } else {
                time = diffMinutes + " minutos";
            }
        } else if (diffSeconds > 0L) {
            time = diffSeconds + " segundos";
        }
        return time;
    }

    public static String getTimeAgo(long time_ago) {
        long cur_time = Calendar.getInstance().getTimeInMillis() / 1000L;
        long time_elapsed = cur_time - time_ago;
        long seconds = time_elapsed;
        int minutes = Math.round((float) (time_elapsed / 60L));
        int hours = Math.round((float) (time_elapsed / 3600L));
        int days = Math.round((float) (time_elapsed / 86400L));
        int weeks = Math.round((float) (time_elapsed / 604800L));
        int months = Math.round((float) (time_elapsed / 2600640L));
        int years = Math.round((float) (time_elapsed / 31207680L));
        if (seconds <= 60L)
            return "menos de un minuto";
        if (minutes <= 60) {
            if (minutes == 1)
                return "un minuto";
            return minutes + " minutos";
        }
        if (hours <= 24) {
            if (hours == 1)
                return "una hora";
            return hours + " horas";
        }
        if (days <= 7) {
            if (days == 1)
                return "ayer";
            return days + " días";
        }
        if (weeks <= 4.3D) {
            if (weeks == 1)
                return "una semana";
            return weeks + " semanas";
        }
        if (months <= 12) {
            if (months == 1)
                return "un mes";
            return months + " meses";
        }
        if (years == 1)
            return "un año";
        return years + " años";
    }
}
