import React from 'react'
import { Switch, Route} from 'react-router-dom';
import Schedule from './Schedule'
import GuestDashboard from './GuestDashboard'
import Welcome from './Welcome'

const GuestApp = () => {
    // handles url routing within the guest interface
    // more routes can be added later on
    return (
        <Switch>
            <Route exact path='/' component={Welcome}/>
            <Route path='/schedule' component={Schedule} />
            <Route path='/dashboard' component={GuestDashboard} />
        </Switch>
    );
}

export default GuestApp