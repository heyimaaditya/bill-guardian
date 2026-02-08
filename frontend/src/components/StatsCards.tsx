import React from 'react';
import { Grid, Paper, Typography, Box } from '@mui/material';
import { DollarSign, Zap, TrendingDown } from 'lucide-react';
import type { AnalyticsSummary } from '../api/client';

interface Props {
  summary: AnalyticsSummary | null;
}

export const StatsCards: React.FC<Props> = ({ summary }) => {
  const stats = [
    {
      title: 'Monthly Burn',
      value: `$${summary?.totalMonthlyBurn.toFixed(2) || '0.00'}`,
      icon: <DollarSign color="#94a3b8" />,
      subtitle: 'Total recurring expenses'
    },
    {
      title: 'Negotiable Items',
      value: `${Object.keys(summary?.spendingByCategory || {}).length}`,
      icon: <Zap color="#fbbf24" />,
      subtitle: 'Active services detected'
    },
    {
      title: 'Potential Savings',
      value: `$${summary?.totalPotentialSavings.toFixed(2) || '0.00'}`,
      icon: <TrendingDown color="#10b981" />,
      subtitle: 'Detected "Lazy Tax"'
    }
  ];

  return (
    <Grid container spacing={3} sx={{ mb: 4 }}>
      {stats.map((stat, index) => (
        <Grid size={{ xs: 12, md: 4 }} key={index}>
          <Paper variant="outlined" sx={{ p: 3, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Box sx={{ p: 1.5, borderRadius: 2, backgroundColor: 'rgba(255,255,255,0.05)' }}>
              {stat.icon}
            </Box>
            <Box>
              <Typography variant="caption" color="textSecondary">{stat.title}</Typography>
              <Typography variant="h5" fontWeight="bold">{stat.value}</Typography>
              <Typography variant="caption" color="primary">{stat.subtitle}</Typography>
            </Box>
          </Paper>
        </Grid>
      ))}
    </Grid>
  );
};