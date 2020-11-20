import React, { useState } from "react";
import Form from "react-bootstrap/Form";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
const GuestDashboard = () => {
  // displays current chosen courses
  // allows user to add new courses
  // saves course preferences in localStorage so that preferences don't get deleted later on

  const [courses, setCourses] = useState([]);
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  const listItems = ["CSCI201", "EE109"];

  const addCourse = (courseData) => {
    // given a course, communicates with the server to check if course is valid
    // if valid, display course on dashboard add to courses, and save to localStorage

    if (verifyCourse(courseData) == false) {
      // course is invalid
      alert("Invalid course.");
    } else {
      // add course to storage
      listItems.push(courseData);
    }
  };

  const verifyCourse = (courseData) => true;

  const removeCourse = (courseData) => {
    // given a course, removes course from list of courses
    for (var i = 0; i < listItems.length; i++) {
      if (listItems[i] == courseData) {
        listItems.splice(i, 1);
      }
    }
  };

  return (
    <div>
      <Form onSubmit={addCourse()}>
        {" "}
        <Form.Group controlId="add">
          <Form.Label>Add a class:</Form.Label>
          <Form.Control type="text" placeholder="Enter a class e.g. CSCI201" />
        </Form.Group>
        <Button variant="primary" type="submit">
          Add
        </Button>
      </Form>

      <Form onSubmit={removeCourse()}>
        {" "}
        <Form.Group controlId="remove">
          <Form.Label>Remove a class:</Form.Label>
          <Form.Control type="text" placeholder="Enter a class e.g. CSCI201" />
        </Form.Group>
        <Button variant="primary" type="submit">
          Remove
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

      <h4>Current Courses</h4>
      <ul className="list-group">
        {listItems.map((listitem) => (
          <li
            key={listitem}
            className="list-group-item list-group-item-primary"
          >
            {listitem}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default GuestDashboard;
