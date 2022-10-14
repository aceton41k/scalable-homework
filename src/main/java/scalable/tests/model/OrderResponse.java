package scalable.tests.model;

import lombok.Getter;

@Getter
public class OrderResponse {
    private String id;
    private String price;
    private String quantity;
    private String side;
}