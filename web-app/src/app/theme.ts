import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#245b3c', dark: '#163b27', light: '#e6f1e9', contrastText: '#fff' },
    secondary: { main: '#e58b31' },
    background: { default: '#f7f8f5', paper: '#fff' },
    text: { primary: '#152019', secondary: '#667069' },
    success: { main: '#28744c' },
    warning: { main: '#c9791d' },
    error: { main: '#c83f3f' },
  },
  typography: {
    fontFamily:
      'Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif',
    h3: { fontWeight: 750, letterSpacing: '-0.045em' },
    h4: { fontWeight: 750, letterSpacing: '-0.035em' },
    h5: { fontWeight: 700 },
    button: { fontWeight: 700, textTransform: 'none' },
  },
  shape: { borderRadius: 14 },
  components: {
    MuiButton: {
      styleOverrides: {
        root: { borderRadius: 10, boxShadow: 'none', padding: '10px 18px' },
        contained: { boxShadow: 'none' },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: { border: '1px solid #e8ece7', boxShadow: '0 4px 18px rgba(23, 38, 28, 0.045)' },
      },
    },
    MuiOutlinedInput: { styleOverrides: { root: { backgroundColor: '#fff', borderRadius: 10 } } },
  },
});
