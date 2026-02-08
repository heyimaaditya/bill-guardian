import React, { useState } from 'react';
import { Button, Typography, Box, Paper, CircularProgress, Alert } from '@mui/material';
import { UploadCloud } from 'lucide-react';
import { uploadStatement } from '../api/client';

interface Props {
  onUploadSuccess: () => void;
}

export const FileUploader: React.FC<Props> = ({ onUploadSuccess }) => {
  const [file, setFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState<{ type: 'success' | 'error', msg: string } | null>(null);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setFile(event.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!file) return;
    setLoading(true);
    setStatus(null);
    try {
      await uploadStatement(file);
      setStatus({ type: 'success', msg: 'Audit Started! Refreshing dashboard...' });
      setFile(null);
      // Wait a second for Kafka to process before refreshing
      setTimeout(onUploadSuccess, 1500);
    } catch (err) {
      setStatus({ type: 'error', msg: 'Connection failed. Is the Backend running?' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Paper variant="outlined" sx={{ p: 4, textAlign: 'center', borderColor: 'divider' }}>
      <UploadCloud size={40} color="#10b981" />
      <Typography variant="h6" sx={{ mt: 2 }}>Upload Statement</Typography>
      <Typography variant="body2" color="textSecondary" sx={{ mb: 3 }}>
        Upload CSV (Chase/Amex format)
      </Typography>

      <Box sx={{ mb: 2 }}>
        <input accept=".csv" style={{ display: 'none' }} id="file-upload" type="file" onChange={handleFileChange} />
        <label htmlFor="file-upload">
          <Button variant="outlined" component="span" fullWidth sx={{ py: 1.5 }}>
            {file ? file.name : 'Select File'}
          </Button>
        </label>
      </Box>

      <Button
        variant="contained"
        fullWidth
        disabled={!file || loading}
        onClick={handleUpload}
        sx={{ py: 1.5, fontWeight: 'bold' }}
      >
        {loading ? <CircularProgress size={24} color="inherit" /> : 'Run AI Auditor'}
      </Button>

      {status && <Alert severity={status.type} sx={{ mt: 2 }}>{status.msg}</Alert>}
    </Paper>
  );
};