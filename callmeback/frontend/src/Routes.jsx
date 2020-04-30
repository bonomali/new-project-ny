import React from 'react'
import { Route, Switch, BrowserRouter as Router } from 'react-router-dom'
import Home from './components/Home.jsx'
import Reservation from './components/Reservation.jsx'

function Routes() {
    return (
        <Router>
            <div className="container-fluid">
                <Switch>
                    <Route path='/home' component={Home} />
                    <Route path='/reservations/:id' component={Reservation} />
                    <Route exact path='/' component={Home} />
                </Switch>
            </div>
        </Router>
    )
}

export default Routes;
