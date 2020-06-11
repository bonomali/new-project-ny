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
import Cancel from './Cancel';
import { BrowserRouter as Router } from 'react-router-dom';
import routeData from 'react-router';

// TODO: Check that the "keep my call" user story sends the user back to the
// WaitDetails page (either the standard page or the moving average page)

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

test('cancels call', async () => {
  const mockLocation = {
    pathname: '/cancel',
    state: { id: 'id' },
  };
  jest.spyOn(routeData, 'useLocation').mockReturnValue(mockLocation);
  render(
    <Router>
      <Cancel />
    </Router>
  );

  axiosMock.patch.mockResolvedValueOnce({ data: {} });

  const submit = screen.getByRole('button');
  fireEvent.submit(submit);

  await waitFor(() => expect(axiosMock.patch).toHaveBeenCalledTimes(1));
  expect(axiosMock.patch).toHaveBeenCalledWith('/api/v1/reservations/id', {
    resolution: expect.objectContaining({ type: 'CANCELED' }),
  });
  expect(historyAdd).toBe('/cancelconfirmation');
});
