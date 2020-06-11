/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
