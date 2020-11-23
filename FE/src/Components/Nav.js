import React, { useState, useRef } from "react";
import { useAuth } from "../Context/AuthProvider";
import { Link, Route } from "react-router-dom";
import {NavLink } from 'react-router-dom';
import { Navbar, Nav as Nv } from "react-bootstrap";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Swal from 'sweetalert2';

const SignInButton = (props) => {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const emailInput = useRef(null);
  const passwordInput = useRef(null);

  const submitForm = (event) => {
    event.preventDefault();
    const email = emailInput.current.value;
    const password = passwordInput.current.value;

    let promise = props.signIn({ email, password });
    promise.then((msg) => {
      // not login correctly
      if (msg !== null) {
        Swal.fire({
          icon: 'error',
          text: msg,
        });
      } else {
          Swal.fire("Welcome!");
      }
    });
    handleClose();
  };

  return (
    <>
      <Button variant="primary" onClick={handleShow}>
        Sign In
      </Button>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Please Sign in</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={submitForm}>
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                type="email"
                placeholder="Enter email"
                ref={emailInput}
              />
              <Form.Text className="text-muted">
                Login in with usc email
              </Form.Text>
            </Form.Group>

            <Form.Group controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Password"
                ref={passwordInput}
              />
            </Form.Group>
            <Form.Group controlId="formBasicCheckbox">
              <Form.Check type="checkbox" label="Remember the password" />
            </Form.Group>
            <Button variant="primary" type="submit">
              Let me in!
            </Button>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

const SignUpButton = (props) => {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const fnameInput = useRef(null);
  const lnameInput = useRef(null);
  const emailInput = useRef(null);
  const passwordInput = useRef(null);

  const submitForm = (event) => {
    event.preventDefault();
    const fname = fnameInput.current.value;
    const lname = lnameInput.current.value;
    const email = emailInput.current.value;
    const password = passwordInput.current.value;
    props.singUp({ fname, lname, email, password });
    handleClose();
  };

  return (
    <>
      <Button variant="outline-info" onClick={handleShow}>
        Sign Up
      </Button>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Sign Up Form</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={submitForm}>
            <Form.Group controlId="fname">
              <Form.Label>First Name</Form.Label>
              <Form.Control type="text" placeholder="Tom" name="fname" ref={fnameInput} />
            </Form.Group>
            <Form.Group controlId="lname">
              <Form.Label>Last Name</Form.Label>
              <Form.Control type="text" placeholder="Trojan" name="lname" ref={lnameInput} />
            </Form.Group>

            <Form.Group controlId="Email">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                type="email"
                placeholder="Enter email"
                name="email"
                ref={emailInput}
              />
              <Form.Text className="text-muted">
                Login in with usc email
              </Form.Text>
            </Form.Group>

            <Form.Group controlId="Password">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Password"
                name="password"
                ref={passwordInput}
              />
            </Form.Group>

            <Button variant="primary" type="submit">
              Sign me in!
            </Button>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

const Nav = (props) => {
  // can call these functions from auth provider to handle changing authentication
  const { auth, signUp, signIn, signOut } = useAuth();

  // renders at the top of the screen
  // if auth is true, navbar shows signOut button
  // otherwise, navbar shows signUp and signIn buttons
  return (
    <Navbar>
      <Nv activeKey={window.location.pathname}>
        {/*<Navbar.Brand href="/">Home</Navbar.Brand>*/}
        <Nv.Link href="/">Home</Nv.Link>
        <Nv.Link href="/dashboard">Dashboard</Nv.Link>
      </Nv>
      {/*<NavLink to="/dashboard" activeStyle={{ fontsize: '20', color: 'red' }}>Dashboard</NavLink>*/}

      <Navbar.Toggle />
      <Navbar.Collapse className="justify-content-end">
        
        {auth ? (
          <Button onClick={signOut}>Sign Out</Button>
        ) : (
          <>
            <SignUpButton singUp={signUp} />
            &nbsp;&nbsp;&nbsp;
            <SignInButton signIn={signIn} />
          </>
        )}
      </Navbar.Collapse>
    </Navbar>
  );
};

export default Nav;
