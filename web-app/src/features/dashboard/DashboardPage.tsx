import { Build, DirectionsBike, Map, TrendingUp } from '@mui/icons-material';
import { Box, Button, Grid2 as Grid, Stack, Typography } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { AddButton, PageHeader, SectionCard, StatusChip } from '../../components/ui';
import { meApi } from '../../services/api';
export function DashboardPage() {
  const dashboard = useQuery({ queryKey: ['dashboard'], queryFn: meApi.dashboard });
  const data = dashboard.data;
  const bike = data?.primaryMotorcycle;
  const stats = data
    ? [
        { label: 'Motorcycles', value: data.motorcyclesCount, icon: <DirectionsBike /> },
        {
          label: 'Maintenance spend',
          value: `€${data.statistics.maintenanceTotalCost}`,
          icon: <Build />,
        },
        { label: 'Planned rides', value: data.statistics.plannedRidesCount, icon: <Map /> },
        {
          label: 'Estimated distance',
          value: `${data.statistics.estimatedRideDistance} km`,
          icon: <TrendingUp />,
        },
      ]
    : [];
  return (
    <Box className="page-content">
      <PageHeader
        eyebrow={new Intl.DateTimeFormat(undefined, { weekday: 'long', day: 'numeric', month: 'long' }).format(new Date())}
        title={`Good morning${data?.user?.firstName ? `, ${data.user.firstName}` : ''}.`}
        action={
          <AddButton component={RouterLink} to="/rides/new">
            Plan a ride
          </AddButton>
        }
      />
      <Grid container spacing={2.25} mb={3}>
        {stats.map((stat) => (
          <Grid size={{ xs: 6, md: 3 }} key={stat.label}>
            <SectionCard>
              <Stack direction="row" justifyContent="space-between" alignItems="start">
                <Box>
                  <Typography color="text.secondary" variant="body2">
                    {stat.label}
                  </Typography>
                  <Typography variant="h5" mt={0.5}>
                    {stat.value}
                  </Typography>
                </Box>
                <Box color="primary.main">{stat.icon}</Box>
              </Stack>
            </SectionCard>
          </Grid>
        ))}
      </Grid>
      <Grid container spacing={3}>
        <Grid size={{ xs: 12, lg: 7 }}>
          {bike ? (
            <SectionCard
              title="Your primary motorcycle"
              action={
                <Button component={RouterLink} to="/garage">
                  Open garage
                </Button>
              }
            >
              <Box
                sx={{
                  minHeight: 270,
                  borderRadius: 2,
                  overflow: 'hidden',
                  position: 'relative',
                  bgcolor: 'primary.dark',
                  color: 'white',
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                }}
              >
                <Stack sx={{ height: '100%', minHeight: 270, p: 3.5 }} justifyContent="flex-end">
                  <Typography variant="h4">
                    {bike.brand} {bike.model}
                  </Typography>
                  <Typography sx={{ opacity: 0.8 }}>
                    {bike.year} · {bike.currentMileage?.toLocaleString()} km
                  </Typography>
                  <Button
                    component={RouterLink}
                    to={`/garage/${bike.id}/edit`}
                    variant="contained"
                    color="secondary"
                    sx={{ alignSelf: 'flex-start', mt: 2 }}
                  >
                    Update motorcycle
                  </Button>
                </Stack>
              </Box>
            </SectionCard>
          ) : (
            <SectionCard title="Your primary motorcycle">
              <Typography color="text.secondary">
                You have not added a motorcycle yet. Add your first motorcycle to get started.
              </Typography>
            </SectionCard>
          )}
        </Grid>
        <Grid size={{ xs: 12, lg: 5 }}>
          <SectionCard
            title="Maintenance at a glance"
            action={
              <Button component={RouterLink} to="/maintenance">
                View all
              </Button>
            }
          >
            <Stack spacing={1.7}>
              {data?.maintenance?.lastMaintenance ? (
                [data.maintenance.lastMaintenance].map((record: { type: string; date: string }) => (
                  <Stack
                    direction="row"
                    key={`${record.type}-${record.date}`}
                    justifyContent="space-between"
                    alignItems="center"
                    sx={{ py: 1, borderBottom: '1px solid #edf0ec' }}
                  >
                    <Box>
                      <Typography fontWeight={700}>{record.type}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        {record.date}
                      </Typography>
                    </Box>
                    <StatusChip status="COMPLETED" />
                  </Stack>
                ))
              ) : (
                <Typography color="text.secondary">
                  No maintenance record has been added yet.
                </Typography>
              )}
            </Stack>
          </SectionCard>
        </Grid>
      </Grid>
    </Box>
  );
}
