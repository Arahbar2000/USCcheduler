import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Navbar from './Nav'
import Schedule from './Schedule'
import AuthDashboard from './AuthDashboard'
const AuthApp = () => {
    // handles url routing within the authenticated interface
    // more routes can be added later on
    return (
        <Switch>
            <Route exact path='/' component={Schedule} />
            <Route path='/dashboard' component={AuthDashboard} />
        </Switch>
    );
}

export default AuthApp