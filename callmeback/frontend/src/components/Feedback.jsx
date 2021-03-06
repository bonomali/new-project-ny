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
import {Container, Form, Rating, Button} from 'semantic-ui-react';
import {useHistory} from 'react-router-dom';
import axios from 'axios';

/**
 * @param {object} props contains the id of the reservation
 * @return {string} Container allowing the user to provide feedback on their
 * experience
 */
function Feedback(props) {
  const id = props.id;
  const [details, setDetails] = useState('');
  const defaultRating = 0;
  const [rating, setRating] = useState(defaultRating);
  const [submitButtonEnabled, setSubmitButtonEnabled] = useState(false);
  const history = useHistory();

  useEffect(() => {
    if (rating !== defaultRating) {
      // only enable submit button if the rating has been set.
      if (!submitButtonEnabled) setSubmitButtonEnabled(true);
    } else {
      if (submitButtonEnabled) setSubmitButtonEnabled(false);
    }
  }, [rating, submitButtonEnabled]);

  const handleSubmit = (evt) => {
    evt.preventDefault();

    // Send feedback details to the backend.
    const feedback = {
      date: new Date(),
      rating: rating,
      comment: details,
    };
    axios
        .patch('/api/v1/reservations/' + id, {
          feedback: feedback,
        })
        .then(
            (response) => {
              console.log(response);

              // Send to a thank you screen.
              history.push('/thankyou');
            },
            (error) => {
              console.log(error);
              // TODO Display error in the UI.
            },
        );
  };

  return (
    <Container text textAlign='center' className='paper'>
      <div>
        <div style={{fontSize: '20px', paddingBottom: '10px'}}>
          How was your call?
        </div>
        <Form onSubmit={handleSubmit}>
          <Rating
            icon='star'
            size='huge'
            defaultRating={defaultRating}
            maxRating={5}
            onRate={(evt, data) => {
              setRating(data.rating);
            }}
          />
          <br />
          <br />
          <Form.TextArea
            value={details}
            placeholder='Tell us more (optional)'
            onChange={(evt) => {
              setDetails(evt.target.value);
            }}
          />
          <Button
            type='submit'
            color='blue'
            variant='contained'
            disabled={!submitButtonEnabled}
            className='submit'
          >
            Submit feedback
          </Button>
        </Form>
      </div>
    </Container>
  );
}

export default Feedback;
