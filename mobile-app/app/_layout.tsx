import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Stack } from 'expo-router';
import { SessionProvider } from '../src/session';
const client = new QueryClient({ defaultOptions: { queries: { retry: 1 } } });
export default function RootLayout() { return <QueryClientProvider client={client}><SessionProvider><Stack screenOptions={{headerShown:false}} /></SessionProvider></QueryClientProvider>; }
