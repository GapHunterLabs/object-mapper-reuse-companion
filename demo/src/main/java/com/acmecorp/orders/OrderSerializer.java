package com.acmecorp.orders;

public class OrderSerializer {

    private final ObjectMapper sharedMapper;

    // Built once, in the constructor -- not flagged.
    OrderSerializer() {
        this.sharedMapper = new ObjectMapper();
    }

    // Built again on every call inside a regular method -- flagged.
    String serialize(Order order) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(order);
    }
}
