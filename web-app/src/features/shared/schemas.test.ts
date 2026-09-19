import { describe, expect, it } from 'vitest';
import { motorcycleSchema, rideSchema, signInSchema } from './schemas';
describe('form schemas', () => {
  it('rejects invalid sign-in details', () =>
    expect(signInSchema.safeParse({ email: 'wrong', password: '123' }).success).toBe(false));
  it('accepts a valid motorcycle', () =>
    expect(
      motorcycleSchema.safeParse({
        brand: 'Yamaha',
        model: 'MT-07',
        year: 2024,
        engineCapacity: 689,
        power: 74,
        fuelType: 'Petrol',
        registration: '',
        purchaseDate: '',
        mileage: 0,
        consumption: '',
        primary: false,
      }).success,
    ).toBe(true));
  it('requires a valid distance for a ride', () =>
    expect(
      rideSchema.safeParse({
        title: 'Loop',
        motorcycle: 'Yamaha',
        date: '2026-08-20',
        time: '09:00',
        departure: 'Paris',
        destination: 'Rouen',
        distance: 0,
        duration: 50,
        type: 'LEISURE_RIDE',
        highway: false,
        tolls: false,
        breaks: '',
        notes: '',
      }).success,
    ).toBe(false));
});
