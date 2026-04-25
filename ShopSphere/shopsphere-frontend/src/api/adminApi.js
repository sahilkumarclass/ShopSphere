import api from './axiosInstance';

export const adminApi = {
  dashboard: () => api.get('/admin/dashboard'),
};
