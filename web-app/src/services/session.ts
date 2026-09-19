import type { Session } from './api';
const key = 'rider-companion.session';
export const session = {
  get: (): Session | null => {
    const value = sessionStorage.getItem(key);
    return value ? JSON.parse(value) : null;
  },
  set: (value: Session) => {
    sessionStorage.setItem(key, JSON.stringify(value));
    sessionStorage.setItem('rider-companion.token', value.token);
  },
  clear: () => {
    sessionStorage.removeItem(key);
    sessionStorage.removeItem('rider-companion.token');
  },
};
