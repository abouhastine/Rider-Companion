import { cleanup, render, screen } from '@testing-library/react';
import { afterEach, describe, expect, it } from 'vitest';
import { MemoryRouter, Outlet, Route, Routes } from 'react-router-dom';
import { AppShell } from './AppShell';
import { session } from '../services/session';

afterEach(() => {
  cleanup();
  session.clear();
});

describe('AppShell', () => {
  it('does not show a start ride action in the navigation', () => {
    session.set({
      token: 'test-token',
      expiresAt: '2030-01-01T00:00:00Z',
      user: { id: 1, firstName: 'Rider', lastName: 'Test', email: 'rider@example.com' },
    });

    render(
      <MemoryRouter initialEntries={['/dashboard']}>
        <Routes>
          <Route element={<AppShell />}>
            <Route path="/dashboard" element={<Outlet />} />
          </Route>
        </Routes>
      </MemoryRouter>,
    );

    expect(screen.queryByRole('link', { name: 'Start ride' })).not.toBeInTheDocument();
  });
});
