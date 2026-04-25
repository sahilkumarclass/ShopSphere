import { useEffect, useState } from 'react';
import { catalogApi } from '../../api/catalogApi';
import { ProductCard } from '../../components/shared/ProductCard';

export default function HomePage() {
  const [featured, setFeatured] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    catalogApi.featured()
      .then((res) => setFeatured(res.data))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <h1 style={{ marginTop: 0 }}>Featured</h1>
      {loading ? (
        <div className="spinner">Loading featured products…</div>
      ) : featured.length === 0 ? (
        <div className="card">No featured products yet.</div>
      ) : (
        <div className="product-grid">
          {featured.map((p) => <ProductCard key={p.id} product={p} />)}
        </div>
      )}
    </div>
  );
}
