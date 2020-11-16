import React, {useState, useRef} from 'react';
import { useAuth } from '../Context/AuthProvider';
import { Link } from 'react-router-dom';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Form from 'react-bootstrap/Form';
import FormControl from 'react-bootstrap/FormControl';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';

const SingInButton = (props) => {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const emailInput = useRef(null);
    const passwordInput = useRef(null);

    const submitForm = (event) => {
        let formdata = new URLSearchParams();
        formdata.append("email", emailInput.current.value);
        formdata.append("password", passwordInput.current.value);

        let promise = props.signin(formdata);
        promise.then(msg => {
            // not login correctly
            if (msg !== null){
                alert(msg);
            }
            else{
                alert("welcome!");
            }
        });
        handleClose();
        event.preventDefault();
    }

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
                            <Form.Control type="email" placeholder="Enter email"
                                          ref={emailInput}
                            />
                            <Form.Text className="text-muted">
                               Login in with usc email
                            </Form.Text>
                        </Form.Group>

                        <Form.Group controlId="formBasicPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" placeholder="Password"
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
    )
}

const Nav = (props) => {
    // can call these functions from auth provider to handle changing authentication
    const {auth, signUp, signIn, signOut} = useAuth();

    // renders at the top of the screen
    // if auth is true, navbar shows signOut button
    // otherwise, navbar shows signUp and signIn buttons
    return <Navbar>
        <Navbar.Brand href="#home">Navbar with text</Navbar.Brand>
        <Navbar.Toggle />
        <Navbar.Collapse className="justify-content-end">
                {auth ? <Button onClick={signOut}>SignOut</Button>
                      : <SingInButton signin={signIn}>SignIn</SingInButton>
                }
        </Navbar.Collapse>
    </Navbar>
}

export default Nav