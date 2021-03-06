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

// Serves the built React.js app.

const express = require('express');
const app = express();

app.use(express.static('public'));

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
