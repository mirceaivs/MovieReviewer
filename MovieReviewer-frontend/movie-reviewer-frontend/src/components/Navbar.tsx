
import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const Navbar: React.FC = () => {
  const { user, logout, token } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  if (!token) {
    return (
      <nav className="navbar navbar-light bg-light">
        <div className="container-fluid">
          <NavLink className="navbar-brand mx-auto fw-bold fs-3" to="/" style={{ letterSpacing: 1 }}>
            MovieReviewer
          </NavLink>
          <div className="d-flex ms-auto" style={{ gap: "1rem" }}>
            <NavLink className="btn btn-outline-primary px-4" to="/login">
              Login
            </NavLink>
            <NavLink className="btn btn-primary px-4" to="/register">
              Register
            </NavLink>
          </div>
        </div>
      </nav>
    );
  }

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container-fluid">
        <NavLink className="navbar-brand fw-bold" to="/">
          MovieReviewer
        </NavLink>

        <div className="d-flex align-items-center me-2" style={{ gap: '0.5rem' }}>
          <button
            className="btn btn-outline-secondary btn-sm rounded-circle"
            onClick={() => window.history.back()}
            title="Înapoi"
            style={{ width: 36, height: 36, display: 'flex', alignItems: 'center', justifyContent: 'center' }}
          >
            <i className="bi bi-arrow-left"></i>
          </button>
          <button
            className="btn btn-outline-secondary btn-sm rounded-circle"
            onClick={() => window.history.forward()}
            title="Înainte"
            style={{ width: 36, height: 36, display: 'flex', alignItems: 'center', justifyContent: 'center' }}
          >
            <i className="bi bi-arrow-right"></i>
          </button>
        </div>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <NavLink className="nav-link" to="/movies">
                Filme
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link" to="/reviews">
                Review-uri
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link" to="/watchlist">
                Watchlist
              </NavLink>
            </li>
          </ul>
          <ul className="navbar-nav ms-auto">
            <li className="nav-item d-flex align-items-center px-2">
              <NavLink className="nav-link fw-bold" to="/profile">
                {user?.username}
              </NavLink>
            </li>
            <li className="nav-item">
              <button className="btn btn-primary px-4" onClick={handleLogout}>
                Logout
              </button>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
