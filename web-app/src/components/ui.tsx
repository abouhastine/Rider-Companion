import type { ComponentProps, ElementType, ReactNode } from 'react';
import { Add, CheckCircle, DirectionsBike, ErrorOutline } from '@mui/icons-material';
import {
  Alert,
  Box,
  Button,
  Chip,
  FormControl,
  FormControlLabel,
  FormHelperText,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Stack,
  Switch,
  TextField,
  Typography,
} from '@mui/material';
import type { Control, FieldErrors, FieldValues, Path, UseFormRegister } from 'react-hook-form';
import { Controller } from 'react-hook-form';

export function PageHeader({
  eyebrow,
  title,
  action,
}: {
  eyebrow?: string;
  title: string;
  action?: ReactNode;
}) {
  return (
    <Stack
      direction={{ xs: 'column', sm: 'row' }}
      justifyContent="space-between"
      alignItems={{ sm: 'center' }}
      spacing={2}
      mb={4}
    >
      <Box>
        {eyebrow && (
          <Typography color="primary" fontWeight={800} variant="overline">
            {eyebrow}
          </Typography>
        )}
        <Typography variant="h3" fontSize={{ xs: '2rem', md: '2.5rem' }}>
          {title}
        </Typography>
      </Box>
      {action}
    </Stack>
  );
}
export function SectionCard({
  title,
  action,
  children,
  sx,
}: {
  title?: string;
  action?: ReactNode;
  children: ReactNode;
  sx?: object;
}) {
  return (
    <Paper sx={{ p: { xs: 2, md: 3 }, borderRadius: 3, ...sx }}>
      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        mb={title ? 2.5 : 0}
      >
        {title && (
          <Typography variant="h6" fontWeight={750}>
            {title}
          </Typography>
        )}
        {action}
      </Stack>
      {children}
    </Paper>
  );
}
export function StatusChip({ status }: { status: string }) {
  const color =
    status === 'COMPLETED'
      ? 'success'
      : status === 'OVERDUE'
        ? 'error'
        : status === 'CANCELLED'
          ? 'default'
          : 'warning';
  return (
    <Chip
      size="small"
      icon={
        status === 'COMPLETED' ? (
          <CheckCircle />
        ) : status === 'OVERDUE' ? (
          <ErrorOutline />
        ) : undefined
      }
      label={status.replace('_', ' ')}
      color={color}
      sx={{ fontWeight: 800, textTransform: 'capitalize' }}
    />
  );
}
export function EmptyState({
  title,
  description,
  action,
}: {
  title: string;
  description: string;
  action?: ReactNode;
}) {
  return (
    <Paper sx={{ py: 7, px: 3, textAlign: 'center', borderStyle: 'dashed', borderRadius: 3 }}>
      <DirectionsBike color="primary" sx={{ fontSize: 42, mb: 1 }} />
      <Typography variant="h6">{title}</Typography>
      <Typography color="text.secondary" sx={{ maxWidth: 400, mx: 'auto', mt: 1, mb: 2.5 }}>
        {description}
      </Typography>
      {action}
    </Paper>
  );
}
type AddButtonProps = ComponentProps<typeof Button> & { component?: ElementType; to?: string };
export function AddButton({ children, ...props }: AddButtonProps) {
  return (
    <Button variant="contained" startIcon={<Add />} {...(props as ComponentProps<typeof Button>)}>
      {children}
    </Button>
  );
}
type FormProps<T extends FieldValues> = {
  name: Path<T>;
  label: string;
  register: UseFormRegister<T>;
  errors: FieldErrors<T>;
  type?: string;
  required?: boolean;
  multiline?: boolean;
};
export function FormTextField<T extends FieldValues>({
  name,
  label,
  register,
  errors,
  type,
  required,
  multiline,
}: FormProps<T>) {
  const error = errors[name];
  return (
    <TextField
      fullWidth
      label={label}
      type={type}
      required={required}
      multiline={multiline}
      minRows={multiline ? 3 : undefined}
      {...register(name, type === 'number' ? { valueAsNumber: true } : undefined)}
      error={Boolean(error)}
      helperText={error?.message as string | undefined}
    />
  );
}
export function FormSelect<T extends FieldValues>({
  name,
  label,
  values,
  control,
  errors,
}: {
  name: Path<T>;
  label: string;
  values: Array<string | { value: string; label: string }>;
  control: Control<T>;
  errors: FieldErrors<T>;
}) {
  const error = errors[name];
  return (
    <FormControl fullWidth error={Boolean(error)}>
      <InputLabel>{label}</InputLabel>
      <Controller
        control={control}
        name={name}
        render={({ field }) => (
          <Select {...field} label={label}>
            {values.map((option) => {
              const value = typeof option === 'string' ? option : option.value;
              const label = typeof option === 'string' ? option.replace(/_/g, ' ') : option.label;
              return <MenuItem value={value} key={value}>{label}</MenuItem>;
            })}
          </Select>
        )}
      />
      {error && <FormHelperText>{error.message as string}</FormHelperText>}
    </FormControl>
  );
}
export function FormToggle<T extends FieldValues>({
  name,
  label,
  control,
}: {
  name: Path<T>;
  label: string;
  control: Control<T>;
}) {
  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => (
        <FormControlLabel
          control={<Switch checked={Boolean(field.value)} onChange={field.onChange} />}
          label={label}
        />
      )}
    />
  );
}
export function FormNotice({ children }: { children: ReactNode }) {
  return (
    <Alert severity="info" sx={{ borderRadius: 2 }}>
      {children}
    </Alert>
  );
}
