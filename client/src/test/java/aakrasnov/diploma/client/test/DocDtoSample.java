package aakrasnov.diploma.client.test;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.ScenarioDto;
import aakrasnov.diploma.common.TeamDto;
import java.util.HashMap;

public class DocDtoSample {
    public static DocDto withId(String docId) {
        return DocDto.builder()
            .id(docId)
            .scenario(new ScenarioDto(ScenarioDto.Type.FOR_TEST, new HashMap<>()))
            .team(
                TeamDto.builder()
                    .id("741852963741852963741852")
                    .name("team_super_name")
                    .creatorId("bcaabccbaabccbaabccbaabc")
                    .build()
            )
            .lang("java")
            .build();
    }

    public static DocDto withIdAndTimestamp(String docId, String timestamp) {
        return DocDto.builder()
            .id(docId)
            .scenario(new ScenarioDto(ScenarioDto.Type.FOR_TEST, new HashMap<>()))
            .team(new TeamDto())
            .timestamp(timestamp)
            .lang("java")
            .build();
    }
}
