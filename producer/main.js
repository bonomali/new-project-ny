const simulation = require('./lib/simulation');
const yargs = require('yargs');

// command line arguments
// sample call: node main.js --callsPerHour 100 --speedUpFactor 120 --hostname 'http://192.168.64.4:31442'
const argv = yargs
    .option('callsPerHour', {
        alias: 'callsPerHour',
        description: 'Number of calls that should be sent per hour',
        type: 'number',
    })
    .option('speedUpFactor', {
        alias: 'speedUpFactor',
        description: 'The factor by which real time should be sped up',
        type: 'number',
    })
    .option('hostname', {
        alias: 'hostname',
        description: 'The hostname the call me back API is running on',
        type: 'string',
    })
    .help()
    .alias('help', 'h')
    .argv;

main();

function main() {
    const sendReservations = () => {
        for (i = 0; i < argv.callsPerHour; i++) {
            simulation.sendReservation(argv.hostname)
        }
    }
    let delay = 3600000 // 3600000 milliseconds = 1 hour
    if (argv.speedUpFactor) {
        delay = delay / argv.speedUpFactor
    }
    sendReservations() // run once to start
    setInterval(() => { sendReservations() }, delay)
}