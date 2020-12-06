import { useState } from 'react';
import '../css/Welcome.css'
import logo from '../misc/Scheduler_Logo.png';
import AddCourseForm from './AddCourseForm';
import Schedule from './Schedule';
import Nav from './Nav'
import Container from 'react-bootstrap/Container'

const Home = (props) => {
    const [ search, setSearch ] = useState(true);

    const showSearch = flag => {
        setSearch(flag);
    }

    return (
        <div>
            <Nav show={showSearch} />
            <Container>
            {
                search ? 
                    <div>
                        <div style={{width: '30%', margin: '20px auto', maxWidth: '30%', textAlign: 'center'}}>
                            <img id='mainLogo' src={logo} alt='schedule logo'/>
                        </div>
                        <AddCourseForm show={showSearch} {...props} /> 
                    </div> : 
                    <Schedule show={showSearch} />
            }
            </Container>
        </div>
    );
}

export default Home;