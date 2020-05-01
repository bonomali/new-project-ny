import React, {useState} from 'react';
import { Container, Form, Rating, Button } from 'semantic-ui-react'
import { useHistory } from 'react-router-dom';

function Feedback() {
    const [details, setDetails] = useState('')
    const defaultRating = 0;
    const [rate, setRate] = useState(defaultRating)
    const history = useHistory();

    const handleSubmit = (evt) => {
        evt.preventDefault()
        // TODO: Send rate and details to the backend.
        // Send to a thank you screen.
        history.push('/thankyou')
    }

    return (
        <Container text textAlign='center' className='paper'>
            <div>
                <div style={{"fontSize":"20px", "paddingBottom":"10px"}}>
                    How was your call back?
                </div>
                <Form onSubmit={handleSubmit}>
                <Rating
                icon='star'
                size='huge'
                defaultRating={defaultRating}
                maxRating={5}
                onRate={(evt, data)=>{setRate(data.rating)}}
                />
                <br/>
                <br/>
                <Form.TextArea
                value={details}
                placeholder="Tell us more (optional)"
                onChange={(evt)=>{setDetails(evt.target.value)}}
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