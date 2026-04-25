import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { catalogApi } from '../../api/catalogApi';
import { formatCurrency } from '../../utils/formatters';

export default function ProductDetailPage() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    catalogApi.getProduct(id)
      .then((res) => setProduct(res.data))
      .catch((err) => setError(err.response?.data?.message || 'Could not load product'))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <div className="spinner">Loading…</div>;
  if (error) return <div className="card">{error}</div>;
  if (!product) return null;

  return (
    <div className="card" style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 24 }}>
      <img src={product.imageUrl} alt={product.name} style={{ width: '100%', borderRadius: 8 }} />
      <div>
        <Link to="/customer/products" style={{ fontSize: 13 }}>← Back to products</Link>
        <h1 style={{ marginTop: 12 }}>{product.name}</h1>
        <div style={{ color: '#64748b' }}>{product.categoryName}</div>
        <div style={{ fontSize: 24, fontWeight: 700, color: 'var(--primary)', margin: '16px 0' }}>
          {formatCurrency(product.price)}
        </div>
        <p>{product.description}</p>
        <div style={{ color: product.stockQty > 0 ? '#16a34a' : '#dc2626', marginBottom: 16 }}>
          {product.stockQty > 0 ? `In stock: ${product.stockQty}` : 'Out of stock'}
        </div>
        <button className="btn" disabled={product.stockQty === 0}>
          Add to cart (Phase 2)
        </button>
      </div>
    </div>
  );
}
