import { useCallback, useEffect, useState } from 'react';
import { Container, Grid, Box, Typography } from '@mui/material';
import { ShieldCheck } from 'lucide-react';
import { FileUploader } from './components/FileUploader';
import { DashboardTable } from './components/DashboardTable';
import type { AnalyticsSummary } from './api/client';
import { fetchTransactions, fetchSummary } from './api/client';
import type { Transaction } from './api/client';
import { StatsCards } from './components/StatsCards';
import { useWebSocket } from './api/useWebSocket';
import { CashFlowCalendar } from './components/CashFlowCalendar';

function App() {
 const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [summary, setSummary] = useState<AnalyticsSummary | null>(null);
  const [loading, setLoading] = useState(false);

  // Senior Tip: Use useCallback so the WebSocket hook doesn't trigger on every render
  const loadData = useCallback(async () => {
    setLoading(true);
    try {
      const [txData, summaryData] = await Promise.all([
        fetchTransactions(),
        fetchSummary()
      ]);
      setTransactions(txData);
      setSummary(summaryData);
    } catch (err) {
      console.error("Failed to load data", err);
    } finally {
      setLoading(false);
    }
  }, []);

  // Pass the memoized loadData to the hook
  useWebSocket(loadData);

  useEffect(() => {
    loadData();
  }, [loadData]);

  return (
    <Container maxWidth="lg" sx={{ py: 6 }}>
      {/* Header */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 6 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <ShieldCheck size={48} color="#10b981" />
          <Box>
            <Typography variant="h3">BillGuardian</Typography>
            <Typography variant="subtitle1" color="textSecondary">Household CFO Intelligence</Typography>
          </Box>
        </Box>
        <Box sx={{ textAlign: 'right' }}>
          <Typography variant="h4" color="primary">$0.00</Typography>
          <Typography variant="caption" color="textSecondary">Potential Savings Detected</Typography>
        </Box>
      </Box>
      <StatsCards summary={summary} />
      <Grid container spacing={4}>
        <Grid size={{ xs: 12, md: 4 }}>
          <FileUploader onUploadSuccess={loadData} />
        </Grid>
        <Grid size={{ xs: 12, md: 8 }}>
          <DashboardTable data={transactions} loading={loading} />
          <CashFlowCalendar transactions={transactions} />
        </Grid>
      </Grid>
    </Container>
  );
}

export default App;