package scalable.tests;

import lombok.Getter;

import java.util.List;
@Getter
public class MarketDataResponse {
    public List<Ask> asks;
    public List<String> bids;

}
