import { Dashboard, DirectionsBike, Logout, Map, Menu, Settings, Build } from '@mui/icons-material';
import {
  AppBar,
  Avatar,
  Box,
  Divider,
  Drawer,
  IconButton,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Stack,
  Toolbar,
  Typography,
} from '@mui/material';
import { useState } from 'react';
import { Navigate, NavLink, Outlet, useLocation } from 'react-router-dom';
import { session } from '../services/session';
const navigation = [
  { label: 'Dashboard', to: '/dashboard', icon: <Dashboard /> },
  { label: 'My Garage', to: '/garage', icon: <DirectionsBike /> },
  { label: 'Maintenance', to: '/maintenance', icon: <Build /> },
  { label: 'Ride planner', to: '/rides', icon: <Map /> },
];
function Navigation({ close }: { close?: () => void }) {
  const location = useLocation();
  return (
    <Box sx={{ width: 280, height: '100%', p: 2.5, bgcolor: '#111b31', color: '#f1d2c9' }}>
      <Stack direction="row" alignItems="center" gap={1.25} px={1} py={1.5}>
        <Box
          sx={{
            width: 32,
            height: 32,
          bgcolor: 'primary.main', color: '#081224',
            borderRadius: 2,
            display: 'grid',
            placeItems: 'center',
          }}
        >
          <DirectionsBike fontSize="small" />
        </Box>
        <Box><Typography fontWeight={900} sx={{ lineHeight: 1 }}>RIDER<br />COMPANION</Typography><Typography variant="caption" className="technical-label">Technical utility</Typography></Box>
      </Stack>
      <Typography variant="overline" color="secondary.main" sx={{ px: 1, display: 'block', mt: 5 }}>
        Command center
      </Typography>
      <List>
        {navigation.map((item) => (
          <ListItemButton
            component={NavLink}
            to={item.to}
            key={item.to}
            selected={location.pathname.startsWith(item.to)}
            onClick={close}
            sx={{
              borderRadius: 2,
              mb: 0.5,
              color: 'inherit', '&.active': { bgcolor: 'primary.main', color: '#081224', boxShadow: '3px 3px 0 #742100' },
            }}
          >
            <ListItemIcon sx={{ minWidth: 38, color: 'inherit' }}>{item.icon}</ListItemIcon>
            <ListItemText primary={item.label} primaryTypographyProps={{ fontWeight: 650 }} />
          </ListItemButton>
        ))}
      </List>
      <Divider sx={{ my: 3, borderColor: '#34425f' }} />
      <List>
        <ListItemButton component={NavLink} to="/profile" onClick={close} sx={{ borderRadius: 2 }}>
          <ListItemIcon sx={{ minWidth: 38 }}>
            <Settings />
          </ListItemIcon>
          <ListItemText primary="Profile" />
        </ListItemButton>
        <ListItemButton component={NavLink} to="/sign-out" onClick={close} sx={{ borderRadius: 2 }}>
          <ListItemIcon sx={{ minWidth: 38 }}>
            <Logout />
          </ListItemIcon>
          <ListItemText primary="Sign out" />
        </ListItemButton>
      </List>
    </Box>
  );
}
export function AppShell() {
  const [open, setOpen] = useState(false);
  if (!session.get()) return <Navigate to="/sign-in" replace />;
  return (
    <Box sx={{ display: 'flex', minHeight: '100vh' }}>
      <Box
        component="nav"
        sx={{
          display: { xs: 'none', md: 'block' },
          flexShrink: 0,
          width: 260,
        }}
      >
        <Drawer
          variant="permanent"
          PaperProps={{ sx: { width: 280, border: 0 } }}
        >
          <Navigation />
        </Drawer>
      </Box>
      <AppBar
        position="fixed"
        color="inherit"
        elevation={0}
        sx={{
          display: { md: 'none' },
          bgcolor: '#111b31', color: '#eef1ff', borderBottom: '1px solid #34425f',
        }}
      >
        <Toolbar>
          <IconButton onClick={() => setOpen(true)} aria-label="Open navigation">
            <Menu />
          </IconButton>
          <Typography fontWeight={850} sx={{ flexGrow: 1, ml: 1 }}>
            Rider Companion
          </Typography>
          <Avatar sx={{ width: 31, height: 31, bgcolor: 'primary.main', color: '#081224' }}>R</Avatar>
        </Toolbar>
      </AppBar>
      <Drawer open={open} onClose={() => setOpen(false)}>
        <Navigation close={() => setOpen(false)} />
      </Drawer>
      <Box component="main" sx={{ flexGrow: 1, minWidth: 0, pt: { xs: 8, md: 0 } }}>
        <Outlet />
      </Box>
    </Box>
  );
}
export function AuthLayout() {
  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'grid',
        gridTemplateColumns: { md: '1fr 1.1fr' },
        bgcolor: '#081224',
      }}
    >
      <Box
        sx={{
          display: { xs: 'none', md: 'flex' },
          bgcolor: '#111b31', color: 'white',
          p: 7,
          flexDirection: 'column',
          justifyContent: 'space-between',
          backgroundImage:
            'linear-gradient(145deg, rgba(8,18,36,.95), rgba(8,18,36,.55)), url(https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=1400&q=80)',
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      >
        <Stack direction="row" alignItems="center" gap={1}>
          <DirectionsBike />
          <Typography fontWeight={850}>Rider Companion</Typography>
        </Stack>
        <Box>
          <Typography variant="h3" fontSize="3.4rem">
            Every ride starts with confidence.
          </Typography>
          <Typography sx={{ mt: 2, maxWidth: 430, color: 'rgba(255,255,255,.75)' }}>
            Keep your motorcycle, maintenance and next adventure in one thoughtful place.
          </Typography>
        </Box>
        <Typography variant="body2" color="rgba(255,255,255,.6)">
          Built for riders, not spreadsheets.
        </Typography>
      </Box>
      <Box sx={{ display: 'grid', placeItems: 'center', p: { xs: 2, sm: 5 } }}>
        <Outlet />
      </Box>
    </Box>
  );
}
