import React, { useState, useEffect } from "react";
import Form from "react-bootstrap/Form";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import ListGroup from "react-bootstrap/ListGroup"
import { API_URL } from '../env'
const GuestDashboard = () => {
  // displays current chosen courses
  // allows user to add new courses
  // saves course preferences in localStorage so that preferences don't get deleted later on

  const [courses, setCourses] = useState([]);
  const [extracurriculars, setExtracurriculars] = useState([]);
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [changedPreferences, setChanged] = useState(false)

  useEffect(() => {
    let courseData = JSON.parse(localStorage.getItem("courses"));
    let extracurricularData = JSON.parse(localStorage.getItem("extracurriculars"));
    let startTimeData = localStorage.getItem("startTime");
    let endTimeData = localStorage.getItem("endTime");
    if (courseData != null) {
      setCourses(courseData)
    }
    else {
      setCourses([]);
    }
    if (extracurricularData != null) {
      setExtracurriculars(extracurricularData);
    }
    else {
      setExtracurriculars([]);
    }
    if (startTimeData != null) {
      setStartTime(startTimeData);
    }
    else {
      setStartTime(null);
    }
    if (endTimeData != null) {
      setEndTime(endTimeData);
    }
    else {
      setEndTime(null);
    }
  }, [changedPreferences])

  const addCourse = (event) => {
    event.preventDefault();
    // given a course, communicates with the server to check if course is valid
    // if valid, display course on dashboard add to courses, and save to localStorage
    const courseName = event.target.elements.add.value.toUpperCase().split(" ");
    const department = courseName[0];
    const courseNumber = parseInt(courseName[1]);
    verifyCourse(department, courseNumber, true)
    .then(() => {
      // add course to storage
      let added = false;
      let coursesData = JSON.parse(localStorage.getItem("courses"));
      if (coursesData == null) {
        coursesData = [{department, courseNumber}]
      }
      else {
        for(let i = 0; i < coursesData.length; i++) {
          if(coursesData.department == department && coursesData.courseNumber == courseNumber) {
            added = true;
            alert("Course has already been added!");
            break;
          }
        }
        if (!added) {
          coursesData.push({department, courseNumber})
        }
      }
      if (!added) {
        alert("Valid Course!");
        localStorage.setItem("courses", JSON.stringify(coursesData))
        setChanged(!changedPreferences)
      }
    })
    .catch(() => {
      alert("Invalid course.");
    });
  };

  const verifyCourse = (department, courseNumber, add) => {
    const url = new URL("http://localhost:8080/CS201/api/courses")
    if (add) {
      url.search = new URLSearchParams({department, courseNumber})
    }
    else {
      url.search = new URLSearchParams({department, courseNumber, clear: 0})
    }
    return new Promise((resolve, reject) => {
      fetch(url, {
        method: add ? "GET": "DELETE"
      }).then(response => response.json())
      .then((response) => {
        if (response.status == true) {
          return resolve();
        }
        else {
          return reject();
        }
      })
      .catch(error => {
        // testing purposes, later change to false
          console.log(error)
          return reject();
      });
    })
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

  const clearPreferences = event => {
    localStorage.removeItem("extracurriculars");
    localStorage.removeItem("startTime");
    localStorage.removeItem("endTime");
    setChanged(!changedPreferences);
  }

  const generateEvents = () => {
    const url = new URL(API_URL + 'guest')
    const extracurriculars = JSON.parse(localStorage.getItem("extracurriculars"))
    let extracurriculum = "";
    if (extracurriculars != null) {
      extracurriculars.forEach(extracurricular => {
        const strFormat = JSON.stringify(extracurricular) + ",";
        extracurriculum += strFormat;
      })
      extracurriculum = extracurriculum.slice(0, -1)
    }
    else {
      extracurriculum = null;
    }
    const startTime = localStorage.getItem("startTime");
    const endTime = localStorage.getItem("endTime");
  }

  const addExtracurricular = (event) => {
      event.preventDefault();
      const start = event.target.elements.start.value;
      const end = event.target.elements.end.value;
      const extracurricular = [start + " " + end];
      console.log(start);
      console.log(end);
      let extracurricularData = JSON.parse(localStorage.getItem("extracurriculars"));
      if (extracurricularData == null) {
        extracurricularData = [ extracurricular ]
      }
      else {
        extracurricularData.push(extracurricular);
      }
      console.log(extracurricularData);
      localStorage.setItem("extracurriculars", JSON.stringify(extracurricularData));
      setChanged(!changedPreferences);
  }

  const addTimes = event => {
    event.preventDefault();
    const start = event.target.elements.start.value;
    const end = event.target.elements.end.value;
    if (start != "") {
      localStorage.setItem("startTime", start);
    }
    if( end != "") {
      localStorage.setItem("endTime", end);
    }
    setChanged(!changedPreferences);
  }

  const clearCourses = () => {
    localStorage.removeItem("courses");
    setChanged(!changedPreferences);
  }

  return (
    <div>
      <Button variant="primary" onClick={clearPreferences}>Clear Preferences</Button>
      <Button variant="primary" onClick={clearCourses}>Clear courses</Button>
      <br/>
      <br/>
      <Container>
        <Row>
          <Col>
          <Form onSubmit={addCourse}>
          {" "}
          <Form.Group controlId="add">
          <Form.Label>Add a class:</Form.Label>
          <Form.Control type="text" placeholder="Enter a class e.g. CSCI 201" />
          <Form.Text>Make sure there is a space between department and course number</Form.Text>
        </Form.Group>
        <Button variant="primary" type="submit">
          Add
        </Button>
      </Form>
          </Col>
          <Col>
          <Form onSubmit={removeCourse}>
        {" "}
        <Form.Group controlId="remove">
          <Form.Label>Remove a class:</Form.Label>
          <Form.Control type="text" placeholder="Enter a class e.g. CSCI 201" />
          <Form.Text>Make sure there is a space between department and course number</Form.Text>
        </Form.Group>
        <Button variant="primary" type="submit">
          Remove
        </Button>
      </Form>
          </Col>
        </Row>
        <br/>
        </Container>
          <Form onSubmit={addExtracurricular}>
            <Container fluid>
              <Row>
                <Col>
                <Form.Group controlId="start">
                <Form.Label>Extracurricular start time:</Form.Label>
                <Form.Control type="time" placeholder="00:00"/>
                </Form.Group>
                </Col>
                <Col>
                <Form.Group controlId="end">
                <Form.Label>Extracurricular end time:</Form.Label>
                <Form.Control type="time" placeholder="00:00"/>
                </Form.Group>
                </Col>
              </Row>
              <Row>
                <Col>
                <Button variant="primary" type="submit">
                  Submit
                </Button>
                </Col>
              </Row>
            </Container>
      </Form>
      <br/>
      <Form onSubmit={addTimes}>
        <Container>
          <Row>
            <Col>
            <Form.Group controlId="start">
            <Form.Label>Earliest start time:</Form.Label>
            <Form.Control type="time" placeholder="00:00"/>
          </Form.Group>
            </Col>
            <Col>
            <Form.Group controlId="end">
            <Form.Label>Latest end time:</Form.Label>
            <Form.Control type="time" placeholder="00:00"/>
          </Form.Group>
            </Col>
          </Row>
          <Row>
            <Col>
            <Button variant="primary" type="submit">
            Submit
          </Button>
            </Col>
          </Row>
        </Container>
      </Form>
      <br/>
      <Container fluid>
        <Button style={{background: 'red'}} variant="primary" onClick={generateEvents} block>
            GENERATE EVENTS
        </Button>
      </Container>
      <br/>

      <Container fluid>
        <Row>
          <Col>
          <h4>Current Courses </h4>
          <div>
          <ListGroup>
              {courses.map((course) => (
                  <ListGroup.Item key={course.department + course.courseNumber.toString()}>{course.department + course.courseNumber.toString()}</ListGroup.Item>
              ))}
            </ListGroup>
          </div>
          </Col>
          <Col>
          <div>
            <h4>Extracurriculars </h4>
          <ListGroup>
              {extracurriculars.map(extracurricular => {
                const times = extracurricular[0].split(' ');
              return <ListGroup.Item>Start: {times[0]} <br/> End: {times[1]}</ListGroup.Item>
              })}
            </ListGroup>
          </div>
          </Col>
          <Col>
              <ListGroup>
                {startTime ?  <ListGroup.Item>Start Time: {startTime}</ListGroup.Item> : null}
                {endTime ? <ListGroup.Item>End Time: {endTime}</ListGroup.Item> : null}
              </ListGroup>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default GuestDashboard;
