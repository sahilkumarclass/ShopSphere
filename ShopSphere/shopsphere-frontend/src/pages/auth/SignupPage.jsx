import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authApi } from '../../api/authApi';
import { useAuth } from '../../hooks/useAuth';

export default function SignupPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      const res = await authApi.signup(form);
      login(res.data.token);
      navigate('/customer/home');
    } catch (err) {
      const data = err.response?.data;
      const msg = data?.fieldErrors
        ? Object.values(data.fieldErrors).join(', ')
        : data?.message || 'Signup failed';
      setError(msg);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form className="form card" onSubmit={onSubmit}>
      <h2 style={{ marginTop: 0 }}>Create your account</h2>
      <label>Name
        <input name="name" value={form.name} onChange={onChange} required minLength={2} />
      </label>
      <label>Email
        <input name="email" type="email" value={form.email} onChange={onChange} required />
      </label>
      <label>Password
        <input name="password" type="password" value={form.password} onChange={onChange} required minLength={6} />
      </label>
      {error && <div className="error">{error}</div>}
      <button className="btn" type="submit" disabled={submitting}>
        {submitting ? 'Creating account…' : 'Sign up'}
      </button>
      <div style={{ textAlign: 'center', fontSize: 13 }}>
        Already have an account? <Link to="/auth/login">Sign in</Link>
      </div>
    </form>
  );
}
