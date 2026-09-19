import { useQuery } from '@tanstack/react-query';
import { router } from 'expo-router';
import { Text } from 'react-native';
import { riderApi } from '../../src/api'; import { Action, Card, ErrorMessage, Loading, Page } from '../../src/ui';
export default function Garage(){const q=useQuery({queryKey:['motorcycles'],queryFn:riderApi.motorcycles});if(q.isLoading)return <Loading/>;if(q.error)return <Page><ErrorMessage message={(q.error as any).message}/></Page>;return <Page><Action onPress={()=>router.push('/garage/edit')}>Add motorcycle</Action>{q.data!.map(m=><Card key={m.id}><Text style={{fontSize:18,fontWeight:'700'}}>{m.brand} {m.model}</Text><Text>{m.year} • {m.currentMileage ?? 0} km {m.primaryMotorcycle?'• Primary':''}</Text><Action onPress={()=>router.push(`/garage/${m.id}`)}>View motorcycle</Action></Card>)}</Page>}
