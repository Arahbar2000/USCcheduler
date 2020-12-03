import AuthApp from './AuthApp';
import GuestApp from './GuestApp';
import Nav from './Nav'
import React from 'react'
import { UserProvider } from '../Context/UserProvider'
import { useAuth } from '../Context/AuthProvider'
import Container from 'react-bootstrap/Container'

const App = () => {
  // gets auth status from AuthProvider
  const { auth } = useAuth()
  console.log(auth);

  // if auth is true, an authenticated app is rendered with a user provider to save user data
  // otherwise, a guest app is rendered with no user data

  // the functionality of AuthApp and GuestApp will be different

  // makes sure Navbar always appears on top of page
  return (
    <div>
      <Nav />
      <Container>
        {auth ? <UserProvider><AuthApp/></UserProvider> : <GuestApp/>}
      </Container>
    </div>
  )
}

export default App;
