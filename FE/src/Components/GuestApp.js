import React from 'react'
import { Switch, Route} from 'react-router-dom';
import Schedule from './Schedule'
import GuestDashboard from './GuestDashboard'
import Home from './Home'

const GuestApp = () => {
    // handles url routing within the guest interface
    // more routes can be added later on
    return (
        <Switch>
            <Route exact path='/' component={Home}/>
        </Switch>
    );
}

export default GuestApp