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
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Create the initial database setup.
 */
@ChangeLog(order = "001")
public class DatabaseInitChangeLog {
    public static final String DOC_ID = "625748988af05121cc0d6189";

    public static final String USER_ID = "361d5d8b5bd5adf7ee8793e5";

    public static final String USER_OTHER_TEAMS_ID = "123321123321123321123321";

    public static final String USER_FOR_CHECK_ID = "aa00bbccddeeff221133aa55";

    public static final String ADMIN_ID = "93e56f6b5bdddf7ee8b5bd5a";

    static final String PATTERN_1_ID = "111111111111111111111111";

    static final String PATTERN_2_ID = "222222222222222222222222";

    static final String PATTERN_3_ID = "333333333333333333333333";

    static final String PATTERN_4_ID = "444444444444444444444444";

    static final String PATTERN_5_ID = "555555555555555555555555";

    static final String PATTERN_6_ID = "666666666666666666666666";

    static final String PATTERN_7_ID = "777777777777777777777777";

    static final String PATTERN_8_ID = "888888888888888888888888";

    private static final String TEAM_PRIVATE = "b5bd5adf7b5bd93e56f6d5d8";

    private static final String TEAM_TEST = "aaccbbffdd11223344556677";

    @ChangeSet(order = "001", id = "init_users", author = "genryxy")
    public void initUsers(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        userRepo.save(
            User.builder()
                .id(new ObjectId(USER_ID))
                .username("user")
                .password(passwordEncoder.encode("user"))
                .role(Role.USER)
                .teams(Sets.newHashSet(commonTeam(), testTeam(), refactoringTeam()))
                .isActive(true)
                .build()
        );
        userRepo.save(
            User.builder()
                .id(new ObjectId(ADMIN_ID))
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .role(Role.ADMIN)
                .teams(Sets.newHashSet(commonTeam()))
                .isActive(true)
                .build()
        );
        userRepo.save(
            User.builder()
                .id(new ObjectId(USER_OTHER_TEAMS_ID))
                .username("username")
                .password(passwordEncoder.encode("username"))
                .role(Role.USER)
                .teams(Sets.newHashSet(commonTeam(), otherTeamPrivate()))
                .isActive(true)
                .build()
        );
        // Only private team without common team for this test user.
        userRepo.save(
            User.builder()
                .id(new ObjectId(USER_FOR_CHECK_ID))
                .username("onlytest")
                .password(passwordEncoder.encode("onlytest"))
                .role(Role.USER)
                .teams(Sets.newHashSet(teamOfUserForCheck()))
                .isActive(true)
                .build()
        );
    }

