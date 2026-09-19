import type { ChecklistItem, MaintenanceRecord, Motorcycle, Ride } from '../../types/domain';
export const motorcycles: Motorcycle[] = [
  {
    id: 'tracer-9',
    brand: 'Yamaha',
    model: 'Tracer 9 GT',
    year: 2023,
    mileage: 12480,
    engineCapacity: 890,
    primary: true,
    image:
      'https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=1200&q=80',
  },
  {
    id: 'royal-enfield',
    brand: 'Royal Enfield',
    model: 'Himalayan',
    year: 2021,
    mileage: 8430,
    engineCapacity: 411,
    primary: false,
    image:
      'https://images.unsplash.com/photo-1529422643029-d4585747aaf2?auto=format&fit=crop&w=1200&q=80',
  },
];
export const maintenanceRecords: MaintenanceRecord[] = [
  {
    id: 'm1',
    type: 'Oil change',
    motorcycle: 'Yamaha Tracer 9 GT',
    date: '2026-07-22',
    mileage: 12000,
    cost: 119,
    provider: 'Moto Service Paris',
    status: 'COMPLETED',
  },
  {
    id: 'm2',
    type: 'Chain tension',
    motorcycle: 'Yamaha Tracer 9 GT',
    date: '2026-08-30',
    mileage: 13000,
    cost: 0,
    provider: 'Home garage',
    status: 'PLANNED',
  },
  {
    id: 'm3',
    type: 'Brake fluid',
    motorcycle: 'Royal Enfield Himalayan',
    date: '2026-07-12',
    mileage: 8100,
    cost: 95,
    provider: 'Road & Trail',
    status: 'OVERDUE',
  },
];
export const rides: Ride[] = [
  {
    id: 'r1',
    title: 'Vexin loop',
    date: '2026-08-23',
    destination: 'La Roche-Guyon',
    distance: 142,
    type: 'Leisure ride',
    status: 'PLANNED',
  },
  {
    id: 'r2',
    title: 'Alpine weekend',
    date: '2026-09-12',
    destination: 'Annecy',
    distance: 460,
    type: 'Road trip',
    status: 'DRAFT',
  },
];
export const checklist: ChecklistItem[] = [
  'Check tire pressure',
  'Check fuel level',
  'Check the chain',
  'Check lights',
  'Check the weather',
  'Take vehicle documents',
  'Take the phone',
  'Bring water',
  'Check riding gear',
].map((label, index) => ({ label, done: index < 3 }));
