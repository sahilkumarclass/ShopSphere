import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

export function ProtectedRoute({ role }) {
  const { user, ready } = useAuth();

  if (!ready) return <div className="spinner">Loading…</div>;
  if (!user) return <Navigate to="/auth/login" replace />;
  if (role && user.role !== role) return <Navigate to="/auth/login" replace />;

  return <Outlet />;
}