    @ChangeSet(order = "002", id = "init_teams", author = "genryxy")
    public void initTeams(TeamRepo teamRepo) {
        teamRepo.save(commonTeam());
        teamRepo.save(testTeam());
        teamRepo.save(
            Team.builder()
                .id(new ObjectId(TEAM_PRIVATE))
                .name("team2_private")
                .creatorId(new ObjectId(USER_OTHER_TEAMS_ID))
                .build()
        );
        teamRepo.save(
            Team.builder()
                .id(new ObjectId("bbbbbbbbbbbbbbbbbbbbbbbb"))
                .name("name3")
                .creatorId(new ObjectId(USER_OTHER_TEAMS_ID))
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
                .id(new ObjectId(DOC_ID))
                .team(commonTeam())
                .lang("java")
                .timestamp(LocalDateTime.now().format(DocDto.DATE_FORMATTER).toString())
                .scenario(new Scenario(Scenario.Type.MIGRATION, new HashMap<>()))
                .patterns(
                    Collections.singletonList(
                        Pattern.builder()
                            .id(new ObjectId(PATTERN_8_ID))
                            .authorId(new ObjectId(USER_ID))
                            .data(data)
                            .meta(meta)
                            .build()
                    )
                ).build()
        );
        docRepo.save(
            Doc.builder()
                .team(otherTeamPrivate())
                .lang("java")
                .scenario(new Scenario(Scenario.Type.MIGRATION, new HashMap<>()))
                .patterns(
                    Arrays.asList(
                        Pattern.builder()
                            .id(new ObjectId(PATTERN_6_ID))
                            .authorId(new ObjectId(USER_ID))
                            .data(data)
                            .meta(meta)
                            .build(),
                        Pattern.builder()
                            .id(new ObjectId(PATTERN_7_ID))
                            .authorId(new ObjectId(USER_ID))
                            .data(data)
                            .meta(meta)
                            .build()
                    )
                ).build()
        );
        docRepo.save(
            Doc.builder()
                .team(refactoringTeam())
                .lang("java")
                .scenario(new Scenario(Scenario.Type.REFACTORING, new HashMap<>()))
                .patterns(
                    Arrays.asList(
                        Pattern.builder()
                            .id(new ObjectId(PATTERN_4_ID))
                            .authorId(new ObjectId(USER_ID))
                            .data(data)
                            .meta(meta)
                            .build(),
                        Pattern.builder()
                            .id(new ObjectId(PATTERN_5_ID))
                            .authorId(new ObjectId(USER_ID))
                            .data(data)
                            .meta(meta)
                            .build()
                    )
                ).build()
        );
        docRepo.save(
            Doc.builder()
                .team(teamOfUserForCheck())
                .lang("java")
                .scenario(new Scenario(Scenario.Type.MIGRATION, new HashMap<>()))
                .patterns(new ArrayList<>())
                .build()
        );
        docRepo.save(
            Doc.builder()
                .team(teamOfUserForCheck())
                .lang("js")
                .scenario(new Scenario(Scenario.Type.UNKNOWN, new HashMap<>()))
                .patterns(new ArrayList<>())
                .build()
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
                .id(new ObjectId(PATTERN_1_ID))
                .authorId(new ObjectId(USER_ID))
                .data(data)
                .meta(meta)
                .build()
        );
        patternRepo.save(
            Pattern.builder()
                .id(new ObjectId(PATTERN_2_ID))
                .authorId(new ObjectId(USER_ID))
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
                .id(new ObjectId(PATTERN_3_ID))
                .authorId(new ObjectId(USER_ID))
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
        docRepo.save(
            Doc.builder()
                .team(testTeam())
                .lang("java")
                .scenario(new Scenario(Scenario.Type.FOR_TEST, new HashMap<>()))
                .patterns(
                    Collections.singletonList(
                        Pattern.builder()
                            .id(new ObjectId("222222111111111111111111"))
                            .authorId(new ObjectId(USER_ID))
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
                .id(new ObjectId(ADMIN_ID))
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
            .id(Team.COMMON_TEAM_ID)
            .name("team1_common")
            .creatorId(new ObjectId(USER_ID))
            .build();
    }

    private static Team testTeam() {
        Team teamTest = new Team();
        teamTest.setId(new ObjectId(TEAM_TEST));
        teamTest.setName("test team");
        teamTest.setCreatorId(new ObjectId(USER_ID));
        teamTest.setInvitation("some invite uuid code for test team");
        return teamTest;
    }

    private static Team refactoringTeam() {
        Team teamRef = new Team();
        teamRef.setId(new ObjectId("eeeaaadddbbbccc111222333"));
        teamRef.setName("refactoring team");
        teamRef.setCreatorId(new ObjectId(USER_ID));
        teamRef.setInvitation("some invite uuid code");
        return teamRef;
    }

    private static Team otherTeamPrivate() {
        return Team.builder()
            .id(new ObjectId(TEAM_PRIVATE))
            .name("team2_private")
            .creatorId(new ObjectId(USER_OTHER_TEAMS_ID))
            .build();
    }

    private static Team teamOfUserForCheck() {
        return Team.builder()
            .id(new ObjectId("c2ec7c2ec7c2ec7c2ec74440"))
            .name("team only for user check")
            .creatorId(new ObjectId(USER_FOR_CHECK_ID))
            .build();
    }
}
