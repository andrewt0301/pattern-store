package aakrasnov.diploma.service.config.dbmigrations;

import aakrasnov.diploma.common.DocDto;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Creates the initial database setup.
 */
@ChangeLog(order = "001")
public class DatabaseInitChangeLog {
    public static final String USER_ID = "361d5d8b5bd5adf7ee8793e5";

    public static final String ADMIN_ID = "93e56f6b5bdddf7ee8b5bd5a";

    private static final String TEAM_PRIVATE = "b5bd5adf7b5bd93e56f6d5d8";

    @ChangeSet(order = "001", id = "init_users", author = "genryxy")
    public void initUsers(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        userRepo.save(
            User.builder()
                .id(strObjId(USER_ID))
                .username("user")
                .password(passwordEncoder.encode("user"))
                .role(Role.USER)
                .teams(Collections.singleton(commonTeam()))
                .isActive(true)
                .build()
        );
        userRepo.save(
            User.builder()
                .id(strObjId("2"))
                .username("username")
                .password(passwordEncoder.encode("username"))
                .role(Role.USER)
                .isActive(true)
                .build()
        );
        userRepo.save(
            User.builder()
                .username("123")
                .password(passwordEncoder.encode("123"))
                .role(Role.USER)
                .isActive(true)
                .build()
        );
    }

    @ChangeSet(order = "002", id = "init_teams", author = "genryxy")
    public void initTeams(TeamRepo teamRepo) {
        teamRepo.save(commonTeam());
        teamRepo.save(
            Team.builder()
                .id(strObjId(TEAM_PRIVATE))
                .name("team2_private")
                .creatorId(USER_ID)
                .build()
        );
        teamRepo.save(
            Team.builder()
                .id("3")
                .name("name3")
                .creatorId(USER_ID)
                .invitation("invit-uuid-name3")
                .build()
        );
    }

    @ChangeSet(order = "003", id = "init_docs", author = "genryxy")
    public void initDocs(DocRepo docRepo) {
        Map<String, String> data = ImmutableMap.of("data", "here should be json with of pattern");
        Map<String, String> meta = new HashMap<>();
        meta.put("versionFrom", "0.0.9");
        meta.put("versionTo", "1.0.0");
        docRepo.save(
            Doc.builder()
                .id(strObjId("625748988af05121cc0d6189"))
                .team(commonTeam())
                .lang("java")
                .timestamp(LocalDateTime.now().format(DocDto.DATE_FORMATTER).toString())
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
                .team(
                    Team.builder().id("2")
                        .creatorId("2")
                        .name("team2").build()
                ).lang("java")
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
        Team teamRef = new Team();
        teamRef.setName("refactoring team");
        teamRef.setCreatorId(USER_ID);
        teamRef.setInvitation("some invite uuid code");
        docRepo.save(
            Doc.builder()
                .team(teamRef)
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

    @ChangeSet(order = "005", id = "init_docs_test", author = "genryxy")
    public void initDocsTest(DocRepo docRepo) {
        Map<String, String> data = ImmutableMap.of("data", "some json with pattern");
        Map<String, String> meta = new HashMap<>();
        meta.put("artifactIdFrom", "artifactIdOld");
        meta.put("artifactIdTo", "artifactIdNew");
        meta.put("versionFrom", "0.0.9");
        meta.put("versionTo", "1.0.0");
        Team teamTest = new Team();
        teamTest.setName("test team");
        teamTest.setCreatorId(USER_ID);
        teamTest.setInvitation("some invite uuid code for test team");
        docRepo.save(
            Doc.builder()
                .team(teamTest)
                .lang("java")
                .scenario(new Scenario(Scenario.Type.FOR_TEST, new HashMap<>()))
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
    }

    @ChangeSet(order = "006", id = "init_admin_pswd_encrypted", author = "genryxy")
    public void initAdminWithEncryptedPswd(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        Set<Team> teams = new HashSet<>();
        teams.add(commonTeam());
        userRepo.save(
            User.builder()
                .id(strObjId(ADMIN_ID))
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .role(Role.ADMIN)
                .teams(teams)
                .isActive(true)
                .build()
        );
    }

    private static Team commonTeam() {
        return Team.builder()
            .id(strObjId(Team.COMMON_TEAM_ID.toString()))
            .name("team1_common")
            .creatorId(USER_ID)
            .build();
    }

    private static String strObjId(String id) {
        if (id.length() > 2) {
            System.out.println("--------------tmp----------------");
            System.out.println(new ObjectId(id).toHexString());
            return new ObjectId(id).toString();
        }
        return id;
    }
}
