package models.sale;

import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class SaleItemsTest extends TestCase {

    @Test
    public void testSetAndGetId() {
        SaleItems item = new SaleItems();
        item.setId(101);
        assertEquals(101, item.getId());
    }

    @Test
    public void testDefaultTotalPriceIsNull() {
        SaleItems item = new SaleItems();
        assertEquals(BigDecimal.ZERO, item.getTotalPrice());
    }

    // have to later implement like notifications when stock goes negative.
    @Test
    public void testNegativeQuantity() {
        SaleItems item = new SaleItems();
        item.setQuantity(-10);
        assertEquals(-10, item.getQuantity());  // If you plan to disallow, add validation later
    }

    @Test
    public void testNegativeUnitPrice() {
        SaleItems item = new SaleItems();

        assertThrows(IllegalArgumentException.class, () -> {
            item.setUnitePrice(-5.5);
        });
    }

    @Test
    public void testManualTotalPriceCalculation() {
        SaleItems item = new SaleItems();
        item.setQuantity(3);
        item.setUnitePrice(15.50);

        BigDecimal expectedTotal = BigDecimal.valueOf(item.getQuantity() * item.getUnitePrice());
        item.setTotalPrice(expectedTotal);

        assertEquals(expectedTotal, item.getTotalPrice());
    }

    @Test
    public void testMismatchBetweenUnitAndTotalPrice() {
        SaleItems item = new SaleItems();
        item.setQuantity(2);
        item.setUnitePrice(10.00);
        item.setTotalPrice(new BigDecimal("15.00"));  // Not 20.00

        assertNotEquals(
                new BigDecimal(item.getQuantity() * item.getUnitePrice()),
                item.getTotalPrice()
        );
    }

    @Test
    public void testLargeQuantity() {
        SaleItems item = new SaleItems();

        assertThrows(IllegalArgumentException.class, () -> {
            item.setQuantity(1500);;
        });
    }

    @Test
    public void testTotalPriceNegative() {
        SaleItems item = new SaleItems();

        assertThrows(IllegalArgumentException.class, () -> {
            item.setTotalPrice(new BigDecimal("-34"));
        });
    }




}