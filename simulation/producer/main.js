const simulation = require('./lib/simulation');
const yargs = require('yargs');

// command line arguments
// sample call: node main.js --calls 100 --period 60 --speed-up 120 --api-url 'http://192.168.64.4:31442'
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
    .option('speedUpFactor', {
        alias: 'speed-up',
        description: 'The factor by which real time should be sped up',
        type: 'number',
        default: 1, // wall clock time
    })
    .option('apiUrl', {
        alias: 'api-url',
        description: 'The URL (host + port) the call me back API is running on',
        type: 'string',
        required: true,
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

function main() {
    const sendReservations = () => {
        for (i = 0; i < argv.callsPerPeriod; i++) {
            simulation.sendReservation(argv.apiUrl)
        }
    }
    let delay = argv.periodLength * 60 * 1000 // periodLength is in minutes, turn into milliseconds
    if (argv.speedUpFactor) {
        delay = delay / argv.speedUpFactor
    }
    // TODO: Take into account speedUpFactor when adding call request times
    sendReservations() // run once to start
    setInterval(sendReservations, delay)
}