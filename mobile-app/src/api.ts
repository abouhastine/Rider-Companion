import Constants from 'expo-constants';
import type { ApiError, Maintenance, MobileSession, Motorcycle, Ride } from '../../shared/src';

const baseUrl = process.env.EXPO_PUBLIC_API_BASE_URL ?? String(Constants.expoConfig?.extra?.apiBaseUrl ?? '');
if (!baseUrl.startsWith('https://')) console.warn('Mobile beta API must be configured with HTTPS.');
let accessToken: string | null = null;
export const setAccessToken = (token: string | null) => { accessToken = token; };

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const response = await fetch(`${baseUrl}${path}`, { ...options, headers: { Accept: 'application/json', ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}), ...options.headers } });
  if (!response.ok) { const body = await response.json().catch(() => ({})); throw { message: body.message ?? 'Request failed', status: response.status } satisfies ApiError; }
  return response.status === 204 ? undefined as T : response.json() as Promise<T>;
}
const json = (body: unknown): RequestInit => ({ method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });

export const mobileAuthApi = {
  login: (body: object) => request<MobileSession>('/api/mobile/auth/login', json(body)),
  register: (body: object) => request<MobileSession>('/api/mobile/auth/register', json(body)),
  refresh: (refreshToken: string) => request<MobileSession>('/api/mobile/auth/refresh', json({ refreshToken })),
  logout: (refreshToken: string) => request<void>('/api/mobile/auth/logout', json({ refreshToken })),
};
export const riderApi = {
  dashboard: () => request<any>('/api/me/dashboard'), profile: () => request<any>('/api/me/profile'), updateProfile: (body: object) => request<any>('/api/me/profile', { method: 'PUT', headers: {'Content-Type':'application/json'}, body: JSON.stringify(body) }),
  motorcycles: () => request<Motorcycle[]>('/api/me/motorcycles'), motorcycle: (id: number) => request<Motorcycle>(`/api/me/motorcycles/${id}`), createMotorcycle: (body: object) => request<Motorcycle>('/api/me/motorcycles', json(body)), updateMotorcycle: (id: number, body: object) => request<Motorcycle>(`/api/me/motorcycles/${id}`, {method:'PUT',headers:{'Content-Type':'application/json'},body:JSON.stringify(body)}), deleteMotorcycle: (id: number) => request<void>(`/api/me/motorcycles/${id}`, {method:'DELETE'}), updateMileage: (id: number, currentMileage: number) => request<Motorcycle>(`/api/me/motorcycles/${id}/mileage`, {method:'PATCH',headers:{'Content-Type':'application/json'},body:JSON.stringify({currentMileage})}), updatePrimary: (id: number, primaryMotorcycle: boolean) => request<Motorcycle>(`/api/me/motorcycles/${id}/primary`, {method:'PATCH',headers:{'Content-Type':'application/json'},body:JSON.stringify({primaryMotorcycle})}),
  imageUrl: (id: number) => `${baseUrl}/api/me/motorcycles/${id}/image`, uploadImage: (id: number, uri: string, mimeType: string) => { const form = new FormData(); form.append('file', {uri, type:mimeType, name:'motorcycle-image'} as any); return request<void>(`/api/me/motorcycles/${id}/image`, {method:'PUT',body:form}); }, deleteImage: (id: number) => request<void>(`/api/me/motorcycles/${id}/image`, {method:'DELETE'}),
  maintenance: () => request<Maintenance[]>('/api/me/maintenance-records'), maintenanceRecord: (id:number) => request<Maintenance>(`/api/me/maintenance-records/${id}`), createMaintenance: (body:object) => request<Maintenance>('/api/me/maintenance-records', json(body)), updateMaintenance: (id:number, body:object) => request<Maintenance>(`/api/me/maintenance-records/${id}`, {method:'PUT',headers:{'Content-Type':'application/json'},body:JSON.stringify(body)}), deleteMaintenance: (id:number) => request<void>(`/api/me/maintenance-records/${id}`, {method:'DELETE'}),
  rides: () => request<Ride[]>('/api/me/rides'), ride: (id:number) => request<Ride>(`/api/me/rides/${id}`), createRide: (body:object) => request<Ride>('/api/me/rides', json(body)), updateRide: (id:number,body:object) => request<Ride>(`/api/me/rides/${id}`, {method:'PUT',headers:{'Content-Type':'application/json'},body:JSON.stringify(body)}), deleteRide: (id:number) => request<void>(`/api/me/rides/${id}`, {method:'DELETE'}), updateRideStatus:(id:number,status:string) => request<Ride>(`/api/me/rides/${id}/status`, {method:'PATCH',headers:{'Content-Type':'application/json'},body:JSON.stringify({status})}), updateChecklist:(rideId:number,itemId:number,checked:boolean) => request(`/api/me/rides/${rideId}/checklist/${itemId}`, {method:'PATCH',headers:{'Content-Type':'application/json'},body:JSON.stringify({checked})}),
};
