import { useState } from 'react';
import '../css/Welcome.css'
import logo from '../misc/Scheduler_Logo.png';
import AddCourseForm from './AddCourseForm';
import Schedule from './Schedule';
import Nav from './Nav'
import Container from 'react-bootstrap/Container'
import Button from 'react-bootstrap/Button'

const Home = (props) => {
    const [ search, setSearch ] = useState(true);
    const [ clear, setClear ] = useState(false)

    const showSearch = flag => {
        setSearch(flag);
    }

    const clearSchedules = () => {
        localStorage.removeItem("schedules");
        localStorage.removeItem("events");
        setClear(!clear)
    }

    return (
        <div>
            <Nav search={search} clearSchedules={clearSchedules} clear={clear} show={showSearch} />
            {/* <Button onClick={() => setSearch(!search)}>Change</Button> */}
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