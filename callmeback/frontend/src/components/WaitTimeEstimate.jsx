import React from 'react';
import Moment from 'react-moment';
import moment from 'moment';

function WaitTimeEstimate(props) {
  const {estimateDate} = props;

  const now = moment();
  const estimateMoment = moment(estimateDate);
  const estimateDuration = moment.duration(estimateMoment.diff(now));

  let timeUnit;
  if(estimateDuration.asMinutes() < 60) {
    timeUnit = 'minutes';
  } else if (estimateDuration.asHours() < 24) {
    timeUnit = 'hours';
  } else {
    timeUnit = 'days';
  }

  return (
      <span>
        <Moment diff={Date.now()} unit={timeUnit}> {estimateDate} </Moment>
        {' ' + timeUnit}
      </span>
  )
}

export default WaitTimeEstimate;
