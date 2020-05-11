import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import { waitFor } from '@testing-library/dom';
import '@testing-library/jest-dom/extend-expect'
import axiosMock from 'axios'
import Home from './Home';

jest.mock('axios')
let historyAdd = '';
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    push: jest.fn((route)=>{historyAdd=route})
  })
}));

test('renders call queue form', async () => {
  render(<Home />);
  expect(screen.getByRole('button')).toHaveAttribute('disabled')

  axiosMock.post.mockResolvedValueOnce({
    data: { _links: { self: { href: 'reservations/res-id' } } },
  })

  const name = screen.getByPlaceholderText("Name")
  const number = screen.getByPlaceholderText("Phone number")
  const query = screen.getByPlaceholderText("Tell us how we can help")
  const submit = screen.getByRole('button')

  fireEvent.change(name, {
    target: {
      value: 'mockname'
    }
  })
  fireEvent.change(number, {
    target: {
      value: 'mocknumber'
    }
  })
  fireEvent.change(query, {
    target: {
      value: 'mockquery'
    }
  })
  fireEvent.submit(submit)

  await waitFor(() => expect(axiosMock.post).toHaveBeenCalledTimes(1))
  expect(axiosMock.post).toHaveBeenCalledWith("/api/v1/reservations", {
    preferredName: 'mockname',
    contactPhone: 'mocknumber',
    query: 'mockquery',
  })
  expect(historyAdd).toBe("/reservations/res-id");

});
