package test;

import main.CreateSchedule;
import main.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateScheduleTest{
    User user = null;
    CreateSchedule cs = null;

    @BeforeEach
    void prepareData(){
        user = new User(1, "jiashu", "xu", "jiashuxu@usc.edu", "1");
        cs = new CreateSchedule(user);
    }

    @Test
    void TestAllCourses(){
        System.out.println("heelo");
        System.out.println(cs.all_courses);
//        cs.getSchedules(10);


    }


}