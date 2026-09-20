import { cleanup, render, screen } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { afterEach, describe, expect, it, vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import { DashboardPage } from './DashboardPage';
import { meApi } from '../../services/api';

afterEach(() => {
  cleanup();
  vi.restoreAllMocks();
});

describe('DashboardPage', () => {
  it('links each summary card to its dedicated page', async () => {
    vi.spyOn(meApi, 'dashboard').mockResolvedValue({
      user: { firstName: 'Alex' },
      motorcyclesCount: 1,
      primaryMotorcycle: null,
      maintenance: { lastMaintenance: null },
      statistics: { maintenanceTotalCost: 120, plannedRidesCount: 2, estimatedRideDistance: 300 },
    });

    render(
      <QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}>
        <MemoryRouter>
          <DashboardPage />
        </MemoryRouter>
      </QueryClientProvider>,
    );

    expect(await screen.findByRole('link', { name: /Motorcycles/ })).toHaveAttribute('href', '/garage');
    expect(screen.getByRole('link', { name: /Maintenance spend/ })).toHaveAttribute('href', '/maintenance');
    expect(screen.getByRole('link', { name: /Planned rides/ })).toHaveAttribute('href', '/rides');
    expect(screen.getByRole('link', { name: /Estimated distance/ })).toHaveAttribute('href', '/rides');
  });
});
