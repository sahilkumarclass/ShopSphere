import { Outlet } from 'react-router-dom';
import { Navbar } from './Navbar';

export function AdminLayout() {
  return (
    <>
      <Navbar />
      <main className="container">
        <h2 style={{ marginTop: 0 }}>Admin</h2>
        <Outlet />
      </main>
    </>
  );
}
