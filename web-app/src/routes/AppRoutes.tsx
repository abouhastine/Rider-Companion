import { ErrorOutline } from '@mui/icons-material';
import { Box, Button, Paper, Typography } from '@mui/material';
import { Navigate, Route, Routes, useRouteError } from 'react-router-dom';
import { EmptyState } from '../components/ui';
import { DashboardPage } from '../features/dashboard/DashboardPage';
import { GaragePage, MotorcycleFormPage } from '../features/garage/GaragePages';
import { MaintenanceFormPage, MaintenancePage } from '../features/maintenance/MaintenancePages';
import { ProfilePage, SignInPage, SignOutPage, SignUpPage } from '../features/auth/AuthPages';
import { RideFormPage, RidesPage } from '../features/rides/RidePages';
import { AppShell, AuthLayout } from '../layouts/AppShell';
export function RouteErrorBoundary() {
  const error = useRouteError();
  return (
    <Box className="page-content">
      <Paper sx={{ p: 4, textAlign: 'center' }}>
        <ErrorOutline color="error" sx={{ fontSize: 42 }} />
        <Typography variant="h4">Something went wrong</Typography>
        <Typography color="text.secondary" mt={1}>
          {error instanceof Error ? error.message : 'Please try again.'}
        </Typography>
      </Paper>
    </Box>
  );
}
function NotFoundPage() {
  return (
    <Box className="page-content">
      <EmptyState
        title="Page not found"
        description="The road you are looking for does not exist."
        action={
          <Button href="/dashboard" variant="contained">
            Back to dashboard
          </Button>
        }
      />
    </Box>
  );
}
export function AppRoutes() {
  return (
    <Routes>
      <Route element={<AuthLayout />} errorElement={<RouteErrorBoundary />}>
        <Route path="/sign-in" element={<SignInPage />} />
        <Route path="/sign-up" element={<SignUpPage />} />
      </Route>
      ·
      <Route element={<AppShell />} errorElement={<RouteErrorBoundary />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/garage" element={<GaragePage />} />
        <Route path="/garage/new" element={<MotorcycleFormPage />} />
        <Route path="/garage/:motorcycleId/edit" element={<MotorcycleFormPage />} />
        <Route path="/maintenance" element={<MaintenancePage />} />
        <Route path="/maintenance/new" element={<MaintenanceFormPage />} />
        <Route path="/rides" element={<RidesPage />} />
        <Route path="/rides/new" element={<RideFormPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/sign-out" element={<SignOutPage />} />
      </Route>
      <Route path="/" element={<Navigate to="/sign-in" replace />} />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}
