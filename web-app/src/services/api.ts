import { apiClient } from './apiClient';
export type Session = {
  token: string;
  expiresAt: string;
  user: { id: number; firstName: string; lastName: string; email: string };
};
export type MotorcycleApi = {
  id: number;
  brand: string;
  model: string;
  year: number;
  engineCapacity: number;
  power: number;
  fuelType: string;
  registrationNumber: string;
  purchaseDate: string;
  currentMileage: number;
  averageConsumption: number | null;
  primaryMotorcycle: boolean;
  hasImage: boolean;
};
export const authApi = {
  signUp: async (body: object) => (await apiClient.post<Session>('/api/sign-in', body)).data,
  login: async (body: object) => (await apiClient.post<Session>('/api/login', body)).data,
  logout: () => apiClient.post('/api/logout'),
};
export const meApi = {
  profile: () => apiClient.get('/api/me/profile').then((r) => r.data),
  updateProfile: (body: object) => apiClient.put('/api/me/profile', body).then((r) => r.data),
  dashboard: () => apiClient.get('/api/me/dashboard').then((r) => r.data),
  motorcycles: () => apiClient.get<MotorcycleApi[]>('/api/me/motorcycles').then((r) => r.data),
  motorcycle: (id: string) =>
    apiClient.get<MotorcycleApi>(`/api/me/motorcycles/${id}`).then((r) => r.data),
  createMotorcycle: (body: object) =>
    apiClient.post<MotorcycleApi>('/api/me/motorcycles', body).then((r) => r.data),
  updateMotorcycle: (id: string, body: object) =>
    apiClient.put<MotorcycleApi>(`/api/me/motorcycles/${id}`, body).then((r) => r.data),
  deleteMotorcycle: (id: number) => apiClient.delete(`/api/me/motorcycles/${id}`),
  uploadImage: (id: number, file: File) => {
    const data = new FormData();
    data.append('file', file);
    return apiClient.put(`/api/me/motorcycles/${id}/image`, data);
  },
  image: (id: number) =>
    apiClient
      .get(`/api/me/motorcycles/${id}/image`, { responseType: 'blob' })
      .then((r) => URL.createObjectURL(r.data)),
};
