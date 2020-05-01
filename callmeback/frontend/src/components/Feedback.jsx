import React, {useState} from 'react';
import { Container, Form, Rating, Button } from 'semantic-ui-react'

function Feedback() {
    const [details, setDetails] = useState('')
    const defaultRating = 3;
    const [rate, setRate] = useState(defaultRating)
    return (
        <Container text textAlign='center' className='paper'>
            <div>
                <div style={{"fontSize":"20px", "paddingBottom":"10px"}}>
                    How was your call back?
                </div>
                <Form onSubmit={(evt)=>{setDetails(evt.target.data)}}>
                <Rating
                icon='star'
                size='huge'
                defaultRating={defaultRating}
                maxRating={4}
                value={rate}
                onRate={(evt)=>{setRate(evt.target.data)}}
                />
                <Form.TextArea
                value={details}
                placeholder="Tell us more (optional)"
                />
                <Button
                type="submit"
                color="blue"
                variant="contained"
                className="submit"
                >
                Submit feedback
                </Button>
                </Form>
            </div>
        </Container>
    )
}

export default Feedback;