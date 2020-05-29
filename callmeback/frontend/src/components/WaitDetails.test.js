import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { BrowserRouter as Router } from 'react-router-dom';
import WaitDetails from './WaitDetails';

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
  expect(getAllByText('a few seconds').length).toEqual(4);
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
  expect(getAllByText('an hour').length).toEqual(2);
  expect(getAllByText('2 hours').length).toEqual(2);
});
