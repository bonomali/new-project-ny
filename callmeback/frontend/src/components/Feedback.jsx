import React, {useState} from 'react';
import { Container, Icon, Rating } from 'semantic-ui-react'

function Feedback() {
    return (
        <Container text textAlign='center' className='paper'>
            <div>
                <div style={{"font-size":"20px"}}>
                    How was your call back?
                </div>
                <div style={{"font-size":"30px"}}>
                    <Rating icon='star' defaultRating={3} maxRating={4} />
                </div>
            </div>
        </Container>
    )
}

export default Feedback;