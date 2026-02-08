import React from 'react';
import { DataGrid } from '@mui/x-data-grid';
import type { GridColDef } from '@mui/x-data-grid';
import { Paper, Typography, Box, Chip, Tooltip, LinearProgress, IconButton } from '@mui/material';
import type { Transaction } from '../api/client';
import { FileDown } from 'lucide-react';

const columns: GridColDef[] = [
  { field: 'transactionDate', headerName: 'Date', width: 110 },
  { field: 'merchantName', headerName: 'Merchant', width: 180 },
  { 
    field: 'category', 
    headerName: 'Category', 
    width: 140,
    renderCell: (params) => (
      <Chip label={params.value} size="small" variant="outlined" 
            color={params.value === 'UTILITY' ? 'primary' : 'default'} />
    )
  },
  { 
    field: 'amount', 
    headerName: 'Amount', 
    width: 100,
    valueFormatter: (value) => `$${value}`
  },
  { 
    field: 'aiAdvice', 
    headerName: 'AI Strategy & Scripts', 
    flex: 1,
    renderCell: (params) => (
      <Tooltip title={params.value}>
        <Typography variant="body2" sx={{ color: '#10b981', cursor: 'help', mt: 1.5 }}>
          {params.value}
        </Typography>
      </Tooltip>
    )
  },
  { 
  field: 'communityAverage', 
  headerName: 'Price Health', 
  width: 150,
  renderCell: (params) => {
    if (!params.value) return <Typography variant="caption">N/A</Typography>;
    
    const userPaid = params.row.amount;
    const fairPrice = params.value;
    // Calculate how much over/under they are (percentage)
    const ratio = Math.min((fairPrice / userPaid) * 100, 100);
    const isOverpaying = userPaid > fairPrice;

    return (
      <Box sx={{ width: '100%', mt: 1.5 }}>
        <LinearProgress 
          variant="determinate" 
          value={ratio} 
          color={isOverpaying ? "error" : "success"}
          sx={{ height: 8, borderRadius: 5 }}
        />
        <Typography variant="caption" sx={{ fontSize: '0.6rem' }}>
          {isOverpaying ? `Paying ${((userPaid/fairPrice - 1) * 100).toFixed(0)}% more than avg` : "Fair Price"}
        </Typography>
      </Box>
    );
  }
},
{
  field: 'actions',
  headerName: 'Export',
  width: 100,
  renderCell: (params) => (
    <IconButton
      color="primary" 
      onClick={() => window.open(`http://localhost:8080/api/statements/${params.row.id}/export`, '_blank')}
      disabled={!params.row.aiAdvice.includes('SCRIPT')}
    >
      <FileDown size={20} />
    </IconButton>
  )
}
];

interface Props {
  data: Transaction[];
  loading: boolean;
}

export const DashboardTable: React.FC<Props> = ({ data, loading }) => {
  return (
    <Box sx={{ height: 500, width: '100%', mt: 2 }}>
      <Paper variant="outlined" sx={{ height: '100%', p: 2 }}>
        <DataGrid
          rows={data}
          columns={columns}
          loading={loading}
          getRowId={(row) => row.id}
          initialState={{ pagination: { paginationModel: { pageSize: 7 } } }}
          sx={{ border: 0, '& .MuiDataGrid-columnHeaderTitle': { fontWeight: 'bold' } }}
        />
      </Paper>
    </Box>
  );
};