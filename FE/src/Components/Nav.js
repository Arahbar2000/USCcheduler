import React, { useState } from "react";
import { Navbar, Nav as Nv } from "react-bootstrap";
import { LinkContainer } from 'react-router-bootstrap';
import logo from '../misc/Scheduler.png';
import '../css/Nav.css'
import NavDropdown from 'react-bootstrap/NavDropdown'

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
      {!props.search ? 
      <LinkContainer to='/'>
        <Navbar.Brand onClick={() => props.show(true)}><img id='navLogo' src={logo} /></Navbar.Brand>
      </LinkContainer> : null
      }
      {/* <Nv onSelect={handleSelect} activeKey={activeKey}>
        <Navbar.Brand href="/">Home</Navbar.Brand>
        <LinkContainer to='/schedule'>
          <Nv.Link eventKey={1}>Schedule</Nv.Link>
        </LinkContainer>
        <LinkContainer to='/dashboard'>
          <Nv.Link eventKey={2}>Dashboard</Nv.Link>
        </LinkContainer>
      </Nv> */}

      <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
      <Navbar.Collapse id="responsive-navbar-nav" className="justify-content-end">
        <Nv className="ml-auto">
          {!props.search ? 
          <Nv.Link onClick={() => props.show(true)}>Modify Courses</Nv.Link> :
          <Nv.Link onClick={() => props.show(false)}>Previous Selections</Nv.Link>}
        </Nv>
      </Navbar.Collapse>
    </Navbar>
  );
};
// return (
//   <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
//   <Navbar.Brand href="#home">React-Bootstrap</Navbar.Brand>
//   <Navbar.Toggle aria-controls="responsive-navbar-nav" />
//   <Navbar.Collapse id="responsive-navbar-nav">
//     <Nav className="mr-auto">
//       <Nav.Link href="#features">Features</Nav.Link>
//       <Nav.Link href="#pricing">Pricing</Nav.Link>
//       <NavDropdown title="Dropdown" id="collasible-nav-dropdown">
//         <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
//         <NavDropdown.Item href="#action/3.2">Another action</NavDropdown.Item>
//         <NavDropdown.Item href="#action/3.3">Something</NavDropdown.Item>
//         <NavDropdown.Divider />
//         <NavDropdown.Item href="#action/3.4">Separated link</NavDropdown.Item>
//       </NavDropdown>
//     </Nav>
//     <Nav>
//       <Nav.Link href="#deets">More deets</Nav.Link>
//       <Nav.Link eventKey={2} href="#memes">
//         Dank memes
//       </Nav.Link>
//     </Nav>
//   </Navbar.Collapse>
// </Navbar>
// )

export default Nav;
