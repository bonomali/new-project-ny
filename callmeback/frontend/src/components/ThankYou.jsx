import React from 'react';
import { Container } from 'semantic-ui-react'
import { Link } from 'react-router-dom';

function ThankYou() {
    return (
        <Container text style={{"paddingTop":"2em"}}>
            <div>Thank you!</div>
            <br/>
            <div><Link to="/home">Go to the New York State call homepage</Link></div>
        </Container>
    )
}

export default ThankYou;