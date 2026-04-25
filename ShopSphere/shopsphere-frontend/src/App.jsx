import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/shared/ProtectedRoute';
import { CustomerLayout } from './components/layout/CustomerLayout';
import { AdminLayout } from './components/layout/AdminLayout';

import LoginPage from './pages/auth/LoginPage';
import SignupPage from './pages/auth/SignupPage';
import HomePage from './pages/customer/HomePage';
import ProductListPage from './pages/customer/ProductListPage';
import ProductDetailPage from './pages/customer/ProductDetailPage';
import PlaceholderPage from './pages/customer/PlaceholderPage';
import DashboardPage from './pages/admin/DashboardPage';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public auth routes */}
          <Route path="/auth/login" element={<LoginPage />} />
          <Route path="/auth/signup" element={<SignupPage />} />

          {/* Customer */}
          <Route element={<ProtectedRoute role="CUSTOMER" />}>
            <Route element={<CustomerLayout />}>
              <Route path="/customer/home" element={<HomePage />} />
              <Route path="/customer/products" element={<ProductListPage />} />
              <Route path="/customer/products/:id" element={<ProductDetailPage />} />
              <Route path="/customer/cart" element={<PlaceholderPage title="Cart" />} />
              <Route path="/customer/checkout" element={<PlaceholderPage title="Checkout" />} />
              <Route path="/customer/orders" element={<PlaceholderPage title="My Orders" />} />
              <Route path="/customer/orders/:id" element={<PlaceholderPage title="Order Details" />} />
            </Route>
          </Route>

          {/* Admin */}
          <Route element={<ProtectedRoute role="ADMIN" />}>
            <Route element={<AdminLayout />}>
              <Route path="/admin/dashboard" element={<DashboardPage />} />
              <Route path="/admin/products" element={<PlaceholderPage title="Manage Products" />} />
              <Route path="/admin/orders" element={<PlaceholderPage title="Manage Orders" />} />
              <Route path="/admin/reports" element={<PlaceholderPage title="Reports" />} />
            </Route>
          </Route>

          <Route path="/" element={<Navigate to="/customer/home" replace />} />
          <Route path="*" element={<Navigate to="/customer/home" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
