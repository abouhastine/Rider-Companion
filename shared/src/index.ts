/** Framework-neutral Rider Companion API contracts shared by web and mobile clients. */
export type UserSummary = { id: number; firstName: string; lastName: string; email: string };
export type WebSession = { token: string; expiresAt: string; user: UserSummary };
export type MobileSession = { accessToken: string; accessTokenExpiresAt: string; refreshToken: string; refreshTokenExpiresAt: string; user: UserSummary };
export type Motorcycle = { id: number; brand: string; model: string; year: number; engineCapacity?: number; power?: number; fuelType?: string; registrationNumber?: string | null; purchaseDate?: string | null; currentMileage?: number; averageConsumption?: number | null; primaryMotorcycle: boolean; hasImage: boolean };
export type MotorcycleSummary = Pick<Motorcycle, 'id' | 'brand' | 'model'>;
export type Maintenance = { id: number; motorcycle: MotorcycleSummary; maintenanceType?: string; status: 'COMPLETED' | 'PLANNED' | 'OVERDUE'; completionDate?: string | null; plannedDate?: string | null; mileage?: number | null; plannedMileage?: number | null; cost?: number | null; serviceProvider?: string | null; notes?: string | null };
export type ChecklistItem = { id: number; label: string; checked: boolean };
export type Ride = { id: number; motorcycle: MotorcycleSummary; title?: string; plannedDate?: string; departureTime?: string; departureLocation?: string; destination?: string; estimatedDistance?: number; estimatedDuration?: number; rideType?: string; useHighway?: boolean; useTolls?: boolean; plannedBreaks?: number | null; status: 'DRAFT' | 'PLANNED' | 'COMPLETED' | 'CANCELLED'; notes?: string | null; checklistItems: ChecklistItem[] };
export type ApiError = { message: string; status?: number };

export const readableApiError = (value: unknown): ApiError => {
  if (typeof value === 'object' && value && 'message' in value) return value as ApiError;
  return { message: 'An unexpected error occurred' };
};
