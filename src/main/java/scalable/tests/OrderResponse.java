package scalable.tests;

import lombok.Getter;

@Getter
public class OrderResponse {
    private String id;
    private String price;
    private String quantity;
    private String side;
}