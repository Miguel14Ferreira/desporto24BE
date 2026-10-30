package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTaekwondoResultsTest {
    private CommunityRoundEntryRequest competitor(String name, String side,
            CommunityMatchResult result, String number) {
        return new CommunityRoundEntryRequest(name, side, result,
                0, 0, false, false, null, null, null, null,
                number == null ? null : new BigDecimal(number));
    }
    private final CommunityMatchResult W = CommunityMatchResult.WIN;
    private final CommunityMatchResult L = CommunityMatchResult.LOSS;
    private final CommunityMatchResult D = CommunityMatchResult.DRAW;
    private List<CommunityRoundEntryRequest> bout(String b, String r,
            CommunityMatchResult blue, CommunityMatchResult red) {
        return List.of(competitor("blue", "CHUNG", blue, b),
                competitor("red", "HONG", red, r));
    }

    @Test void onlyRecognizesExistingCatalogueTaekwondoVariants() {
        for (String type : new String[] {"TKD_KYORUGI_BEST3",
                "TKD_POOMSAE_RECOGNIZED", "TKD_POOMSAE_FREESTYLE"}) {
            assertEquals(type, CommunityRoundEventType.validate("Taekwondo", type));
            assertTrue(CommunityRoundEventType.isNeutralEvent("Taekwondo", type));
            assertFalse(CommunityRoundEventType.isRaceEvent("Taekwondo", type));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Taekwondo", null));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Judo", "TKD_KYORUGI_BEST3"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validate("Taekwondo", "TKD_ITF_SPAR"));
    }

    @Test void completedKyorugiRequiresTwoRoundWinsAndCorrectSides() {
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Taekwondo",
                "TKD_KYORUGI_BEST3", bout("2","0",W,L)));
        assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries("Taekwondo",
                "TKD_KYORUGI_BEST3", bout("1","2",L,W)));
        for (String[] bad : new String[][] {{"1","1"},{"2","2"},{"3","0"},
                {"1.5","0"},{"-1","2"},{"0","0"},{null,"0"}}) {
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Taekwondo",
                            "TKD_KYORUGI_BEST3", bout(bad[0],bad[1],W,L)));
        }
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Taekwondo",
                        "TKD_KYORUGI_BEST3", bout("2","1",W,W)));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Taekwondo",
                        "TKD_KYORUGI_BEST3", List.of(
                                competitor("blue","CHUNG",W,"2"),
                                competitor("alsoBlue","CHUNG",L,"0"))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Taekwondo",
                        "TKD_KYORUGI_BEST3", List.of(
                                competitor("blue","CHUNG",W,"2"))));
    }

    @Test void recognizedAndFreestyleUseFinalJudgesMarkNotMadeUpWdl() {
        for (String type : new String[] {"TKD_POOMSAE_RECOGNIZED",
                "TKD_POOMSAE_FREESTYLE"}) {
            assertDoesNotThrow(() -> CommunityRoundEventType.validateEntries(
                    "Taekwondo", type,
                    List.of(competitor("first",null,D,"9.125"),
                            competitor("second",null,D,"9.125"),
                            competitor("third",null,D,"0"))));
            for (String bad : new String[] {null,"-1","10.001","9.1234"}) {
                assertThrows(IllegalArgumentException.class,
                        () -> CommunityRoundEventType.validateEntries("Taekwondo",
                                type,List.of(competitor("p",null,D,bad))), "bad="+bad);
            }
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Taekwondo",type,
                            List.of(competitor("p",null,W,"8.5"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Taekwondo",type,
                            List.of(competitor("p","A",D,"8.5"))));
            assertThrows(IllegalArgumentException.class,
                    () -> CommunityRoundEventType.validateEntries("Taekwondo",type,
                            List.of(competitor("same",null,D,"7"),
                                    competitor("same",null,D,"8"))));
        }
    }

    @Test void rejectsOtherSportsAndUnrelatedMetrics() {
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Futebol",
                        "TKD_KYORUGI_BEST3",bout("2","0",W,L)));
        var invalid = new CommunityRoundEntryRequest("blue","CHUNG",W,
                1,0,false,false,null,null,null,null,new BigDecimal("2"));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityRoundEventType.validateEntries("Taekwondo",
                        "TKD_KYORUGI_BEST3",List.of(invalid,
                                competitor("red","HONG",L,"0"))));
    }
}
