import React from 'react';
import { Container, Icon } from 'semantic-ui-react'
import Moment from 'react-moment';

function WaitDetails(props) {
    const {topic, expCallStartMin, expCallStartMax, waitMin, waitMax} = props.reservationDetails

    const callStartFormatted = <Moment format="h:mm A">{expCallStartMin}</Moment>
    const callStartMaxFormatted = <Moment format="h:mm A">{expCallStartMax}</Moment>

    return (
        <Container text textAlign='center' className='paper'>
            <div>
                <div style={{"fontSize":"20px"}}>
                    We'll call you back regarding <b>{topic}</b> in
                </div>
                <div style={{"fontSize":"30px"}}>
                    <Icon name='clock' />  {waitMin} - {waitMax} min
                </div>
                <div>
                    between {callStartFormatted} and {callStartMaxFormatted}
                </div>
            </div>
        </Container>
    )

}

export default WaitDetails;