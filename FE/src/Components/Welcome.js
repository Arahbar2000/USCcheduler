import Button from 'react-bootstrap/Button';

const Welcome = (props) => {
    const onClick = () => {
        props.history.push('/dashboard');
    }
    return (
        <Button onClick={onClick}>Get Started</Button>
    );
}

export default Welcome;