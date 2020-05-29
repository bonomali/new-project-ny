import React from 'react';
import { render, screen } from '@testing-library/react';
import { waitFor } from '@testing-library/dom';
import '@testing-library/jest-dom/extend-expect';
import axiosMock from 'axios';
import routeData from 'react-router';
import { BrowserRouter as Router } from 'react-router-dom';
import Reservation from './Reservation';

// TODO: Add test checking that fetchReservation is called at interval
// TODO: Fix "use act()" warnings

jest.mock('axios');

const mockParams = {
  id: 'mockid',
};
const mockUnresolvedReservation = {
  window: {
    naiveWindow: {
      min: new Date(),
      max: new Date(),
    },
    movingAvgWindow: {
      min: new Date(),
      max: new Date(),
    },
  },
  resolution: null,
};
const mockResolvedReservation = {
  window: {
    naiveWindow: {
      min: new Date(),
      max: new Date(),
    },
    movingAvgWindow: {
      min: new Date(),
      max: new Date(),
    },
  },
  resolution: {
    date: new Date(),
    type: 'RESOLVED',
  },
};
const WaitDetailsString = "We'll call you in";
const FeedbackString = 'How was your call?';

beforeAll(() => {
  jest.useFakeTimers();
});

beforeEach(() => {
  jest.resetAllMocks();
  jest.spyOn(routeData, 'useParams').mockReturnValue(mockParams);
});

test('renders WaitDetails for unresolved reservation', async () => {
  const mockLocation = {
    pathname: '/reservations/mockid',
    state: {
      reservation: mockUnresolvedReservation,
    },
  };
  axiosMock.get.mockResolvedValue({
    data: mockUnresolvedReservation,
  });
  render(
    <Router>
      <Reservation location={mockLocation} />
    </Router>
  );

  // Confirm the WaitDetails component is rendered
  expect(screen.getByText(WaitDetailsString)).toBeDefined();

  // Wait for fetchReservation to be called
  await waitFor(() => expect(axiosMock.get).toHaveBeenCalledTimes(1));
  expect(axiosMock.get).toHaveBeenCalledWith('/api/v1/reservations/mockid');

  // Confirm the WaitDetails component is rendered
  await waitFor(() =>
    expect(screen.getByText(WaitDetailsString)).toBeDefined()
  );
});

test('renders Feedback for resolved reservation', async () => {
  const mockLocation = {
    pathname: '/reservations/mockid',
    state: {
      reservation: mockResolvedReservation,
    },
  };
  axiosMock.get.mockResolvedValue({
    data: mockResolvedReservation,
  });
  render(
    <Router>
      <Reservation location={mockLocation} />
    </Router>
  );

  // Confirm the Feedback component is rendered
  expect(screen.getByText(FeedbackString)).toBeDefined();

  // Wait for fetchReservation to be called
  await waitFor(() => expect(axiosMock.get).toHaveBeenCalledTimes(1));
  expect(axiosMock.get).toHaveBeenCalledWith('/api/v1/reservations/mockid');

  // Confirm the Feedback component is rendered
  await waitFor(() => expect(screen.getByText(FeedbackString)).toBeDefined());
});

test('renders WaitDetails initially, then Feedback', async () => {
  const mockLocation = {
    pathname: '/reservations/mockid',
    state: {
      reservation: mockUnresolvedReservation,
    },
  };
  axiosMock.get.mockResolvedValue({
    data: mockResolvedReservation,
  });
  render(
    <Router>
      <Reservation location={mockLocation} />
    </Router>
  );

  // Confirm the WaitDetails component is rendered
  expect(screen.getByText(WaitDetailsString)).toBeDefined();

  // Wait for fetchReservation to be called with updated resolved reservation
  await waitFor(() => expect(axiosMock.get).toHaveBeenCalledTimes(1));
  expect(axiosMock.get).toHaveBeenCalledWith('/api/v1/reservations/mockid');

  // Confirm the Feedback component is rendered
  await waitFor(() => expect(screen.getByText(FeedbackString)).toBeDefined());
});

test('renders WaitDetails once fetchReservation returns', async () => {
  // Do not include Reservation as a passed-in property
  const mockLocation = {
    pathname: '/reservations/mockid',
  };
  axiosMock.get.mockResolvedValue({
    data: mockUnresolvedReservation,
  });
  render(
    <Router>
      <Reservation location={mockLocation} />
    </Router>
  );

  // Confirm neither component is rendered
  expect(screen.queryByText(WaitDetailsString)).toBeNull();
  expect(screen.queryByText(FeedbackString)).toBeNull();

  // Wait for fetchReservation to be called with unresolved reservation
  await waitFor(() => expect(axiosMock.get).toHaveBeenCalledTimes(1));
  expect(axiosMock.get).toHaveBeenCalledWith('/api/v1/reservations/mockid');

  // Confirm the WaitDetails component is rendered
  await waitFor(() =>
    expect(screen.getByText(WaitDetailsString)).toBeDefined()
  );
});
