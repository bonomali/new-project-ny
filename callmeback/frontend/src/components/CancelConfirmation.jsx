import React from 'react';
import { Container } from 'semantic-ui-react'

function CancelConfirmation() {
    return (
        <Container text className='paper'>
            <div>You have cancelled your call with us. We're here if you want to <a href="/home">Request a new call.</a></div>
        </Container>
    )
}

export default CancelConfirmation;