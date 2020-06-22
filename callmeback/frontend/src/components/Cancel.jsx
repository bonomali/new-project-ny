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
import {Container, Form, Button} from 'semantic-ui-react';
import Moment from 'react-moment';
import {useLocation} from 'react-router';
import {Link} from 'react-router-dom';
import {useHistory} from 'react-router-dom';
import axios from 'axios';

/**
 * @return {string} A container allowing a user to cancel their call
 */
function Cancel() {
  const location = useLocation();
  const history = useHistory();

  const handleSubmit = (evt) => {
    evt.preventDefault();

    // Send feedback details to the backend.
    const resolution = {
      date: new Date(),
      type: 'CANCELED',
    };
    axios
        .patch('/api/v1/reservations/' + location.state.id, {
          resolution: resolution,
        })
        .then(
            (response) => {
              console.log(response);

              // Send to the confirmation screen.
              history.push('/cancelconfirmation');
            },
            (error) => {
              console.log(error);
              // TODO Display error in the UI.
            },
        );
  };

  // If the state isn't populated, display empty page
  if (!location || !location.state) {
    return (
      <Container
        text
        className='paper'
        data-testid='cancel-container'
      ></Container>
    );
  }

  const callStartFormatted = (
    <Moment format='h:mm A'>{location.state.callStartMin}</Moment>
  );
  const callStartMaxFormatted = (
    <Moment format='h:mm A'>{location.state.callStartMax}</Moment>
  );
  const reservationLink =
      location.state.useMovingAverage ?
      '/reservations-ma/' + location.state.id :
      '/reservations/' + location.state.id;

  return (
    <Container text className='paper' data-testid='cancel-container'>
      <div style={{textAlign: 'center'}}>Cancel your call?</div>
      <br />
      <div style={{fontSize: 'small'}}>
        You have a call with us scheduled to start between {callStartFormatted}
        {' '}
        and {callStartMaxFormatted} today.
      </div>
      <br />
      <div style={{fontSize: 'small'}}>
        If you cancel, we will not call you. You can always schedule a new call
        later.
      </div>
      <br />
      <div style={{textAlign: 'center'}}>
        <Form onSubmit={handleSubmit}>
          <Button size='small' type='submit'>
            Cancel my call
          </Button>
        </Form>
        <br />
        <Link to={reservationLink} style={{fontSize: 'small'}}>
          Keep my call
        </Link>
      </div>
    </Container>
  );
}

export default Cancel;
