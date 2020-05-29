import React from 'react';
import { Container, Form, Button } from 'semantic-ui-react';
import Moment from 'react-moment';
import { useLocation } from 'react-router';
import { Link } from 'react-router-dom';
import { useHistory } from 'react-router-dom';
import axios from 'axios';

function Cancel() {
  let location = useLocation();

  const callStartFormatted = (
    <Moment format='h:mm A'>{location.state.expCallStartMin}</Moment>
  );
  const callStartMaxFormatted = (
    <Moment format='h:mm A'>{location.state.expCallStartMax}</Moment>
  );
  const reservationLink = '/reservations/' + location.state.id;
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
        }
      );
  };

  return (
    <Container text className='paper'>
      <div style={{ textAlign: 'center' }}>Cancel your call?</div>
      <br />
      <div style={{ fontSize: 'small' }}>
        You have a call with us scheduled to start between {callStartFormatted}{' '}
        and {callStartMaxFormatted} today.
      </div>
      <br />
      <div style={{ fontSize: 'small' }}>
        If you cancel, we will not call you. You can always schedule a new call
        later.
      </div>
      <br />
      <div style={{ textAlign: 'center' }}>
        <Form onSubmit={handleSubmit}>
          <Button size='small' type='submit'>
            Cancel my call
          </Button>
        </Form>
        <br />
        <Link to={reservationLink} style={{ fontSize: 'small' }}>
          Keep my call
        </Link>
      </div>
    </Container>
  );
}

export default Cancel;
