const simulation = require('./lib/simulation');
const yargs = require('yargs');

// command line arguments
const argv = yargs
    .option('callsPerPeriod', {
        alias: 'calls',
        description: 'Number of calls that should be sent per period',
        type: 'number',
        default: 10,
    })
    .option('periodLength', {
        alias: 'period',
        description: 'Length of period for calls to be sent. In wall clock time, in minutes',
        type: 'number',
        default: 60,
    })
    .option('apiUrl', {
        alias: 'api-url',
        description: 'The URL (host + port) the call me back API is running on',
        type: 'string',
        required: true,
    })
    .option('rewindRequestDatesBy', {
        alias: 'rewind-request-dates-by',
        description: 'The number of minutes by which to rewind the request date from the present',
        type: 'number',
        default: 0, // present time
    })
    .help()
    .alias('help', 'h')
    .argv;

main();

/**
 * Asynchronous method for sending call requests.
 */
function sendReservations (apiUrl, callsPerPeriod, rewindRequestDateMins) {
    for (i = 0; i < callsPerPeriod; i++) {
        simulation.sendReservation(apiUrl, rewindRequestDateMins)
    }
}

/**
 * Producer script for generating call requests. To ensure that the database does not
 * receive a massive backlog of unaddressed calls, make sure that the --calls and --period
 * parameters will introduce the same volume of calls that can be handled by the script
 * at ../consumer/main.js.
 * 
 * sample call: node main.js --calls 100 --period 60 --speed-up 120 --api-url 'http://192.168.64.4:31442'
 */
function main() {
    const delay = argv.periodLength * 60 * 1000 // periodLength is in minutes, turn into milliseconds
    sendReservations(argv.apiUrl, argv.callsPerPeriod, argv.rewindRequestDatesBy) // run once to start
    setInterval(() => { sendReservations(argv.apiUrl, argv.callsPerPeriod, argv.rewindRequestDatesBy) }, delay)
}