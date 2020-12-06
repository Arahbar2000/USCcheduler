import React, { useState } from "react";
import { Navbar, Nav as Nv } from "react-bootstrap";
import { LinkContainer } from 'react-router-bootstrap';
import logo from '../misc/img.png';
import '../css/Nav.css'

const Nav = (props) => {
  // can call these functions from auth provider to handle changing authentication
  // const { auth, signUp, signIn, signOut } = useAuth();

  // renders at the top of the screen
  // if auth is true, navbar shows signOut button
  // otherwise, navbar shows signUp and signIn buttons
  const [ activeKey, setKey ] = useState(0) 

  const handleSelect = key => {
    setKey(key);
  }

  return (
    <Navbar>
      <LinkContainer to='/'>
        <Navbar.Brand onClick={() => props.show(true)}><img id='navLogo' src={logo} /></Navbar.Brand>
      </LinkContainer>
      {/* <Nv onSelect={handleSelect} activeKey={activeKey}>
        <Navbar.Brand href="/">Home</Navbar.Brand>
        <LinkContainer to='/schedule'>
          <Nv.Link eventKey={1}>Schedule</Nv.Link>
        </LinkContainer>
        <LinkContainer to='/dashboard'>
          <Nv.Link eventKey={2}>Dashboard</Nv.Link>
        </LinkContainer>
      </Nv> */}

      <Navbar.Toggle />
      <Navbar.Collapse className="justify-content-end">
      </Navbar.Collapse>
    </Navbar>
  );
};

export default Nav;
