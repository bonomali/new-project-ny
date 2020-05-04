import React, {useState, useEffect, useCallback} from 'react';
import {useParams} from 'react-router';
import { Container } from 'semantic-ui-react'
import Feedback from './Feedback.jsx'
import WaitDetails from './WaitDetails.jsx'
import axios from 'axios';

function Reservation() {
    const { id } = useParams();

    const [reservationDetails, setReservationDetails] = useState({});

    const fetchReservation = useCallback(async () => {
        try {
            const response = await axios.get("/api/v1/reservations/" + id); //concatenate id variable
            console.log(response.data)
    
            const now = new Date();
            const expCallStartMin = new Date(response.data.window.min)
            const expCallStartMax = new Date(response.data.window.max)
    
            // Calculations for wait time given the min and max exp call time.
            const waitMinMS = expCallStartMin - now;
            const waitMaxMS = expCallStartMax - now;
            const waitMin = Math.round(((waitMinMS % 86400000) % 3600000) / 60000); // minutes
            const waitMax = Math.round(((waitMaxMS % 86400000) % 3600000) / 60000); 
    
            setReservationDetails({
                id: id,
                topic: "Employment", // Not in response yet
                resolved: response.data.resolution != null,
                waitMin, waitMax, expCallStartMin, expCallStartMax,
            });
        }
        catch (error) {
            console.log(error); // Add other error handling.
        }
    }, [id])

    useEffect(() => {
        fetchReservation(); // Run this once upfront to get the data.
    
        // Set interval to refetch data every ten seconds.
        const interval = setInterval(() => {
            fetchReservation();
        }, 1000 * 10);
        return () => clearInterval(interval);
    }, [fetchReservation])

    return (
        <Container text textAlign='center' className='paper'>
            {reservationDetails.id !== "" &&
            !reservationDetails.resolved &&
            <WaitDetails reservationDetails={reservationDetails}/>}
            {reservationDetails.id !== "" &&
            reservationDetails.resolved &&
            <Feedback id={reservationDetails.id}/>}
        </Container>
    )
}

export default Reservation;