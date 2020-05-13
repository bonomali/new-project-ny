const simulation = require('./lib/simulation');
const yargs = require('yargs');

const hostname = 'http://192.168.64.3:31209';

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
    .help()
    .alias('help', 'h')
    .argv;

main();

function main() {
    const sendReservations = () => {
        for (i = 0; i < argv.callsPerHour; i++) {
            simulation.sendReservation(hostname)
        }
    }
    let delay = 3600000 // 3600000 milliseconds = 1 hour
    if (argv.speedUpFactor) {
        delay = delay / argv.speedUpFactor
    }
    sendReservations() // run once to start
    setInterval(() => { sendReservations() }, delay)
}