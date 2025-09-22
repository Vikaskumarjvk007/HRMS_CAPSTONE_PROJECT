package com.hrms.model.helper;



import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

public class LeaveCalendar {
    public static LocalDate showCalendarAndPickDate(Scanner sc) {
        System.out.print("Enter year (e.g. 2025): ");
        int year = sc.nextInt();

        System.out.print("Enter month (1-12): ");
        int month = sc.nextInt();

        YearMonth ym = YearMonth.of(year, month);
        System.out.println("\n--- " + ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + " " + year + " ---");

        System.out.println("Mo Tu We Th Fr Sa Su");

        LocalDate firstDay = ym.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue(); // 1=Mon … 7=Sun

        for (int i = 1; i < dayOfWeek; i++) {
            System.out.print("   ");
        }

        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            System.out.printf("%2d ", day);
            if ((day + dayOfWeek - 1) % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println();

        System.out.print("Select day: ");
        int chosenDay = sc.nextInt();

        return LocalDate.of(year, month, chosenDay);
    }
}
