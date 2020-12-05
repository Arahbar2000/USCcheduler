import Button from 'react-bootstrap/Button';
import '../css/Welcome.css'
import logo from '../misc/img.png'

const Welcome = (props) => {
    const onClick = () => {
        props.history.push('/dashboard');
    }
    return (
        <div>
            <img src={logo} alt='usc logo'/>
            <Button block id='welcomeBtn' onClick={onClick}>Get Started</Button>
        </div>
    );
}

export default Welcome;