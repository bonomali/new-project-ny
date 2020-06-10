import '@testing-library/jest-dom/extend-expect';
import { render } from '@testing-library/react';
import React from 'react';
import { MemoryRouter } from 'react-router-dom';
import Routes from './Routes';

test('/items/:id routes to Item', async () => {
  const { getByTestId } = render(
    <MemoryRouter initialEntries={['/items/0']}>
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
