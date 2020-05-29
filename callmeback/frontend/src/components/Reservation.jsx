import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router';
import { Container } from 'semantic-ui-react';
import Feedback from './Feedback.jsx';
import WaitDetails from './WaitDetails.jsx';
import axios from 'axios';

function Reservation(props) {
  const { id } = useParams();

  const [reservationDetails, setReservationDetails] = useState(() => {
    if (!!props && !!props.location.state) {
      return convertReservationToState(props.location.state.reservation, id);
    }
    return {};
  });

  const fetchReservation = useCallback(async () => {
    try {
      const response = await axios.get('/api/v1/reservations/' + id);
      const reservation = convertReservationToState(response.data, id);
      setReservationDetails(reservation);
    } catch (error) {
      console.log(error); // Add other error handling.
    }
  }, [id]);

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
      {reservationDetails.id !== '' && !reservationDetails.resolved && (
        <WaitDetails reservationDetails={reservationDetails} />
      )}
      {reservationDetails.id !== '' && reservationDetails.resolved && (
        <Feedback id={reservationDetails.id} />
      )}
    </Container>
  );
}

function convertReservationToState(reservation, id) {
  const naiveExpCallStartMin = new Date(reservation.window.naiveWindow.min);
  const naiveExpCallStartMax = new Date(reservation.window.naiveWindow.max);
  const maExpCallStartMin = new Date(reservation.window.movingAvgWindow.min);
  const maExpCallStartMax = new Date(reservation.window.movingAvgWindow.max);

  return {
    id: id,
    topic: 'Business', // Not in response yet, hard coded for demo.
    resolved: reservation.resolution != null,
    naiveExpCallStartMin,
    naiveExpCallStartMax,
    maExpCallStartMin,
    maExpCallStartMax,
  };
}

export default Reservation;
