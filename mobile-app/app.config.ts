import type { ExpoConfig } from 'expo/config';

const local = process.env.APP_ENV === 'local';

const config: ExpoConfig = {
  name: 'Rider Companion',
  slug: 'rider-companion',
  scheme: 'ridercompanion',
  version: '0.1.0',
  orientation: 'portrait',
  userInterfaceStyle: 'automatic',
  plugins: [
    'expo-router',
    ['expo-secure-store', { faceIDPermission: 'Use Face ID to unlock Rider Companion.' }],
    ['expo-local-authentication', { faceIDPermission: 'Use Face ID to unlock Rider Companion.' }],
    ['expo-image-picker', { photosPermission: 'Allow Rider Companion to select a motorcycle photo.' }],
  ],
  ios: {
    supportsTablet: true,
    bundleIdentifier: 'com.ridercompanion.beta',
    infoPlist: local ? { NSAppTransportSecurity: { NSAllowsArbitraryLoads: true } } : undefined,
  },
  android: {
    package: 'com.ridercompanion.beta',
    adaptiveIcon: { backgroundColor: '#172033' },
    usesCleartextTraffic: local || undefined,
  },
};

export default config;
