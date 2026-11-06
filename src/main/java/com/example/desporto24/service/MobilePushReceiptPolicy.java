package com.example.desporto24.service;

import com.fasterxml.jackson.databind.JsonNode;

public final class MobilePushReceiptPolicy {
    private MobilePushReceiptPolicy() {}
    public enum Decision { CONFIRMED, REVOKE_DEVICE, FAILED, PENDING }
    public static Decision classify(JsonNode receipt) {
        if (receipt == null || receipt.isMissingNode() || receipt.isNull()) return Decision.PENDING;
        String status = receipt.path("status").asText("");
        if ("ok".equals(status)) return Decision.CONFIRMED;
        if ("error".equals(status)) {
            return "DeviceNotRegistered".equals(receipt.path("details").path("error").asText(""))
                    ? Decision.REVOKE_DEVICE : Decision.FAILED;
        }
        return Decision.PENDING;
    }
}
