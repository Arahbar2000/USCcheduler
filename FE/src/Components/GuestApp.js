import React from 'react'
import {BrowserRouter, Switch, Route} from 'react-router-dom';
import Schedule from './Schedule'
import GuestDashboard from './GuestDashboard'

const GuestApp = () => {
    // handles url routing within the guest interface
    // more routes can be added later on
    return (
        <Switch>
            <Route exact path='/' component={Schedule} />
            <Route path='/dashboard' component={GuestDashboard} />
        </Switch>
    );
}

export default GuestApp