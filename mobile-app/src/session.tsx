import * as LocalAuthentication from 'expo-local-authentication';
import * as SecureStore from 'expo-secure-store';
import { createContext, useContext, useEffect, useState } from 'react';
import type { MobileSession } from '../../shared/src';
import { mobileAuthApi, setAccessToken } from './api';

const KEY = 'rider-companion.mobile-session';
type State = { session: MobileSession | null; ready: boolean; setSession: (value:MobileSession) => Promise<void>; signOut: () => Promise<void> };
const SessionContext = createContext<State | null>(null);
export function SessionProvider({children}:{children:React.ReactNode}) {
  const [session, set] = useState<MobileSession | null>(null); const [ready,setReady]=useState(false);
  useEffect(() => { (async () => { try { const stored=await SecureStore.getItemAsync(KEY); if (!stored) return; const can=await LocalAuthentication.hasHardwareAsync() && await LocalAuthentication.isEnrolledAsync(); if (!can) return; const result=await LocalAuthentication.authenticateAsync({promptMessage:'Unlock Rider Companion', disableDeviceFallback:false}); if (!result.success) return; const parsed=JSON.parse(stored) as MobileSession; const refreshed=await mobileAuthApi.refresh(parsed.refreshToken); await save(refreshed); } finally { setReady(true); } })(); }, []);
  const save=async(value:MobileSession)=>{ await SecureStore.setItemAsync(KEY,JSON.stringify(value)); setAccessToken(value.accessToken); set(value); };
  const signOut=async()=>{ const current=session; set(null); setAccessToken(null); await SecureStore.deleteItemAsync(KEY); if (current) try { await mobileAuthApi.logout(current.refreshToken); } catch { /* local sign-out remains successful */ } };
  return <SessionContext.Provider value={{session,ready,setSession:save,signOut}}>{children}</SessionContext.Provider>;
}
export const useSession=()=>{const value=useContext(SessionContext); if(!value) throw new Error('SessionProvider missing'); return value;};
