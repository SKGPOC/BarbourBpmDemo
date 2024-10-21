package com.barbour.bpm.demo;

import org.flowable.engine.RuntimeService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.IdmManagementService;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BarbourBpmDemoApplication {

    @Autowired
    private IdmIdentityService idmIdentityService;
    @Autowired
    private RuntimeService runtimeService;

    public static void main(String[] args) {
        SpringApplication.run(BarbourBpmDemoApplication.class, args);
    }

    //@Bean
    public CommandLineRunner init() {

        return new CommandLineRunner() {
            public void run(String... strings) throws Exception {
                if(idmIdentityService.createUserQuery().userId("saibal_ghosh").singleResult()==null) {
                    User user = idmIdentityService.newUser("saibal_ghosh");
                    user.setFirstName("Saibal");
                    user.setLastName("Ghosh");
                    user.setEmail("saibalkumar.ghosh@cognizant.com");
                    user.setDisplayName("Saibal Ghosh");
                    user.setPassword("password");
                    idmIdentityService.saveUser(user);
                }
                if(idmIdentityService.createGroupQuery().groupId("legal_register_task_assignees").singleResult()==null) {
                    Group group = idmIdentityService.newGroup("legal_register_task_assignees");
                    group.setName("Legal Register Task Assignees");
                    group.setType("assignment");
                    idmIdentityService.saveGroup(group);
                    idmIdentityService.createMembership("saibal_ghosh", "legal_register_task_assignees");

                }

            }
        };
    }

}
