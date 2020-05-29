const axios = require('axios');
const faker = require('faker');
const moment = require('moment');

/**
 * Sends a call request with a random name, phone number and query. The call request
 * date will be the current date minus the rewindRequestDateMins.
 * 
 * @param {} apiUrl the url of the API
 * @param {*} rewindRequestDateMins the number of minutes that should be subtracted from the current
 *  time to get the requestDate of the reservation
 */
function sendReservation(apiUrl, rewindRequestDateMins) {
    const queries =
        ['I lost my job, how can I get unemployment?',
         'I need help with small business aid',
         'How can I renew my license when the DMV is closed?']
    const reservation = {
        preferredName: faker.name.findName(),
        contactPhone: faker.phone.phoneNumber(),
        query: queries[Math.round(Math.random()*(queries.length-1))],
        requestDate: moment().subtract(rewindRequestDateMins, 'minutes').toDate(),
    };
    axios.post(apiUrl+'/api/v1/reservations', reservation)
        .then((response) => console.log(response.data))
        .catch((error) => console.log(error));
}

module.exports = {
    sendReservation,
}
