import React, { useState } from 'react';
import { Container, Icon } from 'semantic-ui-react'
import { Link } from 'react-router-dom';
import Moment from 'react-moment';

function WaitDetails(props) {
    const {topic, expCallStartMin, expCallStartMax, waitMin, waitMax, id} = props.reservationDetails

    const callStartFormatted = <Moment format="h:mm A">{expCallStartMin}</Moment>
    const callStartMaxFormatted = <Moment format="h:mm A">{expCallStartMax}</Moment>

    const [checked, setCheckbox] = useState(false)

    const numMinutesInHour = 60;
    const numHoursInDay = 24;
    const numMinutesInDay = numHoursInDay * numMinutesInHour;
    const minWaitInHours = waitMin / numMinutesInHour;
    const minWaitInDays = waitMin / numMinutesInDay;

    return (
        <Container text className='paper'>
            <div>
                <div style={{"textAlign":"center"}} >
                    <div style={{"fontSize":"20px"}}>
                        We'll call you back in
                    </div>
                    {waitMin < numMinutesInHour &&
                    <div style={{"fontSize":"30px"}}>
                        <Icon name='clock' />  {waitMin} - {waitMax} min
                    </div>}
                  {minWaitInHours >= 1 && minWaitInHours < numHoursInDay &&
                  <div style={{"fontSize":"30px"}}>
                    <Icon name='clock' />  {minWaitInHours} - {waitMax / numMinutesInHour} hours
                  </div>}
                  {minWaitInDays >= 1 &&
                  <div style={{"fontSize":"30px"}}>
                    <Icon name='calendar alternate outline' />  {minWaitInDays} - {waitMax / numMinutesInDay} days
                  </div>}
                    <Link
                        to={{
                            pathname: "/cancel",
                            state: {
                                expCallStartMin: expCallStartMin,
                                expCallStartMax: expCallStartMax,
                                id: id,
                            }
                        }}
                        style={{"fontSize":"12px"}}
                    >
                        Cancel call back
                    </Link>
                </div>
                <br/>
                <p style={{"textAlign":"left", "fontSize":"small"}}>
                    You have requested a call with New York State about <b>{topic}</b>.
                    Someone will call you between {callStartFormatted} and {callStartMaxFormatted}.
                    Keep your phone nearby!
                    <br/>
                    <br/>
                    <div style={{"display":"table-row"}}>
                    <span style={{"display":"table-cell", "paddingRight":"4px", "verticalAlign":"top"}}>
                        <input type="checkbox" onChange={()=>{setCheckbox(!checked)}} value={checked}/>
                    </span>
                    <label style={{"display":"table-cell"}}>Text me five minutes before as a reminder. <a>Data charges</a> may apply.</label>
                    </div>
                </p>
            </div>
        </Container>
    )

}

export default WaitDetails;