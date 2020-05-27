import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import { waitFor } from '@testing-library/dom';
import '@testing-library/jest-dom/extend-expect'
import axiosMock from 'axios'
import Cancel from './Cancel';
import { BrowserRouter as Router } from 'react-router-dom'
import routeData from 'react-router';

jest.mock('axios')
let historyAdd = '';
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useHistory: () => ({
        push: jest.fn((route)=>{historyAdd=route})
    }),
}));

test('cancels call', async () => {
    const mockLocation = {
        pathname: '/cancel',
        state: { id: "id" },
    }
    jest.spyOn(routeData, 'useLocation').mockReturnValue(mockLocation)
    render(
        <Router>
            <Cancel />
        </Router>
    );

    axiosMock.patch.mockResolvedValueOnce({ data: {} })

    const submit = screen.getByRole('button')
    fireEvent.submit(submit)

    await waitFor(() => expect(axiosMock.patch).toHaveBeenCalledTimes(1))
    expect(axiosMock.patch).toHaveBeenCalledWith("/api/v1/reservations/id",
        {resolution:
            expect.objectContaining({type: 'CANCELED'})
        })
    expect(historyAdd).toBe("/cancelconfirmation");
});