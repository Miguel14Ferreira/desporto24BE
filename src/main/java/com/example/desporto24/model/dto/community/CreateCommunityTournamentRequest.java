package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import com.example.desporto24.enumeration.TournamentEntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommunityTournamentRequest(
        @NotBlank @Size(max = 120) String name,
        @Min(0) Integer winPoints,
        @Min(0) Integer drawPoints,
        @Min(0) Integer lossPoints,
        @Size(max = 100) String sport,
        TournamentEntryType entryType,
        @Min(1) @Max(30) Integer playersPerTeam,
        @Min(1) @Max(2) Integer roundRobinLegs
) {
    // Backwards compatibility for existing controller/service unit tests.
    public CreateCommunityTournamentRequest(String name, Integer winPoints, Integer drawPoints,
                                             Integer lossPoints, String sport,
                                             TournamentEntryType entryType, Integer playersPerTeam) {
        this(name, winPoints, drawPoints, lossPoints, sport, entryType, playersPerTeam, null);
    }

    public CreateCommunityTournamentRequest(String name, Integer winPoints,
                                             Integer drawPoints, Integer lossPoints, String sport) {
        this(name, winPoints, drawPoints, lossPoints, sport, null, null, null);
    }

    // Preserve clients and tests which construct the original four-field request.
    public CreateCommunityTournamentRequest(String name, Integer winPoints,
                                             Integer drawPoints, Integer lossPoints) {
        this(name, winPoints, drawPoints, lossPoints, null, null, null, null);
    }
}
