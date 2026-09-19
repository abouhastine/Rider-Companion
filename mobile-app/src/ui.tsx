import { ReactNode } from 'react';
import { ActivityIndicator, Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
export function Page({children}:{children:ReactNode}){return <ScrollView contentContainerStyle={styles.page}>{children}</ScrollView>}
export function Card({children}:{children:ReactNode}){return <View style={styles.card}>{children}</View>}
export function Action({children,onPress,danger=false}:{children:ReactNode;onPress:()=>void;danger?:boolean}){return <Pressable accessibilityRole="button" style={[styles.action,danger&&styles.danger]} onPress={onPress}><Text style={styles.actionText}>{children}</Text></Pressable>}
export function Loading(){return <View style={styles.center}><ActivityIndicator/></View>}
export function ErrorMessage({message}:{message:string}){return <Text style={styles.error}>{message}</Text>}
export const styles=StyleSheet.create({page:{padding:16,gap:12,backgroundColor:'#f7f8fa',flexGrow:1},card:{padding:16,gap:8,borderRadius:10,backgroundColor:'white',shadowOpacity:.05,shadowRadius:3},action:{padding:13,backgroundColor:'#155eef',borderRadius:8,alignItems:'center'},danger:{backgroundColor:'#b42318'},actionText:{color:'white',fontWeight:'700'},center:{flex:1,justifyContent:'center',alignItems:'center'},error:{color:'#b42318'}});
