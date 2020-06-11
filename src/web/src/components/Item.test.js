import { render, waitFor } from '@testing-library/react';
import axios from 'axios';
import React from 'react';
import { MemoryRouter } from 'react-router-dom';
import Item from './Item.jsx';

jest.mock('axios');
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => (0)
}));

it('renders an item', async () => {
  axios.get.mockResolvedValue({
    data: {
      name: 'zero'
    }
  });

  const { getByTestId } = render(
    <MemoryRouter initialEntries={["/items/0"]}>
      <Item />
    </MemoryRouter>
  );

  const e = await waitFor(() => getByTestId('item-container'))
  expect(axios.get).toHaveBeenCalledTimes(1);
});

