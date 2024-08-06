package toolrental;

import java.time.LocalDate;
import java.time.Month;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RentalCalendar {
    private final List<Predicate<LocalDate>> holidays;

    public RentalCalendar() {
        holidays = new ArrayList<>();
        addIndependenceDay();
        addLaborDay();
    }

    public boolean isHoliday(LocalDate date) {
        return holidays.stream().anyMatch(holiday -> holiday.test(date));
    }

    public boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public boolean isWeekday(LocalDate date) {
        return !isWeekend(date);
    }

    public void addHoliday(Predicate<LocalDate> holiday) {
        holidays.add(holiday);
    }

    private void addIndependenceDay() {
        addHoliday(date -> {
            if (date.getMonth() != Month.JULY)
                return false;
            int day = date.getDayOfMonth();
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            return day == 4 && isWeekday(date) ||
                    (day == 3 && dayOfWeek == DayOfWeek.FRIDAY) ||
                    (day == 5 && dayOfWeek == DayOfWeek.MONDAY);
        });
    }

    private void addLaborDay() {
        addHoliday(date -> date.getMonth() == Month.SEPTEMBER &&
                date.getDayOfWeek() == DayOfWeek.MONDAY &&
                date.getDayOfMonth() <= 7);
    }
}