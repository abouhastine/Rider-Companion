import { z } from 'zod';
const required = (label: string) => z.string().trim().min(1, `${label} is required`);
const number = (label: string, min = 0) =>
  z.coerce
    .number({ invalid_type_error: `${label} must be a number` })
    .min(min, `${label} must be at least ${min}`);
export const signInSchema = z.object({
  email: z.string().email('Enter a valid email address'),
  password: z.string().min(8, 'Password must contain at least 8 characters'),
});
export const signUpSchema = signInSchema
  .extend({
    firstName: required('First name'),
    lastName: required('Last name'),
    confirmPassword: z.string(),
  })
  .refine((values) => values.password === values.confirmPassword, {
    path: ['confirmPassword'],
    message: 'Passwords do not match',
  });
export const profileSchema = z.object({
  firstName: required('First name'),
  lastName: required('Last name'),
  licenseType: required('License type'),
  licenseYear: number('License year', 1900).max(new Date().getFullYear()),
  experience: required('Experience level'),
  primaryUsage: required('Primary usage'),
  annualDistance: number('Annual distance'),
});
export const motorcycleSchema = z.object({
  brand: required('Brand'),
  model: required('Model'),
  year: number('Year', 1900).max(new Date().getFullYear() + 1),
  engineCapacity: number('Engine capacity', 1),
  power: number('Power', 0),
  fuelType: required('Fuel type'),
  registration: z.string(),
  purchaseDate: z.string(),
  mileage: number('Current mileage'),
  consumption: z.union([z.literal(''), number('Consumption')]),
  primary: z.boolean(),
});
export const maintenanceSchema = z.object({
  motorcycle: required('Motorcycle'),
  type: required('Maintenance type'),
  date: required('Date').refine(
    (value) => new Date(value) <= new Date(),
    'Completion date cannot be in the future',
  ),
  mileage: number('Mileage'),
  cost: number('Cost'),
  provider: z.string(),
  notes: z.string(),
  nextDueDate: z.string(),
  nextDueMileage: z.union([z.literal(''), number('Next due mileage')]),
});
export const rideSchema = z.object({
  title: required('Title'),
  motorcycle: required('Motorcycle'),
  date: required('Date'),
  time: required('Departure time'),
  departure: required('Departure location'),
  destination: required('Destination'),
  distance: number('Estimated distance', 1),
  duration: number('Estimated duration', 1),
  type: required('Ride type'),
  highway: z.boolean(),
  tolls: z.boolean(),
  breaks: z.union([z.literal(''), number('Planned breaks')]),
  notes: z.string(),
  status: z.enum(['DRAFT', 'PLANNED', 'COMPLETED', 'CANCELLED']).default('PLANNED'),
});
export type SignInValues = z.infer<typeof signInSchema>;
export type SignUpValues = z.infer<typeof signUpSchema>;
export type MotorcycleValues = z.infer<typeof motorcycleSchema>;
export type MaintenanceValues = z.infer<typeof maintenanceSchema>;
export type RideValues = z.infer<typeof rideSchema>;
export type ProfileValues = z.infer<typeof profileSchema>;
