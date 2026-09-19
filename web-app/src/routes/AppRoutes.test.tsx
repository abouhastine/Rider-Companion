import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it } from 'vitest';
import { AppRoutes } from './AppRoutes';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
const renderRoutes = (entry: string) =>
  render(
    <QueryClientProvider
      client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}
    >
      <MemoryRouter initialEntries={[entry]}>
        <AppRoutes />
      </MemoryRouter>
    </QueryClientProvider>,
  );
describe('routes', () => {
  it('renders sign in', () => {
    renderRoutes('/sign-in');
    expect(screen.getByRole('heading', { name: 'Welcome back' })).toBeInTheDocument();
  });
  it('renders a not found state', () => {
    renderRoutes('/unknown');
    expect(screen.getByText('Page not found')).toBeInTheDocument();
  });
});
