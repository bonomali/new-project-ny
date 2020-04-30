import React, {useState} from 'react';
import {useParams} from 'react-router';
import Moment from 'react-moment';
import { Container, Icon } from 'semantic-ui-react'

function Reservation() {
    const { id } = useParams();
    const [reservationDetails] = useState(() => {
        // TODO: Call reservations/{id} endpoint to get reservation data
        // (this would not be necessary if we had a global redux state with the
        // reservation data we got upon form submit)
        // and generate a random expected wait time here.
        const topic = "Employment";
        const expCallStart = new Date();
        const expCallStartMax = new Date(expCallStart.getTime() + 5*60000)
        const waitMS = new Date() - expCallStart;
        const waitMin = Math.round(((waitMS % 86400000) % 3600000) / 60000); // minutes
        const waitMax = waitMin + 5;

        return {topic, waitMin, waitMax, expCallStart, expCallStartMax}
    })

    const callStartFormatted = <Moment format="h:mm A">{reservationDetails.expCallStart}</Moment>
    const callStartMaxFormatted = <Moment format="h:mm A">{reservationDetails.expCallStartMax}</Moment>

    return (
        <Container text textAlign='center' className='paper'>
            <div>
                <div style={{"font-size":"20px"}}>
                    How was your call back?
                </div>
                <div style={{"font-size":"30px"}}>
                    <Icon name='clock' />  {reservationDetails.waitMin} - {reservationDetails.waitMax} min
                </div>
                <div>
                    between {callStartFormatted} and {callStartMaxFormatted}
                </div>
            </div>
        </Container>
    )
}

export default Reservation;