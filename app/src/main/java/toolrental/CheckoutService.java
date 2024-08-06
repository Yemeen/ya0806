package toolrental;

import java.time.LocalDate;
import java.util.stream.IntStream;

class CheckoutService {
    private final RentalCalendar rentalCalendar;

    public CheckoutService() {
        this.rentalCalendar = new RentalCalendar();
    }

    public RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        validateInput(toolCode, rentalDays, discountPercent, checkoutDate);
        Tool tool = createToolByCode(toolCode);
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int chargeDays = calculateChargeDays(tool, checkoutDate, rentalDays);

        System.out.println("Checkout calculation:");
        System.out.println("Tool: " + tool.getCode() + ", Type: " + tool.getType() + ", Brand: " + tool.getBrand());
        System.out.println("Rental days: " + rentalDays + ", Charge days: " + chargeDays);

        double dailyCharge = tool.getDailyCharge();
        double preDiscountCharge = Math.round(chargeDays * dailyCharge * 100.0) / 100.0;
        double discountAmount = Math.round((preDiscountCharge * discountPercent / 100.0) * 100.0) / 100.0;
        double finalCharge = Math.round((preDiscountCharge - discountAmount) * 100.0) / 100.0;

        System.out.println("Daily charge: " + dailyCharge);
        System.out.println("Pre-discount charge: " + preDiscountCharge);
        System.out.println("Discount amount: " + discountAmount);
        System.out.println("Final charge: " + finalCharge);

        return new RentalAgreement(toolCode, tool.getType(), tool.getBrand(), rentalDays,
                checkoutDate, dueDate, tool.getDailyCharge(), chargeDays,
                preDiscountCharge, discountPercent, discountAmount, finalCharge);
    }

    private void validateInput(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        if (toolCode == null || toolCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Tool code must not be null or empty");
        }
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be in the range 0-100");
        }
        if (checkoutDate == null) {
            throw new IllegalArgumentException("Checkout date must not be null");
        }
    }

    private Tool createToolByCode(String toolCode) {
        return Tool.fromCode(toolCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tool code: " + toolCode));
    }

    private int calculateChargeDays(Tool tool, LocalDate checkoutDate, int rentalDays) {
        return (int) IntStream.rangeClosed(1, rentalDays)
                .mapToObj(checkoutDate::plusDays)
                .filter(date -> isChargeable(tool, date))
                .count();
    }

    private boolean isChargeable(Tool tool, LocalDate date) {
        boolean isWeekend = rentalCalendar.isWeekend(date);
        boolean isHoliday = rentalCalendar.isHoliday(date);

        return (!isWeekend && !isHoliday) ||
                (isWeekend && tool.isWeekendCharge()) ||
                (isHoliday && tool.isHolidayCharge());
    }
}