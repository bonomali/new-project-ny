import React from 'react';
import { Route, Switch } from 'react-router-dom';
import Header from './components/Header.jsx';
import Home from './components/Home.jsx';
import Reservation from './components/Reservation.jsx';
import ThankYou from './components/ThankYou.jsx';
import Cancel from './components/Cancel.jsx';
import CancelConfirmation from './components/CancelConfirmation.jsx';

function Routes() {
  return (
    <div className='container-fluid'>
      <Header />
      <Switch>
        <Route path='/home' component={Home} />
        <Route path='/thankyou' component={ThankYou} />
        <Route path='/cancel' component={Cancel} />
        <Route path='/cancelconfirmation' component={CancelConfirmation} />
        <Route path='/reservations/:id' component={Reservation} />
        <Route path='/reservations-ma/:id'
          component={(props) =>
            <Reservation {...props} useMovingAverage={true} />} />
        <Route exact path='/' component={Home} />
      </Switch>
    </div>
  );
}

export default Routes;
