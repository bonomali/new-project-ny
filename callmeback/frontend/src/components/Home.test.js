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
import { render, fireEvent, screen } from '@testing-library/react';
import { waitFor } from '@testing-library/dom';
import '@testing-library/jest-dom/extend-expect';
import axiosMock from 'axios';
import Home from './Home';

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

test('renders call queue form', async () => {
  render(<Home />);
  expect(screen.getByRole('button')).toHaveAttribute('disabled');

  axiosMock.post.mockResolvedValueOnce({
    data: { _links: { self: { href: 'reservations/res-id' } } },
  });

  const name = screen.getByPlaceholderText('Name');
  const number = screen.getByPlaceholderText('Phone number');
  const query = screen.getByPlaceholderText('Tell us how we can help');
  const submit = screen.getByRole('button');

  fireEvent.change(name, {
    target: {
      value: 'mockname',
    },
  });
  fireEvent.change(query, {
    target: {
      value: 'mockquery',
    },
  });
  fireEvent.change(number, {
    target: {
      value: '1234567890',
    },
  });
  expect(screen.getByRole('button').getAttribute('disabled')).toBe(null);

  fireEvent.submit(submit);

  await waitFor(() => expect(axiosMock.post).toHaveBeenCalledTimes(1));
  expect(axiosMock.post).toHaveBeenCalledWith(
    '/api/v1/reservations',
    expect.objectContaining({
      preferredName: 'mockname',
      contactPhone: '1234567890',
      query: 'mockquery',
    })
  );
  expect(historyAdd).toBe('/reservations/res-id');
});
