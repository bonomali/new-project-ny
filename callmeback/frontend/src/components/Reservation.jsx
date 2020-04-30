import React, {useState} from 'react';
import {useParams} from 'react-router';
import Time from 'react-time';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import useStyles from  '../styles';
import WatchLaterIcon from '@material-ui/icons/WatchLater';

function Reservation() {
    const classes = useStyles();
    const { id } = useParams();
    const [reservationDetails] = useState(() => {
        // TODO: Call reservations/{id} endpoint to get reservation data
        // and reservations/{id}/statistics to get wait information
        const topic = "Employment";
        const expCallStart = new Date();
        const expCallStartMax = new Date(expCallStart.getTime() + 5*60000)
        const waitMS = new Date() - expCallStart;
        const waitMin = Math.round(((waitMS % 86400000) % 3600000) / 60000); // minutes
        const waitMax = waitMin + 5;

        return {topic, waitMin, waitMax, expCallStart, expCallStartMax}
    })

    const callStartFormatted = <Time value={reservationDetails.expCallStart} format="h:mm A" />
    const callStartMaxFormatted = <Time value={reservationDetails.expCallStartMax} format="h:mm A" />

    return (
        <Container component="main" maxWidth="xs">
            {reservationDetails &&
            <div className={classes.paper}>
                <Typography component="h1" variant="h5" align='center'>
                    We'll call you back regarding <b>{reservationDetails.topic}</b> in
                </Typography>
                <br />
                <Grid container spacing={1} justify='center' alignItems='center'>
                    <Grid item>
                        <WatchLaterIcon fontSize='large' color='action' />
                    </Grid>
                    <Grid item>
                        <Typography component="h1" variant="h4">
                            {reservationDetails.waitMin} - {reservationDetails.waitMax} min
                        </Typography>
                    </Grid>
                </Grid>
                <Typography variant="subtitles1" justify='center'>
                    between {callStartFormatted} and {callStartMaxFormatted}
                </Typography>
                <br />
                
            </div>}
        </Container>
    )
}

export default Reservation;