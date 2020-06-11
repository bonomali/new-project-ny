import axios from 'axios';
import '@testing-library/jest-dom/extend-expect';
import { render, waitFor } from '@testing-library/react';
import React from 'react';
import { MemoryRouter } from 'react-router-dom';
import Routes from './Routes';

jest.mock('axios');
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => (0)
}));

beforeEach(() => {
  axios.get.mockResolvedValue({
    data: {
      name: 'zero'
    }
  });
});

afterEach(() => {
  jest.resetAllMocks();
});

test('/items/:id routes to Item', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/items/0']}>
      <Routes />
    </MemoryRouter>
  );
  await waitFor(() => getByTestId('item-container'));
});

test('/ routes to Item', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/']}>
      <Routes />
    </MemoryRouter>
  );
  await waitFor(() => getByTestId('item-container'));
});
