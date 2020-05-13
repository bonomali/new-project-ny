const axios = require('axios');

function sendReservation(hostname) {
    const queries =
        ['I lost my job, how can I get unemployment?',
         'I need help with small business aid',
         'How can I renew my license when the DMV is closed?']
    const reservation = {
        //preferredName: faker.name.findName(),
        contactPhone: Math.floor(100000000 + Math.random() * 900000000).toString(),
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