import React, { useState } from "react";
import Form from "react-bootstrap/Form";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
const GuestDashboard = () => {
  // displays current chosen courses
  // allows user to add new courses
  // saves course preferences in localStorage so that preferences don't get deleted later on

  const [courses, setCourses] = useState([]);

  const addCourse = (courseData) => {
    // given a course, communicates with the server to check if course is valid
    // if valid, display course on dashboard add to courses, and save to localStorage
  };

  const removeCourse = (courseData) => {
    // given a course, removes course from list of courses
  };

  return (
    <div>
      <Form>
        {" "}
        <Form.Group controlId="class">
          <Form.Label>Add a class:</Form.Label>
          <Form.Control type="text" placeholder="Enter a class e.g. CSCI201" />
        </Form.Group>
        <Button variant="primary" type="submit">
          Add
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
    </div>
  );
};

export default GuestDashboard;
