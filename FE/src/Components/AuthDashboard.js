import React, { useState, useEffect } from "react";
import Form from "react-bootstrap/Form";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";

const AuthDashboard = () => {
    // displays current chosen courses
  // allows user to add new courses
  // saves course preferences in localStorage so that preferences don't get deleted later on

  const [courses, setCourses] = useState([]);
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [changedPreferences, setChanged] = useState(false)

  useEffect(() => {
    let courseData = JSON.parse(localStorage.getItem("courses"));
    let startTimeData = localStorage.getItem("startTime");
    let endTimeData = localStorage.getItem("endTime");
    if (courseData != null) {
      setCourses(courseData)
    }
    if (startTimeData != null) {
      setStartTime(startTimeData);
    }
    if (endTimeData != null) {
      setEndTime(endTimeData);
    }
  }, [changedPreferences])

  const addCourse = (event) => {
    event.preventDefault();
    // given a course, communicates with the server to check if course is valid
    // if valid, display course on dashboard add to courses, and save to localStorage
    const courseName = event.target.elements.add.value.toUpperCase();
    const department = courseName.slice(0, 4);
    const courseNumber = parseInt(courseName.slice(4, courseName.length));
    verifyCourse(department, courseNumber)
    .then(() => {
      // add course to storage
      let coursesData = JSON.parse(localStorage.getItem("courses"));
      if (coursesData == null) {
        coursesData = [{department, courseNumber}]
      }
      else {
        coursesData.push({department, courseNumber})
      }
      localStorage.setItem("courses", JSON.stringify(coursesData))
      setChanged(!changedPreferences)
    })
    .catch(() => {
      alert("Invalid course.");
    });
  };

  const verifyCourse = async (department, courseNumber) => {
    const url = new URL("http://localhost:8080/cs201/api/courses")
    url.search = new URLSearchParams({department, courseNumber})
    await fetch(url, {
            method: "GET",
        }).then(response => response.json())
        .then(() => {
            return true;
        })
        .catch(() => {
          // testing purposes, later change to false
            return true;
        });
  };

  const removeCourse = (event) => {
    event.preventDefault();
    const courseName = event.target.elements.remove.value.toUpperCase();
    const department = courseName.slice(0, 4);
    const courseNumber = parseInt(courseName.slice(4, courseName.length));
    // given a course, removes course from list of courses
    let coursesData = JSON.parse(localStorage.getItem("courses"))
    if (coursesData == null) {
      alert("You currently do not have any courses");
    }
    else {
      let notFound = true;
      for(let i = 0; i < courses.length; i++) {
        if (coursesData[i].department == department 
          && coursesData[i].courseNumber == courseNumber) {
          notFound = false;
          coursesData.splice(i, 1);
          localStorage.setItem("courses", JSON.stringify(coursesData));
          setChanged(!changedPreferences);
          break;
        }
      }
      if (notFound) {
        alert("Course does not exist");
      }
    }
  };

  const addExtracurricular = (event) => {
      event.preventDefault();
      const start = event.target.elements.start.value;
      const end = event.target.elements.end.value;
      console.log(start);
      console.log(end);
  }

  const addStartTime = () => {

  }

  return (
    <div>
      <Form onSubmit={addCourse}>
        {" "}
        <Form.Group controlId="add">
          <Form.Label>Add a class:</Form.Label>
          <Form.Control type="text" placeholder="Enter a class e.g. CSCI201" />
        </Form.Group>
        <Button variant="primary" type="submit">
          Add
        </Button>
      </Form>

      <Form onSubmit={removeCourse}>
        {" "}
        <Form.Group controlId="remove">
          <Form.Label>Remove a class:</Form.Label>
          <Form.Control type="text" placeholder="Enter a class e.g. CSCI201" />
        </Form.Group>
        <Button variant="primary" type="submit">
          Remove
        </Button>
      </Form>

      <Form onSubmit={addExtracurricular}>
        <Form.Group controlId="start">
          <Form.Label>Extracurricular start time:</Form.Label>
          <Form.Control type="time" />
        </Form.Group>
        <Form.Group controlId="end">
          <Form.Label>Extracurricular end time:</Form.Label>
          <Form.Control type="time" />
        </Form.Group>
        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>

      <Form>
        <Form.Group controlId="start">
          <Form.Label>Earliest start time:</Form.Label>
          <Form.Control type="time" />
        </Form.Group>
        <Form.Group controlId="end">
          <Form.Label>Latest end time:</Form.Label>
          <Form.Control type="time" />
        </Form.Group>
        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
      <br/>
      <h4>Current Courses </h4>
      <ul className="list-group">
        {courses.map((course) => (
          <li
            key={course.courseNumber}
            className="list-group-item list-group-item-primary"
          >
            {course.department + course.courseNumber.toString()}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AuthDashboard
