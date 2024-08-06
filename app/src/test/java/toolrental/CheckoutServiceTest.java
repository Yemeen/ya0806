package toolrental;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import toolrental.ToolType;
import toolrental.RentalAgreement;
import toolrental.CheckoutService;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {
    private CheckoutService service;

    @BeforeEach
    void setUp() {
        service = new CheckoutService();
    }

    @Test
    void testScenario1() {
        assertThrows(IllegalArgumentException.class, () -> service.checkout("JAKR", 5, 101, LocalDate.of(2015, 9, 3)));
    }

    @Test
    void testScenario2() {
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        RentalAgreement agreement = service.checkout("LADW", 3, 10, checkoutDate);

        System.out.println("Scenario 2 Results:");
        agreement.print();

        assertEquals("LADW", agreement.getToolCode(), "Tool code mismatch");
        assertEquals(ToolType.LADDER, agreement.getToolType(), "Tool type mismatch");
        assertEquals("Werner", agreement.getToolBrand(), "Tool brand mismatch");
        assertEquals(3, agreement.getRentalDays(), "Rental days mismatch");
        assertEquals(checkoutDate, agreement.getCheckoutDate(), "Checkout date mismatch");
        assertEquals(checkoutDate.plusDays(3), agreement.getDueDate(), "Due date mismatch");
        assertEquals(1.99, agreement.getDailyRentalCharge(), 0.001, "Daily rental charge mismatch");
        assertEquals(2, agreement.getChargeDays(), "Charge days mismatch");
        assertEquals(3.98, agreement.getPreDiscountCharge(), 0.001, "Pre-discount charge mismatch");
        assertEquals(10, agreement.getDiscountPercent(), "Discount percent mismatch");
        assertEquals(0.40, agreement.getDiscountAmount(), 0.001, "Discount amount mismatch");
        assertEquals(3.58, agreement.getFinalCharge(), 0.001, "Final charge mismatch");
    }

    @Test
    void testScenario3() {
        RentalAgreement agreement = service.checkout("CHNS", 5, 25, LocalDate.of(2015, 7, 2));
        assertNotNull(agreement);
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals(ToolType.CHAINSAW, agreement.getToolType());
        assertEquals("Stihl", agreement.getToolBrand());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 7), agreement.getDueDate());
        assertEquals(1.49, agreement.getDailyRentalCharge(), 0.001);
        assertEquals(3, agreement.getChargeDays());
        assertEquals(4.47, agreement.getPreDiscountCharge(), 0.001);
        assertEquals(25, agreement.getDiscountPercent());
        assertEquals(1.12, agreement.getDiscountAmount(), 0.001);
        assertEquals(3.35, agreement.getFinalCharge(), 0.001);
    }

    @Test
    void testScenario4() {
        RentalAgreement agreement = service.checkout("JAKD", 6, 0, LocalDate.of(2015, 9, 3));
        assertNotNull(agreement);
        assertEquals("JAKD", agreement.getToolCode());
        assertEquals(ToolType.JACKHAMMER, agreement.getToolType());
        assertEquals("DeWalt", agreement.getToolBrand());
        assertEquals(6, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 9, 9), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge(), 0.001);
        assertEquals(3, agreement.getChargeDays());
        assertEquals(8.97, agreement.getPreDiscountCharge(), 0.001);
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(0.00, agreement.getDiscountAmount(), 0.001);
        assertEquals(8.97, agreement.getFinalCharge(), 0.001);
    }

    @Test
    void testScenario5() {
        LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
        RentalAgreement agreement = service.checkout("JAKR", 9, 0, checkoutDate);

        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals(ToolType.JACKHAMMER, agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());
        assertEquals(9, agreement.getRentalDays());
        assertEquals(checkoutDate, agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 11), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge(), 0.001);
        assertEquals(5, agreement.getChargeDays());
        assertEquals(14.95, agreement.getPreDiscountCharge(), 0.001);
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(0.00, agreement.getDiscountAmount(), 0.001);
        assertEquals(14.95, agreement.getFinalCharge(), 0.001);
    }

    @Test
    void testScenario6() {
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        RentalAgreement agreement = service.checkout("JAKR", 4, 50, checkoutDate);

        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals(ToolType.JACKHAMMER, agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());
        assertEquals(4, agreement.getRentalDays());
        assertEquals(checkoutDate, agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 6), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge(), 0.001);
        assertEquals(1, agreement.getChargeDays());
        assertEquals(2.99, agreement.getPreDiscountCharge(), 0.001);
        assertEquals(50, agreement.getDiscountPercent());
        assertEquals(1.50, agreement.getDiscountAmount(), 0.001);
        assertEquals(1.49, agreement.getFinalCharge(), 0.001);
    }

    @Test
    void testInvalidDiscountPercent() {
        assertThrows(IllegalArgumentException.class, () -> service.checkout("JAKR", 5, 101, LocalDate.of(2023, 7, 2)));
    }
}