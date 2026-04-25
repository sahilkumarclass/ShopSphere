import { Outlet } from 'react-router-dom';
import { Navbar } from './Navbar';

export function CustomerLayout() {
  return (
    <>
      <Navbar />
      <main className="container">
        <Outlet />
      </main>
    </>
  );
}
