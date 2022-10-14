import scalable.tests.OrderApiController;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MainTests {

    private OrderApiController api;

    @BeforeClass
    public void beforeClass() {
        api = new OrderApiController();
    }

    @DataProvider(name = "order_side")
    public Object[][] dpMethod() {
        return new Object[][]{{"Buy"}, {"Sell"}};
    }


    /**
     * Create order
     * Check order created
     * Delete order
     * Check order deleted
     */
    @Test(dataProvider = "order_side")
    public void createGetDeleteOrder(String side) {
        String price = "250.3";
        String quantity = "55";

        // Create new order
        String orderId = api.createOrder(price, quantity, side)
                .assertHttpCode(200)
                .assertOrderIdNotNull()
                .assertOrderPrice(price)
                .assertOrderSide(side) // TODO BUG: returns 'buy' instead 'Buy','sell' instead 'Sell'
                .assertOrderQuantity(quantity)
                .getFieldOrderId();

        // get created order by id
        api.getOrderById(orderId)
                .assertHttpCode(200)
                .assertOrderId(orderId)
                .assertOrderPrice(price)
                .assertOrderSide(side)
                .assertOrderQuantity(quantity);

        // Delete created order
        api.deleteOrderById(orderId)
                .assertHttpCode(200);

        // Get deleted order
        api.getOrderById(orderId)
                .assertHttpCode(404);
    }


    /**
     * Create 2 orders
     * Clean all orders
     * Check code 200
     * Check that orders were deleted
     */
    @Test
    public void cleanOrderTest() {
        String order1 = api.createOrder("150", "10", "Sell")
                .assertHttpCode(200)
                .getFieldOrderId();
        String order2 = api.createOrder("6509.50", "5", "Buy")
                .assertHttpCode(200)
                .getFieldOrderId();

        api.getOrderClean()
                .assertHttpCode(200)
                .assertMessage("Order book is clean.");

        api.getOrderById(order1)
                .assertHttpCode(404);
        api.getOrderById(order2)
                .assertHttpCode(404);
    }


    /**
     * Create order with required fields filled
     * Check that order was created
     */
    @Test
    public void createOrderPriceNullTest() {
        String quantity = "1";
        String side = "Sell";
        api.createOrder(null, quantity, side)
                .assertHttpCode(400)
                .assertMessage("You need to pass 'price' param");
        //TODO price - optional param as required
    }

    /**
     * Create order with only price filled
     * Check code 400
     */
    @Test
    public void createOrderRequiredFieldsNotFilledTest() {
        api.createOrder("10.10", null, null)
                .assertHttpCode(400)
                .assertMessage("You need to pass 'quantity' param");
    }

    /**
     * Create order with price and quantity filled
     */
    @Test
    public void createOrderWithQuantityAndPriceTest() {
        api.createOrder("10.10", "15", null)
                .assertHttpCode(400)
                .assertMessage("Side can't be null or empty");
    }

    /**
     * Create order with invalid quantity
     * Check code 400
     */
    @Test
    public void createOrderInvalidQuantityTest() {
        String quantity = "Invalid_quantity";
        api.createOrder(null, quantity, null)
                .assertHttpCode(400)
                .assertMessage("You need to pass 'price' param");
    }

    /**
     * Create fields with price - double, quantity - long
     */
    @Test
    public void createOrderNumbersTest() {
        double price = 30.46;
        long quantity = 7768;
        api.createOrder(price, quantity, "Sell")
                .assertHttpCode(200)
                .assertOrderQuantity(String.valueOf(quantity))
                .assertOrderPrice(String.valueOf(price));
    }

    /**
     * Create fields with price - double, quantity - long
     */
    @Test
    public void createOrderIncorrectNumbersTest() {
        api.createOrder(345476878.446, 5684L, "Sell")
                .assertHttpCode(400)
                .assertMessage("Price: Incorrect number of decimal digits");
    }

    /**
     * Create order with invalid price (> 10000)
     * Check code 400
     */
    @Test
    public void createOrderPriceMore10kTest() {
        String price = "10001";
        api.createOrder(price, null, null)
                .assertHttpCode(400)
                .assertMessage("Price can't be more or equal than 10000");
    }

    /**
     * Create order with invalid price=10000
     * Check code 400
     */
    @Test
    public void createOrderPriceIs10kTest() {
        String price = "10000";
        api.createOrder(price, "6", "Sell")
                .assertHttpCode(400)
                .assertMessage("Price can't be more or equal than 10000"); // TODO BUG: price can't be 10000 as required
    }

    /**
     * Create order with invalid price (< 0)
     * Check code 400
     */
    @Test
    public void createOrderPriceLessZeroTest() {
        String price = "-1";
        api.createOrder(price, null, null)
                .assertHttpCode(400)
                .assertMessage("Price can't be less or equal than 0");
    }

    /**
     * Create order with invalid price (0)
     * Check code 400
     */
    @Test
    public void createOrderPriceZeroTest() {
        String price = "0";
        api.createOrder(price, null, null)
                .assertHttpCode(400)
                .assertMessage("Price can't be less or equal than 0");
    }

    /**
     * Create order with invalid quantity=0
     * Check code 400
     */
    @Test
    public void createOrderQuantityZeroTest() {
        int quantity = 0;
        api.createOrder("235", quantity, "Buy")
                .assertHttpCode(400) // TODO BUG: quantity can't be equal 0 as required
                .assertMessage("Price can't be less or equal than 0");
    }

    /**
     * Create order with invalid quantity<0
     * Check code 400
     */
    @Test
    public void createOrderQuantityLessZeroTest() {
        int quantity = -10;
        api.createOrder("235", quantity, "Buy")
                .assertHttpCode(400)
                .assertMessage("Quantity can't be less or equal than 0");
    }

    /**
     * Create order with invalid quantity>10000
     * Check code 400
     */
    @Test
    public void createOrderQuantityMore10kTest() {
        int quantity = 10001;
        api.createOrder("235", quantity, "Buy")
                .assertHttpCode(400)
                .assertMessage("Quantity can't be more or equal than 10000");
    }

    /**
     * Create order with invalid quantity=10000
     * Check code 400
     */
    @Test
    public void createOrderQuantityIs10kTest() {
        int quantity = 10000;
        api.createOrder("235", quantity, "Buy")
                .assertHttpCode(400)
                .assertMessage("Quantity can't be more or equal than 10000");
    }

    /**
     * Get order with id null
     * Check code 400
     */
    @Test
    public void getOrderIdIsNull() {
        api.getOrderById(null)
                .assertHttpCode(400);
    }

    /**
     * Get order id when id is not existed
     * Check code 404
     */
    @Test
    public void getOrderIdNotFoundTest() {
        api.getOrderById("9999")
                .assertHttpCode(404)
                .assertMessage("Order not found");
    }

    /**
     * Get order id when id is invalid (>10000)
     * Check code 400
     */
    @Test
    public void getOrderInvalidIdTest() {
        api.getOrderById("10001")
                .assertHttpCode(400)
                .assertMessage("ID can't be more or equal than 10000");
    }


    /**
     * Delete order with id null
     * Check code 400
     */
    @Test
    public void deleteOrderIdIsNull() {
        api.getOrderById(null)
                .assertHttpCode(400);
    }

    /**
     * Delete order with not existing id
     * CHeck code 404
     */
    @Test
    public void deleteOrderIdNotFoundTest() {
        api.deleteOrderById("9999")
                .assertHttpCode(404);
    }

    /**
     * Create 2 orders
     * Get market data
     * Check code 200
     * Check asks number
     * Check bids number
     */
    @Test
    public void getMarketData() {
        api.createOrder("150", "10", "Buy")
                .assertHttpCode(200)
                .getFieldOrderId();
        api.createOrder("6509.50", "5", "Buy")
                .assertHttpCode(200)
                .getFieldOrderId();
         api.createOrder("150", "10", "Sell")
                .assertHttpCode(200)
                .getFieldOrderId();
        api.createOrder("6509.50", "5", "Sell")
                .assertHttpCode(200)
                .getFieldOrderId();

        // TODO BUG: content-type of response is text/html, should be application/json
        api.getMarketData()
                .assertHttpCode(200)
                .assertAsksNumber(2)
                .assertBidsNumber(2);

        api.getOrderClean()
                .assertHttpCode(200)
                .assertMessage("Order book is clean.");

        api.getMarketData()
                .assertHttpCode(200)
                .assertAsksNumber(0)
                .assertBidsNumber(0);
    }

}