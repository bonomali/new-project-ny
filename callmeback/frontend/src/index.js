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

ReactDOM.render(
  <React.StrictMode>
    <Router>
      <Routes />
    </Router>
  </React.StrictMode>,
  document.getElementById('root')
);
