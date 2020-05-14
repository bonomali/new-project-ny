const axios = require('axios');

function requestCall(apiUrl) {
    const request = {
    };
    axios.post(apiUrl+'/api/v1/reservations', request)
        .then((response) => console.log(response))
        .catch((error) => console.log(error))
}

module.exports = {
    sendReservation: requestCall,
}