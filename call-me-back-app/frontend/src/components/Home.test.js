import React from 'react';
import { render } from '@testing-library/react';
import Home from './Home';

test('renders welcome link', () => {
  const { getByText } = render(<Home />);
  const linkElement = getByText(/Call-Me-Back/i);
  expect(linkElement).toBeInTheDocument();
});
