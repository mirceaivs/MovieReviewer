import React, { useState } from 'react';
import authService from '../services/authService';
import { useNavigate } from 'react-router-dom';

const Register: React.FC = () => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [password, setPassword] = useState('');
  const [fieldErrors, setFieldErrors] = useState<{ [key: string]: string }>({});
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);

  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFieldErrors({});
    setErrorMsg(null);
    setSuccessMsg(null);
    try {
      const response = await authService.register({ email, username, firstName, lastName, password });
      setSuccessMsg(`Contul utilizatorului ${response.data.username} a fost creat cu succes. Vă puteți autentifica.`);
      setTimeout(() => {
        navigate('/login');
      }, 1000); 
      setEmail(''); setUsername(''); setFirstName(''); setLastName(''); setPassword('');
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
          setErrorMsg('Eroare la înregistrare. Încercați din nou.');
        }
      } else {
        setErrorMsg('Eroare la înregistrare. Încercați din nou.');
      }
    }
  };

  return (
    <div className="auth-form">
      <h2>Inregistrare</h2>
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}
      {successMsg && <div className="alert alert-success">{successMsg}</div>}
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
          <label className="form-label">Nume utilizator</label>
          <input 
            type="text"
            className={`form-control ${fieldErrors.username ? 'is-invalid' : ''}`}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required 
          />
          {fieldErrors.username && <div className="invalid-feedback">{fieldErrors.username}</div>}
        </div>
        <div className="mb-3">
          <label className="form-label">Prenume</label>
          <input 
            type="text"
            className={`form-control ${fieldErrors.firstName ? 'is-invalid' : ''}`}
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
            required 
          />
          {fieldErrors.firstName && <div className="invalid-feedback">{fieldErrors.firstName}</div>}
        </div>
        <div className="mb-3">
          <label className="form-label">Nume</label>
          <input 
            type="text"
            className={`form-control ${fieldErrors.lastName ? 'is-invalid' : ''}`}
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
            required 
          />
          {fieldErrors.lastName && <div className="invalid-feedback">{fieldErrors.lastName}</div>}
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
        <button type="submit" className="btn btn-primary">Inregistrare</button>
      </form>
    </div>
  );
};

export default Register;

