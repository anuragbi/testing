package com.test.weather.coolesthour;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.weather.information.RestServiceApplication;


@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = RestServiceApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)

public class CoolestHourApplicationTests {
    @Autowired
    ApplicationContext ctx;

    @Test
    public void testRun() {
        //  String[] arg = {"94043"};
        //CoolestHourApplication.main(arg);
    }


}
