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
    callStartMin: mockDate,
    callStartMax: mockDate,
    id: 'mockid',
  };
  const { getByText, getAllByText } = render(
    <Router>
      <WaitDetails reservationDetails={mockReservationDetails} />
    </Router>
  );
  expect(getByText("We'll call you in")).toBeDefined();
  expect(getByText('Cancel call')).toBeDefined();
  expect(getAllByText('now').length).toEqual(2);
  expect(getAllByText('mocktopic').length).toEqual(2);
});

test('renders wait details with 1-2 hour wait', async () => {
  const mockDateMin = new Date();
  mockDateMin.setHours(mockDateMin.getHours() + 1);
  const mockDateMax = new Date(mockDateMin);
  mockDateMax.setHours(mockDateMax.getHours() + 1);
  const mockReservationDetails = {
    topic: 'mocktopic',
    callStartMin: mockDateMin,
    callStartMax: mockDateMax,
    id: 'mockid',
  };
  const { getByText } = render(
    <Router>
      <WaitDetails reservationDetails={mockReservationDetails} />
    </Router>
  );
  expect(getByText("1 hour")).toBeDefined();
  expect(getByText("2 hours")).toBeDefined();
});

test('renders wait details with 45 mins - 1 hour', async () => {
  const mockDateMin = new Date();
  mockDateMin.setMinutes(mockDateMin.getMinutes() + 45);
  // Add 30 mins to the starting wait time.
  const mockDateMax = new Date(mockDateMin);
  mockDateMax.setMinutes(mockDateMax.getMinutes() + 30);
  const mockReservationDetails = {
    topic: 'mocktopic',
    callStartMin: mockDateMin,
    callStartMax: mockDateMax,
    id: 'mockid',
  };
  const { getByText } = render(
    <Router>
      <WaitDetails reservationDetails={mockReservationDetails} />
    </Router>
  );
  expect(getByText("45 min")).toBeDefined();
  expect(getByText("1 hour")).toBeDefined();
});
