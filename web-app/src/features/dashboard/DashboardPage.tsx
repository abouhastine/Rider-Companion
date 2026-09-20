import { Build, DirectionsBike, Map, TrendingUp, Speed, Verified } from '@mui/icons-material';
import { Box, Button, Grid2 as Grid, Stack, Typography } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { AddButton, PageHeader, SectionCard, StatusChip } from '../../components/ui';
import { meApi } from '../../services/api';
export function DashboardPage() {
  const dashboard = useQuery({ queryKey: ['dashboard'], queryFn: meApi.dashboard });
  const data = dashboard.data;
  const bike = data?.primaryMotorcycle;
  const primaryImage = useQuery({
    queryKey: ['motorcycle-image', bike?.id],
    queryFn: () => meApi.image(bike!.id),
    enabled: Boolean(bike?.hasImage),
  });
  const stats = data
    ? [
        { label: 'Motorcycles', value: data.motorcyclesCount, icon: <DirectionsBike />, to: '/garage' },
        {
          label: 'Maintenance spend',
          value: `€${data.statistics.maintenanceTotalCost}`,
          icon: <Build />,
          to: '/maintenance',
        },
        { label: 'Planned rides', value: data.statistics.plannedRidesCount, icon: <Map />, to: '/rides' },
        {
          label: 'Estimated distance',
          value: `${data.statistics.estimatedRideDistance} km`,
          icon: <TrendingUp />,
          to: '/rides',
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
            <Box component={RouterLink} to={stat.to} sx={{ color: 'inherit', display: 'block', textDecoration: 'none' }}>
              <SectionCard sx={{ borderColor: '#40506f' }}>
                <Stack direction="row" justifyContent="space-between" alignItems="start">
                  <Box>
                    <Typography color="text.secondary" variant="body2">
                      {stat.label}
                    </Typography>
                    <Typography variant="h5" mt={0.5}>
                      {stat.value}
                    </Typography>
                  </Box>
                  <Box color="primary.main" sx={{ p: 1, border: '1px solid #6a3a2e', borderRadius: 1 }}>{stat.icon}</Box>
                </Stack>
              </SectionCard>
            </Box>
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
                  bgcolor: '#0b172a',
                  color: 'white',
                  backgroundImage: `linear-gradient(90deg, rgba(8,18,36,.94), rgba(8,18,36,.3)), url(${primaryImage.data ?? 'https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=1400&q=80'})`,
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                }}
              >
                <Stack sx={{ height: '100%', minHeight: 270, p: 3.5 }} justifyContent="flex-end">
                  <Typography variant="overline" color="primary.light">Selected vehicle</Typography>
                  <Typography variant="h4">
                    {bike.brand} {bike.model}
                  </Typography>
                  <Typography sx={{ opacity: 0.8 }}>
                    {bike.year} · {bike.currentMileage?.toLocaleString()} km
                  </Typography>
                  <Stack direction="row" spacing={1.5} mt={2}><Box sx={{ border: '1px solid #40506f', bgcolor: 'rgba(8,18,36,.7)', px: 1.5, py: 1, borderRadius: 1 }}><Typography variant="overline">Odometer</Typography><Typography><Speed fontSize="small" /> {bike.currentMileage?.toLocaleString()} km</Typography></Box><Box sx={{ border: '1px solid #40506f', bgcolor: 'rgba(8,18,36,.7)', px: 1.5, py: 1, borderRadius: 1 }}><Typography variant="overline">Status</Typography><Typography color="secondary.main"><Verified fontSize="small" /> Ready</Typography></Box></Stack>
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
