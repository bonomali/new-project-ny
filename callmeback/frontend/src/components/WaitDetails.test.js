import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { BrowserRouter as Router } from 'react-router-dom';
import WaitDetails from './WaitDetails';
import moment from 'moment';

// TODO: Dynamically tie this to the index.js changes so that we don't
// have to manually update both.
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

test('renders wait details with current time', async () => {
  const mockDate = new Date();
  const mockReservationDetails = {
    topic: 'mocktopic',
    naiveExpCallStartMin: mockDate,
    naiveExpCallStartMax: mockDate,
    maExpCallStartMin: mockDate,
    maExpCallStartMax: mockDate,
    id: 'mockid',
  };
  const { getByText, getAllByText } = render(
    <Router>
      <WaitDetails reservationDetails={mockReservationDetails} />
    </Router>
  );
  expect(getByText("We'll call you in")).toBeDefined();
  expect(getByText('Cancel call')).toBeDefined();
  expect(getAllByText('just a moment').length).toEqual(1);
  expect(getAllByText('mocktopic').length).toEqual(2);
});

test('renders wait details with 1-2 hour wait', async () => {
  const mockDateMin = new Date();
  mockDateMin.setHours(mockDateMin.getHours() + 1);
  const mockDateMax = new Date(mockDateMin);
  mockDateMax.setHours(mockDateMax.getHours() + 1);
  const mockReservationDetails = {
    topic: 'mocktopic',
    naiveExpCallStartMin: mockDateMin,
    naiveExpCallStartMax: mockDateMax,
    maExpCallStartMin: mockDateMin,
    maExpCallStartMax: mockDateMax,
    id: 'mockid',
  };
  const { getAllByText } = render(
    <Router>
      <WaitDetails reservationDetails={mockReservationDetails} />
    </Router>
  );
  expect(getAllByText('1 hour').length).toEqual(2);
  expect(getAllByText('2 hours').length).toEqual(2);
});

test('renders wait details with 45 mins - 1 hour', async () => {
  const mockDateMin = new Date();
  mockDateMin.setMinutes(mockDateMin.getMinutes() + 45);
  // Add 30 mins to the starting wait time.
  const mockDateMax = new Date(mockDateMin);
  mockDateMax.setMinutes(mockDateMax.getMinutes() + 30);
  const mockReservationDetails = {
    topic: 'mocktopic',
    naiveExpCallStartMin: mockDateMin,
    naiveExpCallStartMax: mockDateMax,
    maExpCallStartMin: mockDateMin,
    maExpCallStartMax: mockDateMax,
    id: 'mockid',
  };
  const { getAllByText } = render(
    <Router>
      <WaitDetails reservationDetails={mockReservationDetails} />
    </Router>
  );
  expect(getAllByText('45 min').length).toEqual(2);
  expect(getAllByText('1 hour').length).toEqual(2);
});
