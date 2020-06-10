import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Routes from './Routes';
import { MemoryRouter } from 'react-router-dom';

test('/items/:id routes to Item', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/items/mockid']}>
      <Routes />
    </MemoryRouter>
  );
  expect(getByTestId('item-container')).toBeDefined();
});

test('/ routes to Item', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/']}>
      <Routes />
    </MemoryRouter>
  );
  expect(getByTestId('item-container')).toBeDefined();
});
