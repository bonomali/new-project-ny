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

import React, {useState, useEffect} from 'react';
import {Container, Button, Form, Input, Header} from 'semantic-ui-react';
import {useHistory} from 'react-router-dom';
import axios from 'axios';
import moment from 'moment';

/**
 * @return {string} Container with a form allowing a user to sign up for a
 * reservation
 */
function Home() {
  const [name, setName] = useState('');
  const [number, setNumber] = useState('');
  const [query, setQuery] = useState('');
  const [requestButtonEnabled, setRequestButtonEnabled] = useState(false);
  const history = useHistory();
  const validPhoneNumber =
      /^(\((\d{3})\)|(\d{3}))([\s-.]?)(\d{3})([\s-.]?)(\d{4})$/;

  useEffect(() => {
    if (
      validPhoneNumber.test(number) &&
      name.trim() !== '' &&
      query.trim() !== ''
    ) {
      // only set requestButtonEnabled to true if number, name, and topic are
      // provided AND requestButtonEnabled is not already true (don't re-render
      // if we don't need to)
      if (!requestButtonEnabled) setRequestButtonEnabled(true);
    } else {
      if (requestButtonEnabled) setRequestButtonEnabled(false);
    }
  }, [validPhoneNumber, number, requestButtonEnabled, name, query]);

  const handleSubmit = (evt) => {
    evt.preventDefault();

    // Store reservation in the backend.
    const reservation = {
      preferredName: name,
      contactPhone: number,
      query: query,
      requestDate: moment(),
    };
    axios.post('/api/v1/reservations', reservation).then(
        (response) => {
          console.log(response);

          // Get ID of reservation from response, and direct the user to
          // /reservations/{id} where they will see information about their
          // call.
          const hrefArray = response.data._links.self.href.split('/');
          const reservationId = hrefArray[hrefArray.length - 1];
          history.push('/reservations/' + reservationId, {
            reservation: response.data,
          });
        },
        (error) => {
          console.log(error);
        // TODO Display error in the UI.
        },
    );
  };

  return (
    <Container
      text
      textAlign='center'
      className='paper'
      data-testid='home-container'
    >
      <div>
        <Header as='h1'>Request a call with us</Header>
        <div>
          Tell us what you need and someone from our New York State offices will
          call you.
        </div>
        <br />
        <Form onSubmit={handleSubmit}>
          <Form.Field
            required
            control={Input}
            value={name}
            placeholder='Name'
            onChange={(evt) => {
              setName(evt.target.value);
            }}
          />
          <Form.Field
            required
            control={Input}
            value={number}
            type='tel'
            placeholder='Phone number'
            onChange={(evt) => {
              setNumber(evt.target.value);
            }}
          />
          <Form.TextArea
            required
            placeholder='Tell us how we can help'
            value={query}
            onChange={(evt) => {
              setQuery(evt.target.value);
            }}
          />
          <br />
          <Button
            type='submit'
            variant='contained'
            color='blue'
            disabled={!requestButtonEnabled}
            className='submit'
          >
            Send request
          </Button>
        </Form>
      </div>
    </Container>
  );
}

export default Home;
