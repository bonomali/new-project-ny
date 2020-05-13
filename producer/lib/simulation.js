const axios = require('axios');
const faker = require('faker');

function sendReservation(hostname) {
    const queries =
        ['I lost my job, how can I get unemployment?',
         'I need help with small business aid',
         'How can I renew my license when the DMV is closed?']
    const reservation = {
        preferredName: faker.name.findName(),
        contactPhone: Math.floor(1000000000 + Math.random() * 9000000000).toString(), // random 10-digit number that doesnt start with 0
        query: queries[Math.round(Math.random()*(queries.length-1))],
    };
    axios.post(hostname+'/api/v1/reservations', reservation)
    .then(
        (response) => { console.log(response)},
        (error) => {console.log(error)}
    );
}

module.exports = {
    sendReservation,
}