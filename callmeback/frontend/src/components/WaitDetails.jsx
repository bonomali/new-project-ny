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

import React, {useState} from 'react';
import {Container, Icon} from 'semantic-ui-react';
import {Link} from 'react-router-dom';
import Moment from 'react-moment';
import FaqAccordion from './FaqAccordion.jsx';
import moment from 'moment';

/**
 * @param {object} props contains information about the reservation
 * @return {string} Container displaying the expected wait time for a particular
 * reservation
 */
function WaitDetails(props) {
  const {
    topic,
    callStartMin,
    callStartMax,
    id,
    useMovingAverage,
  } = props.reservationDetails;

  const callStartFormatted = (
    <Moment format='h:mm A'>{callStartMin}</Moment>
  );
  const callStartMaxFormatted = (
    <Moment format='h:mm A'>{callStartMax}</Moment>
  );

  const [checked, setCheckbox] = useState(false);

  const now = moment();
  const callStartMaxMoment = moment(callStartMax);
  const callStartMaxDuration = moment.duration(callStartMaxMoment.diff(now));
  const longWait = callStartMaxDuration.asHours() >= 24;
  const iconName = longWait ? 'calendar alternate outline' : 'clock';

  let waitTimeEstimate;
  if (longWait) {
    waitTimeEstimate = (
      <Moment format={'ddd, MMMM Do'}>{callStartMax}</Moment>
    );
  } else {
    waitTimeEstimate = (
      <div>
        <span>
          <Moment fromNow ago>
            {callStartMin}
          </Moment>{' '}
          -{' '}
          <Moment fromNow ago>
            {callStartMax}
          </Moment>
        </span>
      </div>
    );
  }

  return (
    <Container text className='paper'>
      {/* Ensure props are loaded before rendering the component */}
      {!!callStartMin && (
        <div>
          <div style={{textAlign: 'center'}}>
            <div style={{fontSize: '20px'}}>
              We'll call you {longWait ? 'on' : 'in'}
            </div>
            <div style={{display: 'inline-table', alignItems: 'center'}}>
              <div style={{display: 'table-row', fontSize: '30px'}}>
                <span
                  style={{
                    display: 'table-cell',
                    textAlign: 'right',
                    paddingRight: '4px',
                    verticalAlign: 'top',
                  }}
                >
                  <Icon name={iconName} />
                </span>
                <label style={{display: 'table-cell'}}>
                  {waitTimeEstimate}
                </label>
              </div>
            </div>
            <br/>
            <Link
              to={{
                pathname: '/cancel',
                state: {
                  callStartMin: callStartMin,
                  callStartMax: callStartMax,
                  id: id,
                  useMovingAverage: useMovingAverage,
                },
              }}
              style={{fontSize: '12px'}}
            >
              Cancel call
            </Link>
          </div>
          <br />
          <div style={{textAlign: 'left', fontSize: 'small'}}>
            You have requested a call with New York State about <b>{topic}</b>.
            Someone will call you between {callStartFormatted} and{' '}
            {callStartMaxFormatted}. Keep your phone nearby!
            <br />
            <br />
            <div style={{display: 'table-row'}}>
              <span
                style={{
                  display: 'table-cell',
                  paddingRight: '4px',
                  verticalAlign: 'top',
                }}
              >
                <input
                  type='checkbox'
                  onChange={() => {
                    setCheckbox(!checked);
                  }}
                  value={checked}
                />
              </span>
              <label style={{display: 'table-cell'}}>
                Text me five minutes before as a reminder.{' '}
                <a href='#no-op'>Data charges</a> may apply.
              </label>
              <br />
            </div>
          </div>
          <div className='faq-title'>
            Common questions about <b>{topic}</b>
          </div>
          <div className='accordion'>
            <FaqAccordion />
          </div>
        </div>
      )}
    </Container>
  );
}

export default WaitDetails;
