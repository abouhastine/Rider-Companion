import * as SecureStore from 'expo-secure-store';
import { createContext, useContext, useEffect, useState } from 'react';
import type { MobileSession } from '../../shared/src';
import { mobileAuthApi, setAccessToken } from './api';

const KEY = 'rider-companion.mobile-refresh-token';
type State = { signedIn: boolean; ready: boolean; setSession: (value:MobileSession) => Promise<void>; signOut: () => Promise<void> };
const SessionContext = createContext<State | null>(null);
export function SessionProvider({children}:{children:React.ReactNode}) {
  const [signedIn, setSignedIn] = useState(false); const [ready,setReady]=useState(false);
  useEffect(() => { (async () => { try { const refresh=await SecureStore.getItemAsync(KEY, { requireAuthentication:true, authenticationPrompt:'Unlock Rider Companion' }); if (!refresh) return; const refreshed=await mobileAuthApi.refresh(refresh); await save(refreshed); } catch { setAccessToken(null); setSignedIn(false); } finally { setReady(true); } })(); }, []);
  const save=async(value:MobileSession)=>{ await SecureStore.setItemAsync(KEY,value.refreshToken,{requireAuthentication:true,authenticationPrompt:'Unlock Rider Companion'}); setAccessToken(value.accessToken); setSignedIn(true); };
  const signOut=async()=>{ const refresh=await SecureStore.getItemAsync(KEY,{requireAuthentication:true}); setSignedIn(false); setAccessToken(null); await SecureStore.deleteItemAsync(KEY); if (refresh) try { await mobileAuthApi.logout(refresh); } catch { /* local sign-out remains successful */ } };
  return <SessionContext.Provider value={{signedIn,ready,setSession:save,signOut}}>{children}</SessionContext.Provider>;
}
export const useSession=()=>{const value=useContext(SessionContext); if(!value) throw new Error('SessionProvider missing'); return value;};
