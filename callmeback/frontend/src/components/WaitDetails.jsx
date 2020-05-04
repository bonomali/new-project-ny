import React, { useState } from 'react';
import { Container, Icon } from 'semantic-ui-react'
import { Link } from 'react-router-dom';
import Moment from 'react-moment';
import Accordion from './Accordion.jsx';

function WaitDetails(props) {
    const {topic, expCallStartMin, expCallStartMax, waitMin, waitMax, id} = props.reservationDetails

    const callStartFormatted = <Moment format="h:mm A">{expCallStartMin}</Moment>
    const callStartMaxFormatted = <Moment format="h:mm A">{expCallStartMax}</Moment>

    const [checked, setCheckbox] = useState(false)
    
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
                <h2 style={{"textAlign":"left", "fontSize":"medium"}}>
                    Common questions about <b>{topic}</b> 
                </h2>
                <div style={{"textAlign":"left", "fontSize":"small"}} >
                    <Accordion />
                </div>              
            </div>               
        </Container>
    )

}

export default WaitDetails;