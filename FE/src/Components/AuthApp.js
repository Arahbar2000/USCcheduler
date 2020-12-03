import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Schedule from './Schedule'
import AuthDashboard from './AuthDashboard'
const AuthApp = () => {
    // handles url routing within the authenticated interface
    // more routes can be added later on
    console.log('authenticated');
    return (
        <Switch>
            <Route exact path='/' component={Schedule} />
            <Route path='/home/dashboard' component={AuthDashboard} />
        </Switch>
    );
}

export default AuthApp