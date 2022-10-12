package scalable.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class OrderApiController {
    static final String baseUrl = PropertyReader.getProperties().getProperty("url");
    RequestSpecification reqSpec = new RequestSpecBuilder()
            .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()))
            .setContentType(ContentType.JSON)
            .setBaseUri(baseUrl)
            .setBasePath("/api")
            .build();


    Response response;

    public Response getResponse() {
        return response;
    }

    public OrderApiController getOrderById(String id) {
        response = given()
                .spec(reqSpec)
                .queryParam("id", id)
                .get("/order");
        return this;
    }

    public OrderApiController deleteOrderById(String id) {
        response = given()
                .spec(reqSpec)
                .queryParam("id", id)
                .delete("/order");
        return this;
    }

    public OrderApiController getOrderClean() {
        response = given()
                .spec(reqSpec)
                .get("/order/clean");
        return this;
    }

    public OrderApiController createOrder(Object price, Object quantity, String side) {
        Map<String, Object> map = new HashMap<>() {{
            put("price", price);
            put("quantity", quantity);
            put("side", side);
        }};
        response = given()
                .spec(reqSpec)
                .body(map)
                .post("/order/create");
        return this;
    }
    public OrderApiController getMarketData() {
        response = given()
                .spec(reqSpec)
                .get("/marketdata");
        return this;
    }

    public OrderApiController assertAsksNumber(int expectedSize) {
        assertEquals(getResponse().as(MarketDataResponse.class).getAsks().size(), expectedSize, "Asks size");
        return this;
    }

    public OrderApiController assertBidsNumber(int expectedSize) {
        assertEquals(response.as(MarketDataResponse.class).getBids().size(), expectedSize, "Bids size");
        return this;
    }

    public OrderApiController assertHttpCode(int expectedHttpCode) {
        assertEquals(getResponse().statusCode(), expectedHttpCode, "HTTP Code");
        return this;
    }

    public OrderApiController assertOrderPrice(Object expectedPrice) {
        assertEquals(response.as(OrderResponse.class).getPrice(), expectedPrice, "Price");
        return this;
    }

    public OrderApiController assertOrderId(String expectedId) {
        assertEquals(response.as(OrderResponse.class).getId(), expectedId, "Id");
        return this;
    }

    public OrderApiController assertOrderIdNotNull() {
        assertNotNull(response.as(OrderResponse.class).getId(), "Id");
        return this;
    }

    public OrderApiController assertOrderSide(String expectedSide) {
        assertEquals(response.as(OrderResponse.class).getSide(), expectedSide, "Side");
        return this;
    }

    public OrderApiController assertMessage(String expectedMessage) {
        assertEquals(response.as(MessageResponse.class).getMessage(), expectedMessage, "Message");
        return this;
    }


    public OrderApiController assertOrderQuantity(String expectedQuantity) {
        assertEquals(response.as(OrderResponse.class).getQuantity(), expectedQuantity, "Quantity");
        return this;
    }

    public String getFieldOrderId() {
        return response.as(OrderResponse.class).getId();
    }

}


