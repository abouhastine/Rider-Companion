import { cleanup, render, screen } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { afterEach, describe, expect, it, vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import { RideFormPage } from './RidePages';
import { meApi } from '../../services/api';

afterEach(() => {
  cleanup();
  vi.restoreAllMocks();
});

describe('RideFormPage', () => {
  it('places ride title and motorcycle next to each other in the two-column form', async () => {
    vi.spyOn(meApi, 'motorcycles').mockResolvedValue([
      { id: 1, brand: 'Yamaha', model: 'MT-07', year: 2024, engineCapacity: 689, power: 73, fuelType: 'Petrol', registrationNumber: null, purchaseDate: null, currentMileage: 0, averageConsumption: null, primaryMotorcycle: true, hasImage: false },
    ]);

    render(
      <QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}>
        <MemoryRouter>
          <RideFormPage />
        </MemoryRouter>
      </QueryClientProvider>,
    );

    const titleColumn = (await screen.findByRole('textbox', { name: 'Ride title' })).closest('.MuiGrid2-root');
    const motorcycleColumn = screen.getAllByRole('combobox')[0].closest('.MuiGrid2-root');

    expect(titleColumn?.nextElementSibling).toBe(motorcycleColumn);
  });

  it('uses the full main-content width for the ride form', async () => {
    vi.spyOn(meApi, 'motorcycles').mockResolvedValue([
      { id: 1, brand: 'Yamaha', model: 'MT-07', year: 2024, engineCapacity: 689, power: 73, fuelType: 'Petrol', registrationNumber: null, purchaseDate: null, currentMileage: 0, averageConsumption: null, primaryMotorcycle: true, hasImage: false },
    ]);

    render(<QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}><MemoryRouter><RideFormPage /></MemoryRouter></QueryClientProvider>);

    const form = (await screen.findByRole('textbox', { name: 'Ride title' })).closest('form');
    expect(form?.parentElement?.parentElement?.className).not.toContain('MuiGrid2-grid-lg-8');
  });
});
