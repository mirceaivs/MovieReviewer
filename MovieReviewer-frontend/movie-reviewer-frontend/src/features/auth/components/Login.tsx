import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import { useAuth } from '../../../context/AuthContext'; 

const Login: React.FC = () => {
  const navigate = useNavigate();
  const { login } = useAuth(); 
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [fieldErrors, setFieldErrors] = useState<{ [key: string]: string }>({});
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFieldErrors({});
    setErrorMsg(null);
    try {
      const response = await authService.login({ email, password });
      login(response.data.token); 
      navigate('/'); 
    } catch (error: any) {
      if (error.response) {
        if (error.response.status === 400) {
          const data = error.response.data;
          if (data.error) {
            setErrorMsg(data.error);
          } else {
            setFieldErrors(data);
          }
        } else {
          setErrorMsg('Eroare la conectare. Încercați din nou.');
        }
      } else {
        setErrorMsg('Eroare la conectare. Încercați din nou.');
      }
    }
  };

  return (
    <div className="auth-form">
      <h2>Autentificare</h2>
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Email</label>
          <input 
            type="email"
            className={`form-control ${fieldErrors.email ? 'is-invalid' : ''}`}
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required 
          />
          {fieldErrors.email && <div className="invalid-feedback">{fieldErrors.email}</div>}
        </div>
        <div className="mb-3">
          <label className="form-label">Parola</label>
          <input 
            type="password"
            className={`form-control ${fieldErrors.password ? 'is-invalid' : ''}`}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required 
          />
          {fieldErrors.password && <div className="invalid-feedback">{fieldErrors.password}</div>}
        </div>
        <button type="submit" className="btn btn-primary">Login</button>
      </form>
    </div>
  );
};

export default Login;
