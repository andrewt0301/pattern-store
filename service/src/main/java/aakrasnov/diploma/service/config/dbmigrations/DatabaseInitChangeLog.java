package aakrasnov.diploma.service.config.dbmigrations;

import aakrasnov.diploma.service.domain.Doc;
import aakrasnov.diploma.service.domain.Pattern;
import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.Scenario;
import aakrasnov.diploma.service.domain.Team;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.repo.DocRepo;
import aakrasnov.diploma.service.repo.PatternRepo;
import aakrasnov.diploma.service.repo.TeamRepo;
import aakrasnov.diploma.service.repo.UserRepo;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 * Creates the initial database setup.
 */
@ChangeLog(order = "001")
public class DatabaseInitChangeLog {
    @ChangeSet(order = "001", id = "init_users", author = "genryxy")
    public void initUsers(UserRepo userRepo) {
        userRepo.save(
            User.builder()
                .id(strObjId("1"))
                .username("username1")
                .password("should be encrypted")
                .role(Role.ADMIN)
                .isActive(true)
                .build()
        );
        userRepo.save(
            User.builder()
                .id(strObjId("2"))
                .username("user")
                .password("should be encrypted")
                .role(Role.ADMIN)
                .isActive(true)
                .build()
        );
        userRepo.save(
            User.builder()
                .username("123")
                .password("123 should be encrypted")
                .role(Role.USER)
                .isActive(true)
                .build()
        );
    }

    @ChangeSet(order = "002", id = "init_teams", author = "genryxy")
    public void initTeams(TeamRepo teamRepo) {
        teamRepo.save(
            Team.builder()
                .id(strObjId("624f6f6b5bdddf7ee83350a0"))
                .name("name1")
                .build()
        );
        teamRepo.save(
            Team.builder()
                .id(strObjId("2"))
                .name("name2")
                .build()
        );
        teamRepo.save(
            Team.builder()
                .id("3")
                .name("name3")
                .invitation("invit-uuid-name3")
                .build()
        );
    }

    @ChangeSet(order = "003", id = "init_docs_migration", author = "genryxy")
    public void initDocsMigration(DocRepo docRepo) {
        Map<String, String> data = ImmutableMap.of("data", "here should be json with of pattern");
        Map<String, String> meta = new HashMap<>();
        meta.put("versionFrom", "0.0.9");
        meta.put("versionTo", "1.0.0");
        docRepo.save(
            Doc.builder()
                .team(Team.builder().id(Team.COMMON_OBJ_ID.toString()).name("team1").build())
                .lang("java")
                .scenario(new Scenario(Scenario.Type.MIGRATION, new HashMap<>()))
                .patterns(
                    Collections.singletonList(
                        Pattern.builder()
                            .id(strObjId("1"))
                            .authorId("1")
                            .data(data)
                            .meta(meta)
                            .build()
                    )
                ).build()
        );
        docRepo.save(
            Doc.builder()
                .team(Team.builder().id("2").name("team2").build())
                .lang("java")
                .scenario(new Scenario(Scenario.Type.MIGRATION, new HashMap<>()))
                .patterns(
                    Arrays.asList(
                        Pattern.builder()
                            .id(strObjId("1"))
                            .authorId("1")
                            .data(data)
                            .meta(meta)
                            .build(),
                        Pattern.builder()
                            .id(strObjId("2"))
                            .authorId("1")
                            .data(data)
                            .meta(meta)
                            .build()
                    )
                ).build()
        );
    }

    @ChangeSet(order = "004", id = "init_patterns", author = "genryxy")
    public void initPatterns(PatternRepo patternRepo) {
        Map<String, String> data = ImmutableMap.of("data", "here should be json with of pattern");
        Map<String, String> meta = new HashMap<>();
        meta.put("versionFrom", "0.0.9");
        meta.put("versionTo", "1.0.0");
        patternRepo.save(
            Pattern.builder()
                .id(strObjId("1"))
                .authorId("1")
                .data(data)
                .meta(meta)
                .build()
        );
        patternRepo.save(
            Pattern.builder()
                .id(strObjId("2"))
                .authorId("1")
                .data(data)
                .meta(meta)
                .build()
        );
        Map<String, Object> data3 = new HashMap<>();
        Map<String, String> meta3 = new HashMap<>();
        new HashMap<String, String>() {{
            put("", "");
        }};
        data3.put(
            "data", new Gson().toJson(
                ImmutableMap.builder()
                    .put("node1", ImmutableMap.of("node2", "val"))
                    .build()
            )
        );
        meta3.put("versionFrom", "1.2.3");
        meta3.put("versionTo", "2.0.1");
        patternRepo.save(
            Pattern.builder()
                .id(strObjId("3"))
                .authorId("2")
                .data(data3)
                .meta(meta3)
                .build()
        );
    }

    @ChangeSet(order = "005", id = "init_docs_refactoring", author = "genryxy")
    public void initDocsRefactoring(DocRepo docRepo) {
        Map<String, String> data = ImmutableMap.of("data", "some json with pattern");
        Map<String, String> meta = new HashMap<>();
        meta.put("artifactIdFrom", "artifactIdOld");
        meta.put("artifactIdTo", "artifactIdNew");
        meta.put("versionFrom", "0.0.9");
        meta.put("versionTo", "1.0.0");
        docRepo.save(
            Doc.builder()
                .team(Team.builder().id(Team.COMMON_OBJ_ID.toString()).name("team1").build())
                .lang("java")
                .scenario(new Scenario(Scenario.Type.REFACTORING, new HashMap<>()))
                .patterns(
                    Arrays.asList(
                        Pattern.builder()
                            .id(strObjId("1"))
                            .authorId("1")
                            .data(data)
                            .meta(meta)
                            .build(),
                        Pattern.builder()
                            .id("2")
                            .authorId("1")
                            .data(data)
                            .meta(meta)
                            .build()
                    )
                ).build()
        );
    }

    private static String strObjId(String id) {
        if (id.length() > 2) {
            System.out.println("--------------tmp----------------");
            System.out.println(new ObjectId(id).toString());
            System.out.println(new ObjectId(id).toHexString());
            return new ObjectId(id).toString();
        }
        return id;
    }
}
