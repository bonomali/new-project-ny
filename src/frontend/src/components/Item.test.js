import React from 'react';
import { render, screen } from '@testing-library/react';
import { waitFor } from '@testing-library/dom';
import '@testing-library/jest-dom/extend-expect';
import axiosMock from 'axios';
import routeData from 'react-router';
import { BrowserRouter as Router } from 'react-router-dom';

jest.mock('axios');

const mockParams = {
  id: 0,
};

beforeAll(() => {
  jest.useFakeTimers();
});

beforeEach(() => {
  jest.resetAllMocks();
  jest.spyOn(routeData, 'useParams').mockReturnValue(mockParams);
});

test('renders Feedback for resolved reservation', async () => {
  const mockLocation = {
    pathname: '/items/0',
    state: {
      item: { name: 'zero' },
    },
  };

  axiosMock.get.mockResolvedValue({
    data: { name: 'zero' },
  });

  render(
    <Router>
      <Item location={mockLocation} />
    </Router>
  );

  // Wait for fetchReservation to be called
  await waitFor(() => expect(axiosMock.get).toHaveBeenCalledTimes(1));
  expect(axiosMock.get).toHaveBeenCalledWith('/api/v1/reservations/0');
});
