import React from 'react';
import { Container, Button } from 'semantic-ui-react'
import Moment from 'react-moment';
import { useLocation } from "react-router";
import { Link } from 'react-router-dom';

function Cancel() {
    let location = useLocation();

    const callStartFormatted = <Moment format="h:mm A">{location.state.expCallStartMin}</Moment>
    const callStartMaxFormatted = <Moment format="h:mm A">{location.state.expCallStartMax}</Moment>
    const reservationLink = "/reservations/" + location.state.id

    return (
        <Container text className='paper'>
            <div style={{"textAlign":"center"}}>Cancel your call?</div>
            <br/>
            <div style={{"fontSize":"small"}}>You have a call with us scheduled to start between {callStartFormatted} and {callStartMaxFormatted} today.</div>
            <br/>
            <div style={{"fontSize":"small"}}>If you cancel, we will not call you. You can always schedule a new call later.</div>
            <br/>
            <div style={{"textAlign":"center"}}>
            <Button size="small">
                {/*TODO: Send cancelation to the backend.*/}
                <Link to="/cancelconfirmation">Cancel my call</Link>
            </Button>
            <br/>
            <br/>
            <Link to={reservationLink} style={{"fontSize":"small"}}>Keep my call</Link>
            </div>
        </Container>
    )
}

export default Cancel;