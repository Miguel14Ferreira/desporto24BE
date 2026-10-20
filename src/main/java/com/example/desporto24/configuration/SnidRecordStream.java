package com.example.desporto24.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Iterates official SNID JSON one installation at a time.
 *
 * A top-level JSON array may contain thousands of installations. Calling
 * ObjectMapper.readTree() on START_ARRAY would load the whole catalogue into
 * memory (and count it as one record), even on a read-only validation request.
 */
final class SnidRecordStream {
    private SnidRecordStream() {}

    static JsonNode nextRecord(JsonParser parser, ObjectMapper mapper) throws IOException {
        JsonToken token;
        while ((token = parser.nextToken()) != null) {
            if (token == JsonToken.START_ARRAY || token == JsonToken.END_ARRAY) {
                continue;
            }
            if (token == JsonToken.START_OBJECT) {
                return mapper.readTree(parser);
            }
            throw new IOException("Unexpected JSON token in SNID record stream: " + token);
        }
        return null;
    }
}
