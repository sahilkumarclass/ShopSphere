import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authApi } from '../../api/authApi';
import { useAuth } from '../../hooks/useAuth';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      const res = await authApi.login(form);
      login(res.data.token);
      navigate(res.data.role === 'ADMIN' ? '/admin/dashboard' : '/customer/home');
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form className="form card" onSubmit={onSubmit}>
      <h2 style={{ marginTop: 0 }}>Welcome back</h2>
      <label>Email
        <input name="email" type="email" value={form.email} onChange={onChange} required />
      </label>
      <label>Password
        <input name="password" type="password" value={form.password} onChange={onChange} required />
      </label>
      {error && <div className="error">{error}</div>}
      <button className="btn" type="submit" disabled={submitting}>
        {submitting ? 'Signing in…' : 'Sign in'}
      </button>
      <div style={{ textAlign: 'center', fontSize: 13 }}>
        New to ShopSphere? <Link to="/auth/signup">Create an account</Link>
      </div>
    </form>
  );
}
