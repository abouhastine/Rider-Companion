import { Tabs } from 'expo-router';
export default function TabsLayout(){return <Tabs screenOptions={{headerTitle:'Rider Companion'}}><Tabs.Screen name="index" options={{title:'Home'}}/><Tabs.Screen name="garage" options={{title:'Garage'}}/><Tabs.Screen name="maintenance" options={{title:'Maintenance'}}/><Tabs.Screen name="rides" options={{title:'Rides'}}/></Tabs>;}
