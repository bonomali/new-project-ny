// Serves the built React.js app.

const express = require('express');
const path = require('path');
const app = express();

app.use(express.static('public'))

// Serve compiled React JS/CSS.
app.get('/', function(req, res) {
    res.sendFile('index.html');
});

// If it doesn't match a file in the build path, just redirect to
// index.html and have React take care of it.
app.get('*', function(req, res) {
    res.sendFile('index.html');
});

app.listen(3000);
