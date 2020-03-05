package tax.www.module.object;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 날짜관련 Business 로직
 * <p/>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:59
 */
@Component("CmnDateBiz")
public class CmnDateBiz {

    private static final Logger log = LogManager.getLogger(CmnDateBiz.class);

    /**
     * String형 날짜를 가져와서 Date 날짜 패턴으로 변경
     *
     * @param date         String 형 날짜
     * @param pattern      날짜 패턴(ex : yyyy-MM-dd HH:mm:ss)
     * @param num          이후/이전 n 날짜 간격
     * @param calendarType Calendar의 타입
     * @return the date
     */
    public static Date convertDate(String date, String pattern, int num, int calendarType) {

        Date tmpDate = null;
        DateFormat formatter = new SimpleDateFormat(pattern);

        try {
            tmpDate = formatter.parse(date);

            if (num != 0) {
                return addToDate(tmpDate, num, calendarType);
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }

        return tmpDate;
    }

    /**
     * Date형 날짜를 가져와서 String 날짜 패턴으로 변경
     *
     * @param date    Date 형 날짜
     * @param pattern 날짜 패턴(ex : yyyy-MM-dd HH:mm:ss)
     * @return the string
     */
    public static String convertString(Date date, String pattern) {

        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 오늘 날짜 String Type
     *
     * @param pattern 날짜 패턴(ex : yyyy-MM-dd HH:mm:ss)
     * @return the string
     */
    public static String todayString(String pattern) {

        return convertString(addToCalendar(new Date(), 0, Calendar.DATE).getTime(), pattern);
    }

    /**
     * 날짜의 이후/이전 날짜 가져오기
     *
     * @param date         날짜
     * @param num          간격
     * @param calendarType Calendar의 타입
     * @return the calendar
     */
    public static Calendar addToCalendar(Date date, int num, int calendarType) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        calendarDate.add(calendarType, num);

        return calendarDate;
    }

    /**
     * 특정월이 마지막 날짜 가져오기
     *
     * @param date    String 형 날짜
     * @param pattern 날짜 패턴(ex : yyyy-MM-dd HH:mm:ss)
     * @return int
     */
    public static int monthTolastDay(String date, String pattern) {

        Date tmpDate = null;
        DateFormat formatter = new SimpleDateFormat(pattern);

        try {
            tmpDate = formatter.parse(date);

            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(tmpDate);

            return calendarDate.getActualMaximum(Calendar.DATE);

        } catch (Exception ex) {
            log.error(ex.toString(), ex);

            return 0;
        }
    }

    /**
     * 날짜의 이후/이전 날짜 가져오기
     *
     * @param date         날짜
     * @param num          간격
     * @param calendarType Calendar의 타입
     * @return the date
     */
    public static Date addToDate(Date date, int num, int calendarType) {

        return addToCalendar(date, num, calendarType).getTime();
    }

    /**
     * 주말인지 확인
     *
     * @param date 현재 날짜
     * @return the date
     */
    public static boolean getWeekDay(Date date) {

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        // 일, 월, 화, 수, 목, 금, 토
        int nowWeek = calendarDate.get(Calendar.DAY_OF_WEEK) - 1;

        return (nowWeek == 0 || nowWeek == 6 ? true : false);
    }

    /**
     * 주말/해당 요일일 경우 날짜 이동
     *
     * @param date    현재 날짜
     * @param chkWeek 선택 요일( 0 : 일, 1 : 월, 2 : 화, 3 : 수, 4 : 목, 5 : 금, 6 : 토 )
     * @param isWeek  요일 날짜 이동 유무
     * @return the date
     */
    public static Date setWeekDay(Date date, int chkWeek, boolean isWeek) {

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        // 일, 월, 화, 수, 목, 금, 토
        int nowWeek = calendarDate.get(Calendar.DAY_OF_WEEK) - 1;

        if (isWeek == true) {

            date = addToCalendar(date, (chkWeek - nowWeek), Calendar.DATE).getTime();
        }

        switch (nowWeek) {

            case 0:
                // 일요일
                date = addToCalendar(date, 1, Calendar.DATE).getTime();
                break;
            case 6:
                // 토요일
                date = addToCalendar(date, 2, Calendar.DATE).getTime();
                break;
        }

        return date;
    }

    /**
     * 특정 요일일 경우 날짜 이동
     *
     * @param date    현재 날짜
     * @param chkWeek 선택 요일( 0 : 일, 1 : 월, 2 : 화, 3 : 수, 4 : 목, 5 : 금, 6 : 토 )
     * @param num     간격
     * @return the date
     */
    public static Date setMoveDay(Date date, int chkWeek, int num) {

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        // 일, 월, 화, 수, 목, 금, 토
        int nowWeek = calendarDate.get(Calendar.DAY_OF_WEEK) - 1;

        if (nowWeek == chkWeek) {
            date = addToCalendar(date, num, Calendar.DATE).getTime();
        }

        return date;
    }
}
