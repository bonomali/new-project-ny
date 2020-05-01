import React from 'react';
import { Container, Icon } from 'semantic-ui-react'
import Moment from 'react-moment';

function WaitDetails(props) {
    const {topic, expCallStartMin, expCallStartMax, waitMin, waitMax} = props.reservationDetails

    const callStartFormatted = <Moment format="h:mm A">{expCallStartMin}</Moment>
    const callStartMaxFormatted = <Moment format="h:mm A">{expCallStartMax}</Moment>

    return (
        <Container text className='paper'>
            <div>
                <div style={{"textAlign":"center"}} >
                    <div style={{"fontSize":"20px"}}>
                        We'll call you back in
                    </div>
                    <div style={{"fontSize":"30px"}}>
                        <Icon name='clock' />  {waitMin} - {waitMax} min
                    </div>
                </div>
                <br/>
                <p style={{"textAlign":"left", "fontSize":"small"}}>
                    You have requested a call with New York State about <b>{topic}</b>.
                    Someone will call you between {callStartFormatted} and {callStartMaxFormatted}.
                    Keep your phone nearby!
                </p>
            </div>
        </Container>
    )

}

export default WaitDetails;