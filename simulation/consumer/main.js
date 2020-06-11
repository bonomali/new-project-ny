/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const simulation = require('./lib/simulation');
const yargs = require('yargs');

// command line arguments
const argv = yargs
    .option('callLength', {
        alias: 'length',
        description: 'Amount of time in minutes that a call is simulated before being marked as completed',
        type: 'number',
        default: 1
    })
    .option('callPeriod', {
        alias: 'period',
        description: 'Amount of time in minutes between calls',
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

/**
 * Returns a function that passes a result into a Promise, delaying the propagation
 * of that result through the Promise for `time` milliseconds. Designed to be used
 * in a promise chain, e.g. the following prints '10' after 1 second:
 * 
 * Promise.resolve(10).then(promiseWait(1000)).then((value) => console.log(value));
 * 
 * @param {number} time 
 */
function promiseWait(time) {
    return result => new Promise(resolve => setTimeout(resolve, time, result));
} 

/**
 * Asynchronous method for completing a call reservation.
 */
function processCall() {
    simulation.startCall(argv.apiUrl)
        .then((response) => response.data)
        .then(promiseWait(argv.callLength * 60 * 1000)) // simulated call duration
        .then(simulation.endCall)
        .catch((error) => console.log(error));
}

/**
 * Consumer script for handling calls. To ensure that the database does not receive
 * a massive backlog of unaddressed calls, make sure that the --length and --period
 * parameters will process the same volume of calls being introduced into the system
 * by the script at ../producer/main.js.
 * 
 * sample call: node main.js --length 1 --period .5 --api-url 'http://192.168.64.4:31442'
 */
function main() {
    processCall();
    const periodMs = argv.callPeriod * 60 * 1000;
    setInterval(processCall, periodMs);
}
