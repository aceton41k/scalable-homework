package scalable.tests.model;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class OrderRequest {
    private String price;
    private String quantity;
    private String side;
}
