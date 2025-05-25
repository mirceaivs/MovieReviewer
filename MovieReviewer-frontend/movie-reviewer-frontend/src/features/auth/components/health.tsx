import React, { useEffect, useState } from "react";
import axios from "axios";

export const HealthCheck: React.FC = () => {
  const [status, setStatus] = useState<string>("Checking...");
  const [error, setError] = useState<string>("");

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/auth/health")
      .then((res) => setStatus(res.data))
      .catch((err) => {
        setStatus("Nu merge");
        setError(err.message);
      });
  }, []);

  return (
    <div className="container mt-4">
      <div className={`alert ${error ? "alert-danger" : "alert-success"}`}>
        <strong>Status backend:</strong> {status}
        {error && (
          <div style={{ fontSize: 12 }}>
            <strong>Eroare:</strong> {error}
          </div>
        )}
      </div>
    </div>
  );
};

export default HealthCheck;
