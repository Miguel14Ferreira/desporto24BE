package com.example.desporto24.domain;

import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.Locale;
import com.fasterxml.jackson.annotation.JsonInclude;

/** Backwards-compatible HTTP error shape with additive import diagnostic metadata. */
public final class CommunityImportHttpResponse extends HttpResponse {
    private final String code;
    private final Map<String, String> params;

    public CommunityImportHttpResponse(HttpStatus status, String message, String code, Map<String, String> params) {
        super(status.value(), status, status.getReasonPhrase().toUpperCase(Locale.ROOT), message);
        this.code = code;
        this.params = params == null ? Map.of() : Map.copyOf(params);
    }

    public String getCode() { return code; }
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public Map<String, String> getParams() { return params; }
}
