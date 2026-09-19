import { zodResolver } from '@hookform/resolvers/zod';
import { Box, Button, Grid2 as Grid, Stack, Typography } from '@mui/material';
import { useForm } from 'react-hook-form';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import {
  AddButton,
  FormSelect,
  FormTextField,
  PageHeader,
  SectionCard,
  StatusChip,
} from '../../components/ui';
import { maintenanceRecords, motorcycles } from '../shared/mockData';
import { maintenanceSchema, type MaintenanceValues } from '../shared/schemas';
const types = [
  'OIL_CHANGE',
  'OIL_FILTER',
  'AIR_FILTER',
  'CHAIN_KIT',
  'CHAIN_LUBRICATION',
  'CHAIN_TENSION',
  'TIRES',
  'BRAKE_PADS',
  'BRAKE_FLUID',
  'COOLANT',
  'SPARK_PLUGS',
  'BATTERY',
  'GENERAL_SERVICE',
  'OTHER',
];
export function MaintenancePage() {
  return (
    <Box className="page-content">
      <PageHeader
        eyebrow="Maintenance logbook"
        title="Keep it running smoothly"
        action={
          <AddButton component={RouterLink} to="/maintenance/new">
            Add maintenance
          </AddButton>
        }
      />
      <SectionCard>
        <Stack spacing={0.5}>
          {maintenanceRecords.map((record) => (
            <Grid
              container
              alignItems="center"
              spacing={2}
              key={record.id}
              sx={{ py: 2, borderBottom: '1px solid #edf0ec' }}
            >
              <Grid size={{ xs: 8, md: 3 }}>
                <Typography fontWeight={750}>{record.type}</Typography>
                <Typography variant="body2" color="text.secondary">
                  {record.motorcycle}
                </Typography>
              </Grid>
              <Grid size={{ xs: 4, md: 2 }}>
                <StatusChip status={record.status} />
              </Grid>
              <Grid size={{ xs: 6, md: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Date
                </Typography>
                <Typography>{record.date}</Typography>
              </Grid>
              <Grid size={{ xs: 6, md: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Mileage
                </Typography>
                <Typography>{record.mileage.toLocaleString()} km</Typography>
              </Grid>
              <Grid size={{ xs: 6, md: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Cost
                </Typography>
                <Typography>€{record.cost}</Typography>
              </Grid>
              <Grid size={{ xs: 6, md: 1 }}>
                <Typography variant="body2" color="text.secondary">
                  Provider
                </Typography>
                <Typography noWrap>{record.provider}</Typography>
              </Grid>
            </Grid>
          ))}
        </Stack>
      </SectionCard>
    </Box>
  );
}
export function MaintenanceFormPage() {
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
  } = useForm<MaintenanceValues>({
    resolver: zodResolver(maintenanceSchema),
    defaultValues: {
      motorcycle: motorcycles[0].brand + ' ' + motorcycles[0].model,
      type: 'OIL_CHANGE',
      date: '',
      mileage: motorcycles[0].mileage,
      cost: 0,
      provider: '',
      notes: '',
      nextDueDate: '',
      nextDueMileage: '',
    },
  });
  return (
    <Box className="page-content">
      <PageHeader eyebrow="Maintenance logbook" title="Add maintenance record" />
      <SectionCard>
        <Stack
          component="form"
          onSubmit={handleSubmit(() => navigate('/maintenance'))}
          spacing={2.5}
        >
          <Grid container spacing={2.5}>
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
                label="Maintenance type"
                values={types}
                control={control}
                errors={errors}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 4 }}>
              <FormTextField
                name="date"
                label="Completion date"
                type="date"
                register={register}
                errors={errors}
                required
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 4 }}>
              <FormTextField
                name="mileage"
                label="Mileage (km)"
                type="number"
                register={register}
                errors={errors}
                required
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 4 }}>
              <FormTextField
                name="cost"
                label="Cost (€)"
                type="number"
                register={register}
                errors={errors}
                required
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <FormTextField
                name="provider"
                label="Service provider"
                register={register}
                errors={errors}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <FormTextField
                name="nextDueDate"
                label="Next due date"
                type="date"
                register={register}
                errors={errors}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <FormTextField
                name="nextDueMileage"
                label="Next due mileage"
                type="number"
                register={register}
                errors={errors}
              />
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
              Save maintenance
            </Button>
            <Button onClick={() => navigate('/maintenance')}>Cancel</Button>
          </Stack>
        </Stack>
      </SectionCard>
    </Box>
  );
}
