import { cleanup, fireEvent, render, screen } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import { afterEach, describe, expect, it } from 'vitest';
import { MotorcycleFormPage } from './GaragePages';

afterEach(cleanup);

const renderMotorcycleForm = () =>
  render(
    <QueryClientProvider
      client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}
    >
      <MemoryRouter>
        <MotorcycleFormPage />
      </MemoryRouter>
    </QueryClientProvider>,
  );

describe('MotorcycleFormPage', () => {
  it('offers the motorcycle brand catalogue in a searchable field', () => {
    renderMotorcycleForm();

    const brand = screen.getByRole('combobox', { name: 'Brand' });
    fireEvent.click(brand);
    fireEvent.keyDown(brand, { key: 'ArrowDown' });

    expect(screen.getByRole('option', { name: 'Ducati' })).toBeInTheDocument();
  });

  it('places brand and model next to each other in the two-column form', () => {
    renderMotorcycleForm();

    const brandColumn = screen.getByRole('combobox', { name: 'Brand' }).closest('.MuiGrid2-root');
    const modelColumn = screen.getByRole('textbox', { name: 'Model' }).closest('.MuiGrid2-root');

    expect(brandColumn?.nextElementSibling).toBe(modelColumn);
  });
});
