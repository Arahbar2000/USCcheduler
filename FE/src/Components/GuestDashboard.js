import React, {useState} from 'react';

const GuestDashboard = () => {
    // displays current chosen courses
    // allows user to add new courses
    // saves course preferences in localStorage so that preferences don't get deleted later on

    const [courses, setCourses] = useState([])

    const addCourse = (courseData) => {
        // given a course, communicates with the server to check if course is valid
        // if valid, display course on dashboard add to courses, and save to localStorage
    }

    const removeCourse = (courseData) => {
        // given a course, removes course from list of courses
    }
}

export default GuestDashboard