import React, {useState, useEffect} from 'react';
import {useParams} from 'react-router';
import { Container } from 'semantic-ui-react'
import Feedback from './Feedback.jsx'
import WaitDetails from './WaitDetails.jsx'
import axios from 'axios';

function Reservation() {
    const { id } = useParams();

    const [reservationDetails, setReservationDetails] = useState({});

    const fetchReservation = async () => {
        // This will be replaced with the callback interval from API call.
        const now = new Date();
        const expCallStartMin = new Date(now.getTime() + 10*60000)
        const expCallStartMax = new Date(expCallStartMin.getTime() + 5*60000)

        // Calculations for wait time given the min and max exp call time.
        const waitMinMS = expCallStartMin - now;
        const waitMaxMS = expCallStartMax - now;
        const waitMin = Math.round(((waitMinMS % 86400000) % 3600000) / 60000); // minutes
        const waitMax = Math.round(((waitMaxMS % 86400000) % 3600000) / 60000); 

        try {
            const response = await axios.get("/api/v1/reservations/" + id); //concatenate id variable
            console.log(response.data)
    
            setReservationDetails({
                id: id,
                topic: "Employment", // Not in response yet
                resolved: response.data.resolution != null,
                waitMin, waitMax, expCallStartMin, expCallStartMax, // Not in response yet
            });
        }
        catch (error) {
            console.log(error); // Add other error handling.
        }
    }

    useEffect(() => {
        fetchReservation(); // Run this once upfront to get the data.
    
        // Set interval to refetch data every ten seconds.
        const interval = setInterval(() => {
            fetchReservation();
        }, 1000 * 10);
        return () => clearInterval(interval);
    }, [])

    return (
        <Container text textAlign='center' className='paper'>
            {reservationDetails.id !== "" &&
            !reservationDetails.resolved &&
            <WaitDetails reservationDetails={reservationDetails}/>}
            {reservationDetails.id !== "" &&
            reservationDetails.resolved &&
            <Feedback id={id}/>}
        </Container>
    )
}

export default Reservation;