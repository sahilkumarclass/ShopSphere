export default function PlaceholderPage({ title = 'Coming soon' }) {
  return (
    <div className="card">
      <h1 style={{ marginTop: 0 }}>{title}</h1>
      <p>This screen is part of Phase 2 of the ShopSphere build.</p>
    </div>
  );
}
