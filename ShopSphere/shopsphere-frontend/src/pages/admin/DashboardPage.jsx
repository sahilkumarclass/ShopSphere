import { useEffect, useState } from 'react';
import { adminApi } from '../../api/adminApi';
import { formatCurrency } from '../../utils/formatters';

export default function DashboardPage() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    adminApi.dashboard()
      .then((res) => setData(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load dashboard'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="spinner">Loading dashboard…</div>;
  if (error) return <div className="card">{error}</div>;

  const tiles = [
    { label: 'Total Revenue', value: formatCurrency(data.totalRevenue) },
    { label: 'Today Revenue', value: formatCurrency(data.todayRevenue) },
    { label: 'Total Orders', value: data.totalOrders },
    { label: 'Pending Orders', value: data.pendingOrders },
    { label: 'Total Users', value: data.totalUsers },
    { label: 'New Users Today', value: data.newUsersToday },
    { label: 'Total Products', value: data.totalProducts },
    { label: 'Low Stock', value: data.lowStockProducts },
  ];

  return (
    <div>
      <div className="product-grid" style={{ gridTemplateColumns: 'repeat(4, 1fr)' }}>
        {tiles.map((t) => (
          <div key={t.label} className="card">
            <div style={{ color: '#64748b', fontSize: 13 }}>{t.label}</div>
            <div style={{ fontSize: 24, fontWeight: 700, marginTop: 6 }}>{t.value}</div>
          </div>
        ))}
      </div>
      <div className="card" style={{ marginTop: 24 }}>
        <h3 style={{ marginTop: 0 }}>Orders by status</h3>
        <ul>
          {Object.entries(data.ordersByStatus || {}).map(([status, count]) => (
            <li key={status}>{status}: {count}</li>
          ))}
        </ul>
      </div>
    </div>
  );
}
