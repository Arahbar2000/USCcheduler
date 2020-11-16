import React, { useState } from 'react';

const AuthDashboard = () => {
    // displays current courses and friends
    // allows user to add new friends and courses

    const [courses, setCourses] = useState([])
    const [friends, setFriends] = useState([])

    const addCourse = (courseData) => {
        // given a course, communicates with the server to check if course is valid
        // if valid, display course on dashboard add to courses, and save to localStorage
    }

    const removeCourse = (courseData) => {
        // given a course, removes course from list of courses
    }

    const addFriend = (friendData) => {
        // given a friend's info, communicate with database to check if friend exists and also add it to the database if it exists
    }

    const removeFriend = (friendData) => {
        // given a friend's info, communicate with database to check if friend exists and also remove it from database if it exists
    }
}

export default AuthDashboard
