import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import { waitFor } from '@testing-library/dom';
import '@testing-library/jest-dom/extend-expect'
import axiosMock from 'axios'
import Cancel from './Cancel';

jest.mock('axios')
let historyAdd = '';
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    push: jest.fn((route)=>{historyAdd=route})
  })
}));

test('cancels call', async () => {
  render(<Cancel />);

  axiosMock.patch.mockResolvedValueOnce({ data: {} })

  const submit = screen.getByRole('button')
  fireEvent.submit(submit)

  expect(screen.getByRole('button')).toHaveBeenCalledTimes(1)

  await waitFor(() => expect(axiosMock.patch).toHaveBeenCalledTimes(1))
  expect(axiosMock.post).toHaveBeenCalledWith("/api/v1/reservations",
  expect.objectContaining({
    preferredName: 'mockname',
    contactPhone: '1234567890',
    query: 'mockquery',
  }))
  expect(historyAdd).toBe("/reservations/res-id");

});