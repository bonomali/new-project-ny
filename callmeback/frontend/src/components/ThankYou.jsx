import React from 'react';
import { Container } from 'semantic-ui-react'

function ThankYou() {
    return (
        <Container text style={{"paddingTop":"2em"}}>
            <div>Thank you!</div>
            <br/>
            <div><a href="/">Go to NYS call homepage</a></div>
        </Container>
    )
}

export default ThankYou;