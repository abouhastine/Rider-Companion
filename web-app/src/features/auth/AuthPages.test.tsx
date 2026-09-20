import { cleanup, fireEvent, render, screen } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { afterEach, describe, expect, it, vi } from 'vitest';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { ProfilePage } from './AuthPages';
import { meApi } from '../../services/api';

afterEach(() => {
  cleanup();
  vi.restoreAllMocks();
});

describe('ProfilePage', () => {
  it('redirects to the dashboard after a successful profile save', async () => {
    vi.spyOn(meApi, 'profile').mockResolvedValue({
      firstName: 'Alex',
      lastName: 'Martin',
      licenseType: 'A',
      licenseYear: 2018,
      experienceLevel: 'INTERMEDIATE',
      primaryUsage: 'LEISURE',
      estimatedAnnualDistance: 7000,
    });
    vi.spyOn(meApi, 'updateProfile').mockResolvedValue({});

    render(
      <QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}>
        <MemoryRouter initialEntries={['/profile']}>
          <Routes>
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/dashboard" element={<div>Dashboard destination</div>} />
          </Routes>
        </MemoryRouter>
      </QueryClientProvider>,
    );

    await screen.findByDisplayValue('Alex');
    fireEvent.click(screen.getByRole('button', { name: 'Save changes' }));

    expect(await screen.findByText('Dashboard destination')).toBeInTheDocument();
  });
});
