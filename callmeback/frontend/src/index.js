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

import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import Routes from './Routes.jsx';
import moment from 'moment';

moment.updateLocale('en', {
  // Abbreviate relative time rendered output from "minutes" to "min".
  // Override any value of seconds to show "now" rather than "a few seconds".
  relativeTime: {
    s: 'now',
    m: '1 min',
    mm: '%d min',
    h: '1 hour',
  },
});
// Specify exact minutes in the 1-59 range. Without this, 45-59 mins is
// rendered as "1 hour".
moment.relativeTimeThreshold('m', 59);

ReactDOM.render(
  <React.StrictMode>
    <Router>
      <Routes />
    </Router>
  </React.StrictMode>,
  document.getElementById('root')
);
