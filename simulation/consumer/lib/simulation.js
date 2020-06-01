const axios = require('axios');

function startCall(apiUrl) {
    return axios.get(apiUrl + '/api/v1/reservations/search/startNextCall')
        .then((response) =>  { 
            console.log(JSON.stringify(response.data)); 
            return response;
        })
        .catch((error) => {
            if (!error.response || error.response.status != '404') {
                console.log(error) // don't print the error if there is no reservation to start; that is WAI
            }
        });
}

function endCall(reservation) {
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
    
    return axios.patch(reservation._links.self.href, request)
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
