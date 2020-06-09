import React from 'react';
import { Route, Switch } from 'react-router-dom';
import Reservation from './components/Reservation.jsx';

function Routes() {
  return (
    <div className='container-fluid'>
      <Switch>
        <Route path='/reservations/:id' component={Reservation} />
        <Route exact path='/' component={Reservation} />
      </Switch>
    </div>
  );
}

export default Routes;
