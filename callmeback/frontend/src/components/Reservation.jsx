import React, {useState, useEffect, useCallback} from 'react';
import {useParams} from 'react-router';
import { Container } from 'semantic-ui-react'
import Feedback from './Feedback.jsx'
import WaitDetails from './WaitDetails.jsx'
import axios from 'axios';

import moment from 'moment';

function Reservation(props) {
    const { id } = useParams();
    const now = new Date();
    

    const convertReservationToState = (reservation) => {
        const expCallStartMin = new Date(reservation.window.min)
        const expCallStartMax = new Date(reservation.window.max)
        const createdDate = reservation.reservationCreatedDate;

        return {
            id: id,
            topic: "Business", // Not in response yet, hard coded for demo.
            resolved: reservation.resolution != null,
            expCallStartMin, expCallStartMax, createdDate
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
            const reservation_state = convertReservationToState(response.data);

            console.log("current date: " + now);
            console.log(reservation_state);
            
            const events_response = await axios.get("/api/v1/reservations/search/countByEventsNullAndReservationCreatedDateLessThan?date=" + "16 May 2020");
                //reservation_state.created_date);
            const number_of_reservations = events_response.data;
            console.log("num reservations is: " + number_of_reservations);
            
            const currentTime = moment();
            const minWaitTime = currentTime.add(number_of_reservations*10, 'minutes');
            const maxWaitTime = currentTime.add(number_of_reservations*20, 'minutes');

            // Update the min/max fields to use reservations + (number_of_reservations * 25000)
            reservation_state.expCallStartMin += minWaitTime;
            reservation_state.expCallStartMax += maxWaitTime;

            console.log("s min wait time" + reservation_state.expCallStartMin);
            console.log("max wait time" + reservation_state.expCallStartMin);

            console.log("final state is: " + reservation_state);
        
            // SetReservation again

            setReservationDetails(reservation_state);
        }
        catch (error) {
            console.log(error); // Add other error handling.
        }
    }, [id]); // later need to add date

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