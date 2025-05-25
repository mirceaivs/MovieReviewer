import React from "react";
import { Routes, Route, useLocation, useNavigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import { useAuth } from "./context/AuthContext";
import MovieList from "./features/movies/components/MovieList";
import MovieDetails from "./features/movies/components/MovieDetails";
import WatchlistPage from "./features/watchlist/components/WatchlistPage";
import Login from "./features/auth/components/Login";
import Register from "./features/auth/components/Register";
// import GenreList from "./features/genres/components/GenreList";
import AllMoviesList from "./features/movies/components/AllMoviesList";
import MyReviews from "./features/reviews/components/MyReviews";
import UserProfilePage from "./features/user/components/UserProfile";
// import CreateMoviePage from "./features/movies/components/CreateMoviePage";
import EditMoviePage from "./features/movies/components/EditMoviePage";

const App: React.FC = () => {
  const { token } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const isVisitorHome =
    !token &&
    (location.pathname === "/" || location.pathname === "/home" || location.pathname === "");

  return (
    <>
      <Navbar />
      <div className="container mt-4">
        {isVisitorHome ? (
          <div className="d-flex flex-column align-items-center justify-content-center" style={{ height: "60vh" }}>
            <h1 className="mb-4 fw-bold">Trebuie sa va logati pentru a cauta filme!</h1>
            <div>
              <button className="btn btn-primary btn-lg me-3" onClick={() => navigate("/login")}>
                Login
              </button>
              <button className="btn btn-outline-primary btn-lg" onClick={() => navigate("/register")}>
                Register
              </button>
            </div>
          </div>
        ) : (
        
          <Routes>
            <Route path="/" element={<MovieList />} />
            <Route path="/movies/:id" element={<MovieDetails />} />
            {/* <Route path="/genres" element={<GenreList />} /> */}
            <Route path="/watchlist" element={<WatchlistPage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/movies" element={<AllMoviesList />} />
            <Route path="/movies/:id" element={<MovieDetails />} />
            <Route path="/profile" element={<UserProfilePage />} />
            <Route path="/reviews" element={<MyReviews />} />  
            <Route path="/movies/:id/edit" element={<EditMoviePage />} />
            {/* <Route path="/movies/create" element={<CreateMoviePage />} /> */}
          </Routes>
        )}
      </div>
    </>
  );
};

export default App;
