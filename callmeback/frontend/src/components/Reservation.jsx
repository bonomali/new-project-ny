import React, {useState} from 'react';
import {useParams} from 'react-router';
import { Container } from 'semantic-ui-react'
import Feedback from './Feedback.jsx'
import WaitDetails from './WaitDetails.jsx'

function Reservation() {
    const { id } = useParams();
    const [reservationDetails] = useState(() => {
        // TODO: Call reservations/{id} endpoint to get reservation data
        // (this would not be necessary if we had a global redux state with the
        // reservation data we got upon form submit).

        // The following will all come from the API call.
        const topic = "Employment";
        const status = "Waiting";
        const expCallStartMin = new Date();
        const expCallStartMax = new Date(expCallStartMin.getTime() + 5*60000)
    
        // Calculations for wait time given the min and max exp call time.
        const waitMinMS = new Date() - expCallStartMin;
        const waitMaxMS = new Date() - expCallStartMax;
        const waitMin = Math.round(((waitMinMS % 86400000) % 3600000) / 60000); // minutes
        const waitMax = Math.round(((waitMaxMS % 86400000) % 3600000) / 60000); 

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