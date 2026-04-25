import api from './axiosInstance';

export const orderApi = {
  getCart: () => api.get('/orders/cart'),
  addItem: (payload) => api.post('/orders/cart/items', payload),
  startCheckout: () => api.post('/orders/checkout/start'),
};
