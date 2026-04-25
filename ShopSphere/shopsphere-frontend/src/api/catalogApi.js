import api from './axiosInstance';

export const catalogApi = {
  listProducts: (params = {}) => api.get('/catalog/products', { params }),
  getProduct: (id) => api.get(`/catalog/products/${id}`),
  featured: () => api.get('/catalog/featured'),
  categories: () => api.get('/catalog/categories'),
  productsInCategory: (id, params = {}) => api.get(`/catalog/categories/${id}`, { params }),
};
