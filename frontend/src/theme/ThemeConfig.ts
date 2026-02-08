import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: { main: '#10b981' }, // Emerald Green
    background: {
      default: '#020617', // Slate Black
      paper: '#0f172a',    // Slate Blue
    },
    text: {
      primary: '#f8fafc',
      secondary: '#94a3b8',
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica", sans-serif',
    h3: { fontWeight: 800 },
  },
});