import { zodResolver } from '@hookform/resolvers/zod';
import {
  Box,
  Button,
  Checkbox,
  FormControlLabel,
  Grid2 as Grid,
  Stack,
  Typography,
} from '@mui/material';
import { useForm } from 'react-hook-form';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import {
  AddButton,
  FormSelect,
  FormTextField,
  FormToggle,
  PageHeader,
  SectionCard,
  StatusChip,
} from '../../components/ui';
import { checklist, motorcycles, rides } from '../shared/mockData';
import { rideSchema, type RideValues } from '../shared/schemas';
const rideTypes = ['LEISURE_RIDE', 'COMMUTE', 'ROAD_TRIP', 'BUSINESS_TRIP', 'GROUP_RIDE', 'OTHER'];
export function RidesPage() {
  return (
    <Box className="page-content">
      <PageHeader
        eyebrow="Ride planner"
        title="Your next adventures"
        action={
          <AddButton component={RouterLink} to="/rides/new">
            Plan a ride
          </AddButton>
        }
      />
      <Grid container spacing={3}>
        {rides.map((ride) => (
          <Grid size={{ xs: 12, md: 6 }} key={ride.id}>
            <SectionCard>
              <Stack spacing={2}>
                <Stack direction="row" justifyContent="space-between">
                  <Box>
                    <Typography variant="h5">{ride.title}</Typography>
                    <Typography color="text.secondary">{ride.date}</Typography>
                  </Box>
                  <StatusChip status={ride.status} />
                </Stack>
                <Typography>
                  {ride.destination} · {ride.distance} km · {ride.type}
                </Typography>
                <Button component={RouterLink} to="/rides/new" sx={{ alignSelf: 'flex-start' }}>
                  View ride
                </Button>
              </Stack>
            </SectionCard>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
export function RideFormPage() {
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
  } = useForm<RideValues>({
    resolver: zodResolver(rideSchema),
    defaultValues: {
      title: '',
      motorcycle: `${motorcycles[0].brand} ${motorcycles[0].model}`,
      date: '',
      time: '09:00',
      departure: '',
      destination: '',
      distance: 0,
      duration: 0,
      type: 'LEISURE_RIDE',
      highway: false,
      tolls: false,
      breaks: '',
      notes: '',
    },
  });
  return (
    <Box className="page-content">
      <PageHeader eyebrow="Ride planner" title="Plan a ride" />
      <Grid container spacing={3}>
        <Grid size={{ xs: 12, lg: 8 }}>
          <SectionCard>
            <Stack component="form" onSubmit={handleSubmit(() => navigate('/rides'))} spacing={2.5}>
              <Grid container spacing={2.5}>
                <Grid size={12}>
                  <FormTextField
                    name="title"
                    label="Ride title"
                    register={register}
                    errors={errors}
                    required
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <FormSelect
                    name="motorcycle"
                    label="Motorcycle"
                    values={motorcycles.map((item) => `${item.brand} ${item.model}`)}
                    control={control}
                    errors={errors}
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <FormSelect
                    name="type"
                    label="Ride type"
                    values={rideTypes}
                    control={control}
                    errors={errors}
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <FormTextField
                    name="date"
                    label="Date"
                    type="date"
                    register={register}
                    errors={errors}
                    required
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <FormTextField
                    name="time"
                    label="Departure time"
                    type="time"
                    register={register}
                    errors={errors}
                    required
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <FormTextField
                    name="departure"
                    label="Departure location"
                    register={register}
                    errors={errors}
                    required
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <FormTextField
                    name="destination"
                    label="Destination"
                    register={register}
                    errors={errors}
                    required
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 4 }}>
                  <FormTextField
                    name="distance"
                    label="Estimated distance (km)"
                    type="number"
                    register={register}
                    errors={errors}
                    required
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 4 }}>
                  <FormTextField
                    name="duration"
                    label="Duration (minutes)"
                    type="number"
                    register={register}
                    errors={errors}
                    required
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 4 }}>
                  <FormTextField
                    name="breaks"
                    label="Planned breaks"
                    type="number"
                    register={register}
                    errors={errors}
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <FormToggle name="highway" label="Allow highways" control={control} />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <FormToggle name="tolls" label="Allow toll roads" control={control} />
                </Grid>
                <Grid size={12}>
                  <FormTextField
                    name="notes"
                    label="Notes"
                    register={register}
                    errors={errors}
                    multiline
                  />
                </Grid>
              </Grid>
              <Stack direction="row" spacing={1.5}>
                <Button type="submit" variant="contained">
                  Save ride
                </Button>
                <Button onClick={() => navigate('/rides')}>Cancel</Button>
              </Stack>
            </Stack>
          </SectionCard>
        </Grid>
        <Grid size={{ xs: 12, lg: 4 }}>
          <SectionCard title="Before you go">
            <Stack spacing={0.25}>
              {checklist.map((item) => (
                <FormControlLabel
                  key={item.label}
                  control={<Checkbox defaultChecked={item.done} />}
                  label={item.label}
                />
              ))}
            </Stack>
          </SectionCard>
        </Grid>
      </Grid>
    </Box>
  );
}
