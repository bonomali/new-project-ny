import React from 'react';
import { render } from '@testing-library/react';
import Home from './Home';

test('renders welcome link', () => {
  const { getByText } = render(<Home />);
  const linkElement = getByText(/Join the queue/i);
  expect(linkElement).toBeInTheDocument();
});
