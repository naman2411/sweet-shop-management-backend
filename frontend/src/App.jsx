import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import SweetListPage from './pages/SweetListPage';
import AdminPage from './pages/AdminPage';

function App() {
  return (
    <BrowserRouter>
      {/* Navigation Bar (Visible on all pages) */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
        <div className="container">
          <Link className="navbar-brand" to="/">Sweet Shop</Link>
          <div className="navbar-nav">
            <Link className="nav-link" to="/">Home</Link>
            <Link className="nav-link" to="/admin">Admin</Link>
            <Link className="btn btn-outline-light ms-3" to="/login">Login</Link>
          </div>
        </div>
      </nav>

      {/* Page Content Switcher */}
      <Routes>
        <Route path="/" element={<SweetListPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/admin" element={<AdminPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;