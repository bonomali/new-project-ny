import React, {useState, useEffect, useCallback} from 'react';
import {useParams} from 'react-router';
import { Container } from 'semantic-ui-react'
import Feedback from './Feedback.jsx'
import WaitDetails from './WaitDetails.jsx'
import axios from 'axios';

function Reservation(props) {
    const { id } = useParams();

    const convertReservationToState = useCallback((reservation) => {
        const expCallStartMin = new Date(reservation.window.min)
        const expCallStartMax = new Date(reservation.window.max)

        return {
            id: id,
            topic: "Business", // Not in response yet, hard coded for demo.
            resolved: reservation.resolution != null,
            expCallStartMin, expCallStartMax,
        };
    }, [id]);

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
    }, [id, convertReservationToState]);

    useEffect(() => {
        fetchReservation(); // Run this once upfront to get the data.

        // Set interval to refetch data every ten seconds.
        const interval = setInterval(() => {
            fetchReservation();
        }, 1000 * 10);
        return () => clearInterval(interval);
    }, [fetchReservation]);

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
