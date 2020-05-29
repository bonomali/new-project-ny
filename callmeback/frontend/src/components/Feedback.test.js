import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import { waitFor } from '@testing-library/dom';
import '@testing-library/jest-dom/extend-expect';
import axiosMock from 'axios';
import Feedback from './Feedback';

jest.mock('axios');
let historyAdd = '';
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    push: jest.fn((route) => {
      historyAdd = route;
    }),
  }),
}));

test('renders feedback form', async () => {
  render(<Feedback id='mockid' />);

  axiosMock.patch.mockResolvedValueOnce({ data: {} });

  const ratingButtons = screen.getAllByRole('radio');
  const details = screen.getByPlaceholderText('Tell us more (optional)');
  const submit = screen.getByRole('button');
  expect(submit).toHaveAttribute('disabled');

  fireEvent.change(details, {
    target: {
      value: 'mockdetails',
    },
  });
  fireEvent.click(ratingButtons[2]);
  expect(submit.getAttribute('disabled')).toBe(null);

  fireEvent.submit(submit);

  await waitFor(() => expect(axiosMock.patch).toHaveBeenCalledTimes(1));
  expect(axiosMock.patch).toHaveBeenCalledWith('/api/v1/reservations/mockid', {
    feedback: expect.objectContaining({
      rating: 3,
      comment: 'mockdetails',
    }),
  });
  expect(historyAdd).toBe('/thankyou');
});
