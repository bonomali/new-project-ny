import React, {useState} from 'react';
import {useParams} from 'react-router';
import { Container, Icon } from 'semantic-ui-react'
import Feedback from './Feedback.jsx'
import WaitDetails from './WaitDetails.jsx'

function Reservation() {
    const { id } = useParams();
    const [reservationDetails] = useState(() => {
        // TODO: Call reservations/{id} endpoint to get reservation data
        // (this would not be necessary if we had a global redux state with the
        // reservation data we got upon form submit)
        // and generate a random expected wait time here.
        const topic = "Employment";
        const status = "Waiting";
        const expCallStartMin = new Date();
        const expCallStartMax = new Date(expCallStartMin.getTime() + 5*60000)
        const waitMS = new Date() - expCallStartMin;
        const waitMin = Math.round(((waitMS % 86400000) % 3600000) / 60000); // minutes
        const waitMax = waitMin + 5;

        return {topic, status, waitMin, waitMax, expCallStartMin, expCallStartMax}
    })

    return (
        <Container text textAlign='center' className='paper'>
            {!!reservationDetails &&
            reservationDetails.status === "Waiting" &&
            <WaitDetails reservationDetails={reservationDetails}/>}
            {!!reservationDetails &&
            reservationDetails.status === "Call Completed" &&
            <Feedback />}
        </Container>
    )
}

export default Reservation;