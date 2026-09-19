import axios, { AxiosError } from 'axios';
export interface ApiError {
  message: string;
  status?: number;
}
export const apiClient = axios.create({
  baseURL:
    (import.meta as ImportMeta & { env?: Record<string, string> }).env?.VITE_API_BASE_URL ??
    'http://localhost:8081',
  timeout: 10_000,
});
apiClient.interceptors.request.use((config) => {
  const token = sessionStorage.getItem('rider-companion.token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<{ message?: string }>) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      sessionStorage.removeItem('rider-companion.session');
      sessionStorage.removeItem('rider-companion.token');
      if (!window.location.pathname.startsWith('/sign-in')) window.location.assign('/sign-in?reason=session');
    }
    return Promise.reject({
      message: error.response?.data?.message ?? error.message ?? 'An unexpected error occurred',
      status: error.response?.status,
    } satisfies ApiError);
  },
);
