import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import Routes from './Routes.jsx';
import moment from 'moment';

moment.updateLocale('en', {
  // Abbreviate relative time rendered output from "minutes" to "min"
  relativeTime: {
    m: '1 min',
    mm: '%d min',
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
