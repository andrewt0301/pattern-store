package aakrasnov.diploma.service.config.dbmigrations;

import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.repo.UserRepo;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;

/**
 * Creates the initial database setup.
 */
@ChangeLog(order = "001")
public class DatabaseInitChangeLog {
    @ChangeSet(order = "001", id = "init_users", author = "genryxy")
    public void initUsers(UserRepo userRepo) {
        userRepo.save(
            User.builder()
                .username("username1")
                .password("should be encrypted")
                .role(Role.ADMIN)
                .isActive(true)
                .build()
        );
    }
}
