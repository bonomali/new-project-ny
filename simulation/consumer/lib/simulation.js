const axios = require('axios');

function startCall(apiUrl) {
    return axios.patch(apiUrl + '/api/v1/reservations/search/startNextCall', reservation)
        .then((response) =>  { 
            console.log(response); 
            return response;
        })
        .catch((error) => console.log(error));
}

function endCall(apiUrl, reservationId, reservationEvents) {
    if(!reservationEvents) {
        reservationEvents = [];
    }

    const completedEvent = {
        date: Date.now(),
        type: 'COMPLETED',
    };
    reservationEvents.push(completedEvent);

    const request = { events: reservationEvents };
    return axios.patch(apiUrl + `/api/v1/reservation/${reservationId}`, request)
        .then((response) => {
            console.log(response);
            return response;
        })
        .catch((error) => console.log(error))
}


module.exports = {
    startCall,
    endCall,
}