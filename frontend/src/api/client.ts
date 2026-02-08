import axios from 'axios';

const client = axios.create({
  baseURL: 'http://localhost:8080/api',
});

export interface Transaction {
  id: number;
  transactionDate: string;
  merchantName: string;
  amount: number;
  category: string;
  aiAdvice: string;
}

export interface AnalyticsSummary {
  totalMonthlyBurn: number;
  totalPotentialSavings: number;
  spendingByCategory: Record<string, number>;
}

export const uploadStatement = async (file: File): Promise<void> => {
  const formData = new FormData();
  formData.append('file', file);
  await client.post('/statements/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

export const fetchTransactions = async (): Promise<Transaction[]> => {
  const response = await client.get<Transaction[]>('/statements');
  return response.data;
};

export const fetchSummary = async (): Promise<AnalyticsSummary> => {
  const response = await client.get<AnalyticsSummary>('/statements/summary');
  return response.data;
};

export default client;