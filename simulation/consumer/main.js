const simulation = require('./lib/simulation');
const yargs = require('yargs');

// command line arguments
// sample call: node main.js --calls 100 --period 60 --speed-up 120 --api-url 'http://192.168.64.4:31442'
const argv = yargs
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
    // Do stuff
}