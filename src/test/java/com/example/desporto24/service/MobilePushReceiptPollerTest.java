package com.example.desporto24.service;

import com.example.desporto24.model.MobilePushReceipt;
import com.example.desporto24.repository.MobilePushDeviceRepository;
import com.example.desporto24.repository.MobilePushReceiptRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MobilePushReceiptPollerTest {
    private final MobilePushReceiptRepository receipts=mock(MobilePushReceiptRepository.class);
    private final MobilePushDeviceRepository devices=mock(MobilePushDeviceRepository.class);
    private final MobilePushReceiptPoller poller=new MobilePushReceiptPoller(receipts,devices,new ObjectMapper());

    @Test
    void remainsDisabledWithoutExplicitConfiguration() {
        poller.checkDueReceipts();
        verifyNoInteractions(receipts,devices);
    }

    @Test
    void missingExpoAccessTokenNeverQueriesProviderOrDevices() {
        ReflectionTestUtils.setField(poller,"enabled",true);
        ReflectionTestUtils.setField(poller,"accessToken","");
        poller.checkDueReceipts();
        verifyNoInteractions(receipts,devices);
    }

    @Test
    void staleTicketsArePrunedWithoutSendingAnotherPush() {
        ReflectionTestUtils.setField(poller,"enabled",true);
        ReflectionTestUtils.setField(poller,"accessToken","test-only");
        var expired=new MobilePushReceipt();
        expired.setTicketId("12345678-1234-4234-8234-123456789012");
        expired.setDeviceId("12345678-1234-4234-8234-123456789012");
        expired.setTokenHash("f".repeat(64));
        expired.setCreatedAt(Instant.now().minus(26,ChronoUnit.HOURS));
        expired.setNextCheckAt(Instant.now().minus(25,ChronoUnit.HOURS));
        when(receipts.findTop100ByNextCheckAtBeforeOrderByNextCheckAtAsc(any()))
            .thenReturn(List.of(expired));
        poller.checkDueReceipts();
        verify(receipts).delete(expired);
        verify(receipts,never()).save(any());
        verifyNoInteractions(devices);
    }

    @Test
    void tokenFingerprintIsDeterministicWithoutStoringRawToken() {
        String hash=MobilePushReceiptPoller.fingerprint("ExponentPushToken[abcdefghijkl12345678]");
        assertEquals(64,hash.length());
        assertEquals(hash,MobilePushReceiptPoller.fingerprint("ExponentPushToken[abcdefghijkl12345678]"));
        assertNotEquals(hash,MobilePushReceiptPoller.fingerprint("ExponentPushToken[other_token_123456789]"));
        assertFalse(hash.contains("ExponentPushToken"));
    }
}
