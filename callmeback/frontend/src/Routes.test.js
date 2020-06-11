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
import { render } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Routes from './Routes';
import { MemoryRouter } from 'react-router-dom';

test('/home routes to Home', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/home']}>
      <Routes />
    </MemoryRouter>
  );
  expect(getByTestId('home-container')).toBeDefined();
});

test('/thankyou routes to ThankYou', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/thankyou']}>
      <Routes />
    </MemoryRouter>
  );
  expect(getByTestId('thankyou-container')).toBeDefined();
});

test('/cancel routes to Cancel', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/cancel']}>
      <Routes />
    </MemoryRouter>
  );
  expect(getByTestId('cancel-container')).toBeDefined();
});

test('/cancelconfirmation routes to CancelConfirmation', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/cancelconfirmation']}>
      <Routes />
    </MemoryRouter>
  );
  expect(getByTestId('cancelconfirmation-container')).toBeDefined();
});

test('/reservations/:id routes to Reservation', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/reservations/mockid']}>
      <Routes />
    </MemoryRouter>
  );
  expect(getByTestId('reservation-container')).toBeDefined();
});

test('/reservations-ma/:id routes to Reservation', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/reservations/mockid']}>
      <Routes />
    </MemoryRouter>
  );
  expect(getByTestId('reservation-container')).toBeDefined();
});

test('/ routes to Home', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/']}>
      <Routes />
    </MemoryRouter>
  );
  expect(getByTestId('home-container')).toBeDefined();
});
