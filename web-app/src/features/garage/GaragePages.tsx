import { zodResolver } from '@hookform/resolvers/zod';
import { Delete, Edit } from '@mui/icons-material';
import {
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Grid2 as Grid,
  Stack,
  Typography,
} from '@mui/material';
import { useForm } from 'react-hook-form';
import { useEffect, useState } from 'react';
import { Link as RouterLink, useNavigate, useParams } from 'react-router-dom';
import {
  AddButton,
  FormSelect,
  FormTextField,
  FormToggle,
  PageHeader,
  SectionCard,
  StatusChip,
} from '../../components/ui';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { meApi } from '../../services/api';
import { motorcycleSchema, type MotorcycleValues } from '../shared/schemas';
export function GaragePage() {
  const bikes = useQuery({ queryKey: ['motorcycles'], queryFn: meApi.motorcycles });
  const queryClient = useQueryClient();
  const [motorcycleToDelete, setMotorcycleToDelete] = useState<{
    id: number;
    name: string;
  } | null>(null);
  const remove = useMutation({
    mutationFn: meApi.deleteMotorcycle,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['motorcycles'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });
  const confirmDelete = () => {
    if (motorcycleToDelete)
      remove.mutate(motorcycleToDelete.id, { onSuccess: () => setMotorcycleToDelete(null) });
  };
  return (
    <Box className="page-content">
      <PageHeader
        eyebrow="Virtual garage"
        title="Your motorcycles"
        action={
          <AddButton component={RouterLink} to="/garage/new">
            Add motorcycle
          </AddButton>
        }
      />
      <Grid container spacing={3}>
        {bikes.data?.map((bike) => (
          <Grid size={{ xs: 12, md: 6 }} key={bike.id}>
            <Card sx={{ height: '100%', borderRadius: 3, overflow: 'hidden' }}>
              <MotorcycleImage bike={bike} />
              <CardContent>
                <Stack direction="row" justifyContent="space-between" alignItems="start">
                  <Box>
                    <Typography variant="h5">
                      {bike.brand} {bike.model}
                    </Typography>
                    <Typography color="text.secondary">
                      {bike.year} · {bike.currentMileage.toLocaleString()} km ·{' '}
                      {bike.engineCapacity} cc
                    </Typography>
                  </Box>
                  {bike.primaryMotorcycle && <StatusChip status="Primary" />}
                </Stack>
                <Button
                  component={RouterLink}
                  to={`/garage/${bike.id}/edit`}
                  startIcon={<Edit />}
                  sx={{ mt: 1.5 }}
                >
                  Edit motorcycle
                </Button>
                <Button
                  color="error"
                  startIcon={<Delete />}
                  onClick={() =>
                    setMotorcycleToDelete({ id: bike.id, name: `${bike.brand} ${bike.model}` })
                  }
                  disabled={remove.isPending}
                  sx={{ mt: 1.5, ml: 1 }}
                >
                  Delete
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      <Dialog
        open={Boolean(motorcycleToDelete)}
        onClose={() => !remove.isPending && setMotorcycleToDelete(null)}
      >
        <DialogTitle>Delete motorcycle?</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete {motorcycleToDelete?.name}? Related maintenance and ride
            data may also be deleted.
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2.5 }}>
          <Button onClick={() => setMotorcycleToDelete(null)} disabled={remove.isPending}>
            Cancel
          </Button>
          <Button
            color="error"
            variant="contained"
            onClick={confirmDelete}
            disabled={remove.isPending}
          >
            {remove.isPending ? 'Deleting…' : 'Delete motorcycle'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
function MotorcycleImage({ bike }: { bike: import('../../services/api').MotorcycleApi }) {
  const image = useQuery({
    queryKey: ['motorcycle-image', bike.id],
    queryFn: () => meApi.image(bike.id),
    enabled: bike.hasImage,
  });
  return (
    <CardMedia
      component="img"
      height="220"
      image={
        image.data ??
        'https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=1200&q=80'
      }
      alt={`${bike.brand} ${bike.model}`}
    />
  );
}
const defaults: MotorcycleValues = {
  brand: '',
  model: '',
  year: new Date().getFullYear(),
  engineCapacity: 0,
  power: 0,
  fuelType: 'Petrol',
  registration: '',
  purchaseDate: '',
  mileage: 0,
  consumption: '',
  primary: false,
};
export function MotorcycleFormPage() {
  const navigate = useNavigate();
  const { motorcycleId } = useParams();
  const currentQuery = useQuery({
    queryKey: ['motorcycle', motorcycleId],
    queryFn: () => meApi.motorcycle(motorcycleId!),
    enabled: Boolean(motorcycleId),
  });
  const current = currentQuery.data;
  const queryClient = useQueryClient();
  const [imageFile, setImageFile] = useState<File | null>(null);
  const save = useMutation({
    mutationFn: async (values: MotorcycleValues) => {
      const body = {
        user: 0,
        brand: values.brand,
        model: values.model,
        year: values.year,
        engineCapacity: values.engineCapacity,
        power: values.power,
        fuelType: values.fuelType,
        registrationNumber: values.registration,
        purchaseDate: values.purchaseDate || null,
        currentMileage: values.mileage,
        averageConsumption: values.consumption === '' ? null : values.consumption,
        primaryMotorcycle: values.primary,
      };
      const motorcycle = motorcycleId
        ? meApi.updateMotorcycle(motorcycleId, body)
        : meApi.createMotorcycle(body);
      const saved = await motorcycle;
      if (imageFile) await meApi.uploadImage(saved.id, imageFile);
      return saved;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['motorcycles'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      navigate('/garage');
    },
  });
  const {
    register,
    handleSubmit,
    control,
    reset,
    formState: { errors },
  } = useForm<MotorcycleValues>({
    resolver: zodResolver(motorcycleSchema),
    defaultValues: current
      ? {
          ...defaults,
          brand: current.brand,
          model: current.model,
          year: current.year,
          engineCapacity: current.engineCapacity,
          mileage: current.currentMileage,
          primary: current.primaryMotorcycle,
        }
      : defaults,
  });
  useEffect(() => {
    if (!current) return;
    reset({
      ...defaults,
      brand: current.brand,
      model: current.model,
      year: current.year,
      engineCapacity: current.engineCapacity,
      power: current.power,
      fuelType: current.fuelType,
      registration: current.registrationNumber ?? '',
      purchaseDate: current.purchaseDate ?? '',
      mileage: current.currentMileage,
      consumption: current.averageConsumption ?? '',
      primary: current.primaryMotorcycle,
    });
  }, [current, reset]);
  const title = current ? `Edit ${current.brand} ${current.model}` : 'Add a motorcycle';
  return (
    <Box className="page-content">
      <PageHeader eyebrow="Virtual garage" title={title} />
      <SectionCard>
        <Stack
          component="form"
          spacing={2.5}
          onSubmit={handleSubmit((values) => save.mutate(values))}
        >
          <Grid container spacing={2.5}>
            <Grid size={{ xs: 12, sm: 6 }}>
              <FormTextField
                name="brand"
                label="Brand"
                register={register}
                errors={errors}
                required
              />
            </Grid>
            <Grid size={12}>
              <Button component="label" variant="outlined">
                {imageFile ? `Selected: ${imageFile.name}` : 'Upload motorcycle image (optional)'}
                <input
                  hidden
                  type="file"
                  accept="image/jpeg,image/png,image/webp"
                  onChange={(event) => setImageFile(event.target.files?.[0] ?? null)}
                />
              </Button>
              <Typography variant="body2" color="text.secondary" mt={0.75}>
                JPEG, PNG, or WebP, up to 5 MB. Selecting a file replaces the current image.
              </Typography>
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <FormTextField
                name="model"
                label="Model"
                register={register}
                errors={errors}
                required
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 4 }}>
              <FormTextField
                name="year"
                label="Year"
                type="number"
                register={register}
                errors={errors}
                required
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 4 }}>
              <FormTextField
                name="engineCapacity"
                label="Engine capacity (cc)"
                type="number"
                register={register}
                errors={errors}
                required
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 4 }}>
              <FormTextField
                name="power"
                label="Power (hp)"
                type="number"
                register={register}
                errors={errors}
                required
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <FormSelect
                name="fuelType"
                label="Fuel type"
                values={['Petrol', 'Electric', 'Hybrid']}
                control={control}
                errors={errors}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <FormTextField
                name="mileage"
                label="Current mileage (km)"
                type="number"
                register={register}
                errors={errors}
                required
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <FormTextField
                name="registration"
                label="Registration number (optional)"
                register={register}
                errors={errors}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <FormTextField
                name="purchaseDate"
                label="Purchase date (optional)"
                type="date"
                register={register}
                errors={errors}
              />
            </Grid>
            <Grid size={12}>
              <FormToggle name="primary" label="Set as primary motorcycle" control={control} />
            </Grid>
          </Grid>
          <Stack direction="row" spacing={1.5}>
            <Button type="submit" variant="contained" disabled={save.isPending}>
              Save motorcycle
            </Button>
            <Button onClick={() => navigate('/garage')}>Cancel</Button>
          </Stack>
          {save.error && (
            <Typography color="error">
              {(save.error as { message?: string }).message ?? 'Unable to save motorcycle.'}
            </Typography>
          )}
        </Stack>
      </SectionCard>
    </Box>
  );
}
