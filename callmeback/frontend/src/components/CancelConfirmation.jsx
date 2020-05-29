import React from 'react';
import { Container } from 'semantic-ui-react';
import { Link } from 'react-router-dom';

function CancelConfirmation() {
  return (
    <Container
      text
      className='paper'
      data-testid='cancelconfirmation-container'
    >
      <div>
        You have canceled your call with us. We're here if you want to{' '}
        <Link to='/home'>Request a new call.</Link>
      </div>
    </Container>
  );
}

export default CancelConfirmation;
