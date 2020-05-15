const axios = require('axios');
const faker = require('faker');

function sendReservation(apiUrl) {
    const queries =
        ['I lost my job, how can I get unemployment?',
         'I need help with small business aid',
         'How can I renew my license when the DMV is closed?']
    const reservation = {
        preferredName: faker.name.findName(),
        contactPhone: faker.phone.phoneNumber(),
        query: queries[Math.round(Math.random()*(queries.length-1))],
    };
    axios.post(apiUrl+'/api/v1/reservations', reservation)
        .then((response) => console.log(response))
        .catch((error) => console.log(error))
}

module.exports = {
    sendReservation,
}