package com.laioffer.twitch;


import com.laioffer.twitch.db.UserRepository;
import com.laioffer.twitch.db.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class DevelopmentTester implements ApplicationRunner {


    private static final Logger logger = LoggerFactory.getLogger(DevelopmentTester.class);


    private final UserRepository userRepository;


    public DevelopmentTester(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    @Override
    public void run(ApplicationArguments args) {
        UserEntity newUser = new UserEntity(null, "user0", "Foo", "Bar", "password");
        userRepository.save(newUser);
    }
}
