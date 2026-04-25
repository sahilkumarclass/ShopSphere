import { useEffect, useState } from 'react';
import { catalogApi } from '../../api/catalogApi';
import { ProductCard } from '../../components/shared/ProductCard';

export default function ProductListPage() {
  const [data, setData] = useState({ content: [], totalPages: 0, number: 0 });
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(0);

  const load = (p = 0, q = '') => {
    setLoading(true);
    catalogApi.listProducts({ page: p, size: 12, search: q || undefined })
      .then((res) => setData(res.data))
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(page, search); /* eslint-disable-next-line */ }, [page]);

  const onSearch = (e) => {
    e.preventDefault();
    setPage(0);
    load(0, search);
  };

  return (
    <div>
      <h1 style={{ marginTop: 0 }}>All Products</h1>

      <form className="toolbar" onSubmit={onSearch}>
        <input
          type="search"
          placeholder="Search products…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button className="btn" type="submit">Search</button>
      </form>

      {loading ? (
        <div className="spinner">Loading…</div>
      ) : data.content.length === 0 ? (
        <div className="card">No products found.</div>
      ) : (
        <div className="product-grid">
          {data.content.map((p) => <ProductCard key={p.id} product={p} />)}
        </div>
      )}

      {data.totalPages > 1 && (
        <div className="pagination">
          <button className="btn secondary" disabled={page === 0} onClick={() => setPage(page - 1)}>
            Prev
          </button>
          <span>Page {data.number + 1} of {data.totalPages}</span>
          <button
            className="btn secondary"
            disabled={page >= data.totalPages - 1}
            onClick={() => setPage(page + 1)}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}
