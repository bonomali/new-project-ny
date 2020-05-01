import React, {useState, useEffect} from 'react';
import {useParams} from 'react-router';
import { Container } from 'semantic-ui-react'
import Feedback from './Feedback.jsx'
import WaitDetails from './WaitDetails.jsx'

function Reservation() {
    const { id } = useParams();
    
    // Used to indicate whether the component has already been rendered, so on re-render, we can mark
    // status as complete.
    // TODO: Remove when we call the backend and allow the status update to occur there.
    let hasBeenDisplayed = false; 

    const fetchReservation = () => {
        // TODO: Call reservations/{id} endpoint to get reservation data
        // (this would not be necessary if we had a global redux state with the
        // reservation data we got upon form submit).

        // The following will all come from the API call.
        const topic = "Employment";
        const status = hasBeenDisplayed ? "Call Completed" : "Waiting";
        const now = new Date();
        const expCallStartMin = new Date(now.getTime() + 10*60000)
        const expCallStartMax = new Date(expCallStartMin.getTime() + 5*60000)
    
        // Calculations for wait time given the min and max exp call time.
        const waitMinMS = expCallStartMin - now;
        const waitMaxMS = expCallStartMax - now;
        const waitMin = Math.round(((waitMinMS % 86400000) % 3600000) / 60000); // minutes
        const waitMax = Math.round(((waitMaxMS % 86400000) % 3600000) / 60000); 

        hasBeenDisplayed = true;
    
        return {topic, status, waitMin, waitMax, expCallStartMin, expCallStartMax}
    }

    const [reservationDetails, setReservationDetails] = useState(() => {
        return fetchReservation();
    })

    useEffect(() => {
        const interval = setInterval(() => {
            const res = fetchReservation();
            setReservationDetails(res);
        }, 1000 * 10); // 10 seconds
        return () => clearInterval(interval);
    }, [])

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