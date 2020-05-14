const simulation = require('./lib/simulation');
const yargs = require('yargs');

// command line arguments
// sample call: node main.js --calls 100 --period 60 --speed-up 120 --api-url 'http://192.168.64.4:31442'
const argv = yargs
    .option('callLength', {
        alias: 'length',
        description: 'Amount of time in minutes that a call is simulated before being marked as completed',
        type: 'number',
        default: 1
    })
    .option('callPeriod', {
        alias: 'period',
        description: 'Frequency in minutes that a new call is started',
        type: 'number',
        default: 5
    })
    .option('apiUrl', {
        alias: 'api-url',
        description: 'The URL (host + port) the call me back API is running on',
        type: 'string',
        required: true,
    })
    .help()
    .alias('help', 'h')
    .argv;

main();

const promiseTimeout = time => () => new Promise(resolve => setTimeout(resolve, time));

function processCall() {
    simulation.startCall(argv.apiUrl)
        .then((response) => response.data.id)
        .then((reservationId) => promiseTimeout(simulation.endCall, argv.callLength, argv.apiUrl, reservationId))
        .then ((response) => console.log(response))
        .catch((error) => console.log(error));

    // simulation.startCall(argv.apiUrl)
    //     .then((response) => {
    //         const reservationId = response.data.id;
    //         return promiseTimeout(simulation.endCall(argv.apiUrl, reservationId), argv.callLength);
    //     })
    //     .then ((response) => console.log(response))
    //     .catch((error) => console.log(error));
}

function main() {
    processCall(argv.apiUrl);

    const periodMs = argv.callPeriod * 1000;
    setTimeout(processCall, periodMs, argv.apiUrl);
}