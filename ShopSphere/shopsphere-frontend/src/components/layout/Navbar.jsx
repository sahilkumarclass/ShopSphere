import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

export function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/auth/login');
  };

  return (
    <header className="navbar">
      <Link to="/" className="brand">ShopSphere</Link>
      <nav>
        <Link to="/customer/home">Home</Link>
        <Link to="/customer/products">Products</Link>
        {user?.role === 'ADMIN' && <Link to="/admin/dashboard">Admin</Link>}
        {user ? (
          <>
            <span style={{ marginLeft: 16, color: '#64748b' }}>Hi, {user.name || user.email}</span>
            <button className="btn secondary" style={{ marginLeft: 12 }} onClick={handleLogout}>
              Logout
            </button>
          </>
        ) : (
          <Link to="/auth/login" style={{ marginLeft: 16 }}>Login</Link>
        )}
      </nav>
    </header>
  );
}
