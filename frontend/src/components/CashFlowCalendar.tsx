import React from 'react';
import { 
  format, startOfMonth, endOfMonth, startOfWeek, endOfWeek, 
  eachDayOfInterval, isSameDay, parseISO 
} from 'date-fns';
import { Paper, Box, Typography, Grid, Tooltip } from '@mui/material';
import type { Transaction } from '../api/client';

interface Props {
  transactions: Transaction[];
}

export const CashFlowCalendar: React.FC<Props> = ({ transactions }) => {
  const today = new Date();
  const monthStart = startOfMonth(today);
  const monthEnd = endOfMonth(today);
  const calendarStart = startOfWeek(monthStart);
  const calendarEnd = endOfWeek(monthEnd);

  // Generate all days to show in the 7x5 or 7x6 grid
  const days = eachDayOfInterval({
    start: calendarStart,
    end: calendarEnd,
  });

  return (
    <Paper variant="outlined" sx={{ p: 3, mt: 4 }}>
      <Typography variant="h5" sx={{ mb: 3 }}>
        Predictive Cash-Flow: {format(today, 'MMMM yyyy')}
      </Typography>
      
      {/* Weekday Headers */}
      <Grid container columns={7} sx={{ textAlign: 'center', mb: 1 }}>
        {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map((day) => (
          <Grid size={1} key={day}>
            <Typography variant="caption" fontWeight="bold" color="textSecondary">
              {day}
            </Typography>
          </Grid>
        ))}
      </Grid>

      {/* Calendar Grid */}
      <Grid container columns={7} sx={{ borderTop: '1px solid rgba(255,255,255,0.1)', borderLeft: '1px solid rgba(255,255,255,0.1)' }}>
        {days.map((day, idx) => {
          // Find all transactions that hit on this specific day
          const dayTransactions = transactions.filter(tx => 
            isSameDay(parseISO(tx.transactionDate), day)
          );

          // Calculate total "Stress" (Total cost for this day)
          const dayTotal = dayTransactions.reduce((acc, tx) => acc + tx.amount, 0);

          return (
            <Grid 
              size={1} 
              key={idx} 
              sx={{ 
                height: 100, 
                p: 1, 
                borderRight: '1px solid rgba(255,255,255,0.1)', 
                borderBottom: '1px solid rgba(255,255,255,0.1)',
                backgroundColor: isSameDay(day, today) ? 'rgba(16, 185, 129, 0.05)' : 'transparent'
              }}
            >
              <Typography variant="caption" color={day.getMonth() === today.getMonth() ? 'textPrimary' : 'textSecondary'}>
                {format(day, 'd')}
              </Typography>
              
              {dayTotal > 0 && (
                <Tooltip title={dayTransactions.map(t => `${t.merchantName}: $${t.amount}`).join(', ')}>
                  <Box sx={{ 
                    mt: 1, 
                    p: 0.5, 
                    borderRadius: 1, 
                    backgroundColor: dayTotal > 500 ? 'rgba(239, 68, 68, 0.2)' : 'rgba(16, 185, 129, 0.2)',
                    borderLeft: `3px solid ${dayTotal > 500 ? '#ef4444' : '#10b981'}`
                  }}>
                    <Typography sx={{ fontSize: '0.65rem', fontWeight: 'bold' }}>
                      -${dayTotal.toFixed(0)}
                    </Typography>
                  </Box>
                </Tooltip>
              )}
            </Grid>
          );
        })}
      </Grid>
    </Paper>
  );
};