const axios = require('axios');

function startCall(apiUrl) {
    return axios.get(apiUrl + '/api/v1/reservations/search/startNextCall')
        .then((response) =>  { 
            console.log(JSON.stringify(response.data)); 
            return response;
        })
        .catch((error) => console.log(error));
}

function endCall(apiUrl, reservation) {
    let reservationEvents = reservation.events;
    if(!reservationEvents) {
        reservationEvents = [];
    }

    const completedTime = Date.now();

    const resolution = {
        date: completedTime,
        type: 'RESOLVED',
    };

    const completedEvent = {
        date: completedTime,
        type: 'COMPLETED',
    };
    reservationEvents.push(completedEvent);

    const request = { 
        events: reservationEvents,
        resolution: resolution,
    };
    
    let hrefArray = reservation._links.self.href.split('/');
    let reservationId = hrefArray[hrefArray.length - 1];
    return axios.patch(apiUrl + '/api/v1/reservations/' + reservationId, request)
        .then((response) => {
            console.log(JSON.stringify(response.data));
            return response;
        })
        .catch((error) => console.log(error))
}


module.exports = {
    startCall,
    endCall,
}