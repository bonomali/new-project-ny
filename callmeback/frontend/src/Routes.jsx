import React from 'react'
import {Router} from 'react-router'
import { Route, Switch } from 'react-router-dom'
import Home from './components/Home.jsx'
import history from './history'

function Routes() {
    return (
        <Router history={history}>
            <div className="container-fluid">
                <Switch>
                    <Route path='/home' component={Home} />
                    <Route exact path='/' component={Home} />
                </Switch>
            </div>
        </Router>
    )
}

export default Routes;
