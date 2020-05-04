import React, {useState, useEffect, useCallback} from 'react';
import {useParams} from 'react-router';
import { Container } from 'semantic-ui-react'
import Feedback from './Feedback.jsx'
import WaitDetails from './WaitDetails.jsx'
import axios from 'axios';

function Reservation(props) {
    const { id } = useParams();
    const now = new Date();

    const convertReservationToState = (reservation) => {
        const expCallStartMin = new Date(reservation.window.min)
        const expCallStartMax = new Date(reservation.window.max)

        // Calculations for wait time given the min and max exp call time.
        const waitMinMS = expCallStartMin - now;
        const waitMaxMS = expCallStartMax - now;
        const waitMin = Math.round(((waitMinMS % 86400000) % 3600000) / 60000); // minutes
        const waitMax = Math.round(((waitMaxMS % 86400000) % 3600000) / 60000); 

        return {
            id: id,
            topic: "Employment", // Not in response yet
            resolved: reservation.resolution != null,
            waitMin, waitMax, expCallStartMin, expCallStartMax,
        };
    }

    const [reservationDetails, setReservationDetails] = useState(() => {
        if (!!props && !!props.location.state) {
            return convertReservationToState(props.location.state.reservation)
        }
        return {}
    });

    const fetchReservation = useCallback(async () => {
        try {
            const response = await axios.get("/api/v1/reservations/" + id);
            const reservation = convertReservationToState(response.data);
            setReservationDetails(reservation);
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