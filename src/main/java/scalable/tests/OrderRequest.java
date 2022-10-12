package scalable.tests;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class OrderRequest {
    private String price;
    private String quantity;
    private String side;
}
