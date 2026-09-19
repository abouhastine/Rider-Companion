export type Status = 'COMPLETED' | 'PLANNED' | 'OVERDUE';
export interface Motorcycle {
  id: string;
  brand: string;
  model: string;
  year: number;
  mileage: number;
  engineCapacity: number;
  primary: boolean;
  image: string;
}
export interface MaintenanceRecord {
  id: string;
  type: string;
  motorcycle: string;
  date: string;
  mileage: number;
  cost: number;
  provider: string;
  status: Status;
}
export interface Ride {
  id: string;
  title: string;
  date: string;
  destination: string;
  distance: number;
  type: string;
  status: 'DRAFT' | 'PLANNED' | 'COMPLETED' | 'CANCELLED';
}
export interface ChecklistItem {
  label: string;
  done: boolean;
}
