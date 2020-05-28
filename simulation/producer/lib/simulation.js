const axios = require('axios');
const faker = require('faker');
const moment = require('moment');

function sendReservation(apiUrl) {
    const queries =
        ['I lost my job, how can I get unemployment?',
         'I need help with small business aid',
         'How can I renew my license when the DMV is closed?']
    const reservation = {
        preferredName: faker.name.findName(),
        contactPhone: faker.phone.phoneNumber(),
        query: queries[Math.round(Math.random()*(queries.length-1))],
        requestDate: moment(),
    };
    axios.post(apiUrl+'/api/v1/reservations', reservation)
        .then((response) => {
            let updatedRes = response.data;
            updatedRes.requestDate = moment().subtract(30, 'minutes').toDate()
            axios.patch(response.data._links.self.href, updatedRes)
                .then((response) => { console.log("patch response: ", response.data) })
        })
        .catch((error) => console.log(error))
}

module.exports = {
    sendReservation,
}
