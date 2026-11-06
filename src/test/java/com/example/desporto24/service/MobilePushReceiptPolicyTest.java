package com.example.desporto24.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MobilePushReceiptPolicyTest {
    private final ObjectMapper json=new ObjectMapper();

    @Test
    void validatesOnlyRecognizedReceiptOutcomes() throws Exception {
        assertEquals(MobilePushReceiptPolicy.Decision.CONFIRMED,
                MobilePushReceiptPolicy.classify(json.readTree("{\"status\":\"ok\"}")));
        assertEquals(MobilePushReceiptPolicy.Decision.REVOKE_DEVICE,
                MobilePushReceiptPolicy.classify(json.readTree(
                  "{\"status\":\"error\",\"details\":{\"error\":\"DeviceNotRegistered\"}}")));
        assertEquals(MobilePushReceiptPolicy.Decision.FAILED,
                MobilePushReceiptPolicy.classify(json.readTree(
                  "{\"status\":\"error\",\"details\":{\"error\":\"InvalidCredentials\"}}")));
        assertEquals(MobilePushReceiptPolicy.Decision.PENDING,
                MobilePushReceiptPolicy.classify(json.readTree("{}")));
        assertEquals(MobilePushReceiptPolicy.Decision.PENDING,
                MobilePushReceiptPolicy.classify(null));
    }
}
