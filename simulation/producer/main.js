const simulation = require('./lib/simulation');
const yargs = require('yargs');

// command line arguments
// sample call: node main.js --callsPerHour 100 --speedUpFactor 120 --apiUrl 'http://192.168.64.4:31442'
const argv = yargs
    .option('callsPerHour', {
        alias: 'callsPerHour',
        description: 'Number of calls that should be sent per hour',
        type: 'number',
        default: 10,
    })
    .option('speedUpFactor', {
        alias: 'speedUpFactor',
        description: 'The factor by which real time should be sped up',
        type: 'number',
        default: 1, // wall clock time
    })
    .option('apiUrl', {
        alias: 'apiUrl',
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
        for (i = 0; i < argv.callsPerHour; i++) {
            simulation.sendReservation(argv.apiUrl)
        }
    }
    let delay = 3600000 // 3600000 milliseconds = 1 hour
    if (argv.speedUpFactor) {
        delay = delay / argv.speedUpFactor
    }
    // TODO: Take into account speedUpFactor when adding call request times
    sendReservations() // run once to start
    setInterval(sendReservations, delay)
}