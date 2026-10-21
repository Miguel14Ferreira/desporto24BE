package com.example.desporto24.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SnidLocationResourceSelectorTest {

    private static Map<String, Object> resource(String title, String format,
                                                  Object size, String url) {
        var value = new java.util.HashMap<String, Object>();
        value.put("title", title);
        value.put("format", format);
        if (size != null) value.put("filesize", size);
        value.put("url", url);
        return value;
    }

    @Test
    void recognizesOfficialDatasetTitleWithAccentsAndCaseVariants() {
        assertTrue(SnidLocationResourceSelector.matchesDatasetTitle(
                "Sistema Nacional de Informação Desportiva"));
        assertTrue(SnidLocationResourceSelector.matchesDatasetTitle(
                "SISTEMA NACIONAL DE INFORMACAO DESPORTIVA - SNID"));
        assertFalse(SnidLocationResourceSelector.matchesDatasetTitle("Outra fonte desportiva"));
    }

    @Test
    void acceptsNamedOfficialJsonWhenSizeMetadataIsMissing() {
        String url = "https://dados.gov.pt/s/resources/snid-localizacao.json";
        assertEquals(url, SnidLocationResourceSelector.findLocationJsonUrl(List.of(
                resource("SNID - Localização.json", "JSON", null, url))));
        assertEquals(url, SnidLocationResourceSelector.findLocationJsonUrl(List.of(
                resource("snid_localizacao.json", null, 123, url))));
    }

    @Test
    void rejectsExplicitEmptySourceAndUnrelatedJsonResources() {
        String url = "https://dados.gov.pt/s/resources/snid-localizacao.json";
        assertNull(SnidLocationResourceSelector.findLocationJsonUrl(List.of(
                resource("snid-localizacao.json", "json", 0, url),
                resource("snid-demo.json", "json", 1234, url),
                resource("part-1.json", "json", 1234, url))));
    }

    @Test
    void rejectsUntrustedDomainsAndNonJsonFilesEvenWithValidMetadata() {
        String source = "https://dados.gov.pt/s/resources/snid-localizacao.csv";
        assertNull(SnidLocationResourceSelector.findLocationJsonUrl(List.of(
                resource("snid-localizacao.json", "JSON", 1200, "https://evil.example/fixture.json"),
                resource("snid-localizacao.json", "JSON", 1200,
                        "https://dados.gov.pt.evil.example/fixture.json"),
                resource("snid-localizacao.json", "csv", 1200, source),
                resource("snid-localizacao.json", "json", 1200,
                        "http://dados.gov.pt/fixture.json"))));
    }

    @Test
    void missingOfficialResourceReturnsExplicitReadOnlyStatusInsteadOfImporting() {
        assertNull(SnidLocationResourceSelector.findLocationJsonUrl(List.of()));
        assertEquals(Map.of(
                "status", "SOURCE_NOT_AVAILABLE",
                "available", false,
                "readOnly", true,
                "source", "dados.gov.pt",
                "reason", "SNID_LOCATION_JSON_NOT_FOUND"),
                SnidVenueImportData.unavailableLocationJson());
    }

    @Test
    void supportsMimeTypeJsonWhenFormatMetadataIsOmitted() {
        String url = "https://dados.gov.pt/s/resources/location-data";
        Map<String,Object> record = new java.util.HashMap<>();
        record.put("title","SNID Localização");
        record.put("mime_type","application/json");
        record.put("url",url);
        assertEquals(url, SnidLocationResourceSelector.findLocationJsonUrl(List.of(record)));
    }

    @Test
    void refusesInjectedUrlsWithoutContactingRemoteResource() {
        for (String url : List.of(
                "https://user@dados.gov.pt/file.json",
                "https://dados.gov.pt:8443/file.json",
                "https://dados.gov.pt/file.json#attack",
                "file:///etc/passwd",
                "not a url")) {
            assertFalse(SnidLocationResourceSelector.isOfficialHttpsUrl(url));
        }
    }
}
