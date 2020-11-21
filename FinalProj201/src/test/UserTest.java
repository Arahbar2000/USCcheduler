package test;

import main.Preferences;
import main.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User user = null;

    @BeforeEach
    void initUser(){
        user = new User(1, "Jiashu", "xu", "jiashuxu@usc.edu", "1");
    }

    @Test
    void TestUserObject(){
        assertEquals(user.id, 1);
    }

    @Test
    void TestPref(){
        assertEquals(user.prefs.desiredUnits, 18);
        assertEquals(user.prefs.extraCurriculum.size(), 2);
        assertTrue(user.prefs.extraCurriculum.stream().anyMatch(m -> m.containsValue(LocalTime.parse("08:00"))));
        assertTrue(user.prefs.courseList.stream().anyMatch(s -> s.equals("CSCI201")));
        assertTrue(user.prefs.courseList.stream().anyMatch(s -> s.equals("CSCI270")));
    }
}
