import { Link } from 'react-router-dom';
import { formatCurrency } from '../../utils/formatters';

export function ProductCard({ product }) {
  return (
    <Link to={`/customer/products/${product.id}`} className="product-card">
      <img src={product.imageUrl} alt={product.name} loading="lazy" />
      <div className="body">
        <div className="name">{product.name}</div>
        <div className="meta">{product.categoryName}</div>
        <div className="price">{formatCurrency(product.price)}</div>
        {product.stockQty <= 5 && product.stockQty > 0 && (
          <div className="meta" style={{ color: '#dc2626' }}>Only {product.stockQty} left</div>
        )}
        {product.stockQty === 0 && (
          <div className="meta" style={{ color: '#dc2626' }}>Out of stock</div>
        )}
      </div>
    </Link>
  );
}
