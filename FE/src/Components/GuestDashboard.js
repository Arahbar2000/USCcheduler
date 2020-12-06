import React, { useState, useEffect } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import ListGroup from "react-bootstrap/ListGroup";
import { API_URL } from '../env';
import { generateSchedules } from '../Helpers/getSchedules';
import CourseItem from './CourseItem';
import AddCourseForm from './AddCourseForm';
const GuestDashboard = (props) => {
  // displays current chosen courses
  // allows user to add new courses
  // saves course preferences in localStorage so that preferences don't get deleted later on

  return <AddCourseForm {...props} />
};

export default GuestDashboard;
