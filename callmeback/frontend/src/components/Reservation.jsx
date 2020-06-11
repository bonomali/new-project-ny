/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
      return convertReservationToState(props.location.state.reservation, id,
          props.useMovingAverage);
    }
    return {};
  });

  const fetchReservation = useCallback(async () => {
    try {
      const response = await axios.get('/api/v1/reservations/' + id);
      const reservation = convertReservationToState(response.data, id,
          props.useMovingAverage);
      setReservationDetails(reservation);
    } catch (error) {
      console.log(error); // Add other error handling.
    }
  }, [id, props.useMovingAverage]);

  useEffect(() => {
    fetchReservation(); // Run this once upfront to get the data.

    // Set interval to refetch data every ten seconds.
    const interval = setInterval(() => {
      fetchReservation();
    }, 1000 * 10);
    return () => clearInterval(interval);
  }, [fetchReservation]);

  return (
    <Container
      text
      textAlign='center'
      className='paper'
      data-testid='reservation-container'
    >
      {reservationDetails.id !== '' && !reservationDetails.resolved && (
        <WaitDetails reservationDetails={reservationDetails} />
      )}
      {reservationDetails.id !== '' && reservationDetails.resolved && (
        <Feedback id={reservationDetails.id} />
      )}
    </Container>
  );
}

/*
** convertReservationToState takes the reservation and returns the state
** for the component.
*/
function convertReservationToState(reservation, id, useMovingAverage) {
  const callStartMin = new Date(useMovingAverage ?
      reservation.window.movingAvgWindow.min :
      reservation.window.naiveWindow.min);
  const callStartMax = new Date(useMovingAverage ?
      reservation.window.movingAvgWindow.max :
      reservation.window.naiveWindow.max);

  return {
    id: id,
    topic: 'Business', // Not in response yet, hard coded for demo.
    resolved: reservation.resolution != null,
    callStartMin,
    callStartMax,
    useMovingAverage: useMovingAverage,
  };
}

export default Reservation;
