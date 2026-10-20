package com.example.desporto24.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SnidRecordStreamTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void streamsArrayRecordByRecordRatherThanMaterializingEntireCatalogue() throws Exception {
        String json = "[{\"id\":1,\"name\":\"Recinto A\"},{\"id\":2,\"name\":\"Recinto B\"}]";
        try (var parser = mapper.getFactory().createParser(
                json.getBytes(StandardCharsets.UTF_8))) {
            JsonNode first = SnidRecordStream.nextRecord(parser, mapper);
            assertTrue(first.isObject());
            assertEquals("Recinto A", first.path("name").asText());
            // The first call must not consume the second installation.
            assertEquals("Recinto B",
                    SnidRecordStream.nextRecord(parser, mapper).path("name").asText());
            assertNull(SnidRecordStream.nextRecord(parser, mapper));
        }
    }

    @Test
    void acceptsSingleObjectAndEmptyListWithoutAssumingWholeArrayIsOneVenue() throws Exception {
        try (var object = mapper.getFactory().createParser("{\"name\":\"Recinto\"}")) {
            assertEquals("Recinto", SnidRecordStream.nextRecord(object, mapper).path("name").asText());
            assertNull(SnidRecordStream.nextRecord(object, mapper));
        }
        try (var empty = mapper.getFactory().createParser("[]")) {
            assertNull(SnidRecordStream.nextRecord(empty, mapper));
        }
    }

    @Test
    void rejectsUnexpectedPrimitiveInsteadOfReportingFalseSourceCoverage() throws Exception {
        try (var parser = mapper.getFactory().createParser("[{\"name\":\"Válido\"},7]")) {
            assertEquals("Válido", SnidRecordStream.nextRecord(parser, mapper).path("name").asText());
            assertThrows(IOException.class, () -> SnidRecordStream.nextRecord(parser, mapper));
        }
    }
}
