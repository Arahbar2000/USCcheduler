package test;

import main.CreateSchedule;
import main.Schedule;
import main.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(cs.all_courses.size(), 15);
        assertEquals(cs.all_courses.stream().
                filter(c ->
                        c.sectionType.equals("Lec") && c.courseNumber == 201
                ).count(), 1);
    }

    @Test
    void TestGetSchedule(){
        for (int i = 1; i < 6; i++){
            List<Schedule> scheduleList = cs.getSchedules(i);
            assertTrue(scheduleList.stream().allMatch(
                    schedule -> schedule.decidedClasses
                            .stream()
                            .anyMatch(course -> course.sectionType.equals("Lec"))
            ));

            System.out.println("+++++++" + i + "+++++++");
            printSchedule(scheduleList);
            System.out.println();
        }
    }

    void printSchedule(List<Schedule> schedules){
        schedules.stream().forEach(schedule -> {
            schedule.decidedClasses.stream().sorted(
                    (c1, c2)-> c1.startTime != null && c2.startTime != null ?
                            c1.startTime.compareTo(c2.startTime) : 0
            ).forEach(System.out::println);
        });
    }

}