import React, { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import userService from '../services/userService';
import type { UserProfile } from '../type';

const UserProfilePage: React.FC = () => {
  const [user, setUser] = useState<UserProfile | null>(null);
  const [edit, setEdit] = useState(false);
  const [form, setForm] = useState<UserProfile>({});
  const [msg, setMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const modalRef = useRef<HTMLDivElement>(null);

  const navigate = useNavigate();

  useEffect(() => {
    userService.getProfile().then(res => setUser(res.data));
  }, []);

  const handleEdit = () => {
    if (!user) return;
    setForm({
      email: user.email || "",
      username: user.username || "",
      firstName: user.firstName || "",
      lastName: user.lastName || "",
      bio: user.bio || "",
      password: "",
      oldPassword: ""
    });
    setEdit(true);
    setMsg(null);
  };

  const isFormDirty = user && Object.keys(form).some(
    key => form[key as keyof UserProfile] !== undefined && form[key as keyof UserProfile] !== user[key as keyof UserProfile]
  );

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setMsg(null);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMsg(null);
    if (!isFormDirty) {
      setMsg('Nu ai modificat nimic!');
      return;
    }
    setLoading(true);
    try {
      await userService.updateProfile(form);
      setMsg('Profil actualizat!');
      setEdit(false);
      const updated = await userService.getProfile();
      setUser(updated.data);
    } catch (err: any) {
      const backendMsg = err?.response?.data?.error || 'Eroare la update!';
      setMsg(backendMsg);
    } finally {
      setLoading(false);
    }
  };

  const handleShowModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  const handleDelete = async () => {
    setLoading(true);
    try {
      await userService.deleteProfile();
      navigate('/'); 
    } catch (err) {
      setMsg('Eroare la stergere.');
    } finally {
      setLoading(false);
    }
  };

  if (!user) return <div className="text-center mt-5">Se incarca...</div>;

  return (
    <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: "70vh" }}>
      <div className="col-md-7 col-lg-6">
        <h2 className="text-center mb-4">Profilul meu</h2>
        {msg && <div className="alert alert-info">{msg}</div>}
        {!edit ? (
          <div className="card p-4 shadow">
            <p><b>Email:</b> {user.email}</p>
            <p><b>Username:</b> {user.username}</p>
            <p><b>Prenume:</b> {user.firstName}</p>
            <p><b>Nume:</b> {user.lastName}</p>
            <p><b>Bio:</b> {user.bio}</p>
            <p><b>Cont creat la:</b> {user.createdAt && new Date(user.createdAt).toLocaleString()}</p>
            <div className="d-flex justify-content-center">
              <button className="btn btn-primary me-2" onClick={handleEdit}>Editeaza</button>
              <button className="btn btn-danger" onClick={handleShowModal}>Sterge contul</button>
            </div>
          </div>
        ) : (
          <form className="card p-4 shadow" onSubmit={handleSubmit}>
            <div className="mb-2">
              <label>Email</label>
              <input name="email" value={form.email || ""} onChange={handleChange} className="form-control" />
            </div>
            <div className="mb-2">
              <label>Username</label>
              <input name="username" value={form.username || ""} onChange={handleChange} className="form-control" />
            </div>
            <div className="mb-2">
              <label>Prenume</label>
              <input name="firstName" value={form.firstName || ""} onChange={handleChange} className="form-control" />
            </div>
            <div className="mb-2">
              <label>Nume</label>
              <input name="lastName" value={form.lastName || ""} onChange={handleChange} className="form-control" />
            </div>
            <div className="mb-2">
              <label>Bio</label>
              <textarea name="bio" value={form.bio || ""} onChange={handleChange} className="form-control" />
            </div>
            <div className="mb-2">
              <label>Parola noua</label>
              <input type="password" name="password" value={form.password || ""} onChange={handleChange} className="form-control" />
            </div>
            <div className="mb-2">
              <label>Parola veche </label>
              <input type="password" name="oldPassword" value={form.oldPassword || ""} onChange={handleChange} className="form-control" />
            </div>
            <div className="d-flex justify-content-center">
              <button className="btn btn-success me-2" type="submit" disabled={!isFormDirty || loading}>
                {loading ? "Salveaza..." : "Salveaza"}
              </button>
              <button className="btn btn-secondary" type="button" onClick={() => setEdit(false)} disabled={loading}>Renunta</button>
            </div>
          </form>
        )}

        {showModal && (
          <div className="modal fade show d-block" tabIndex={-1} ref={modalRef} style={{ backgroundColor: 'rgba(0,0,0,0.3)' }}>
            <div className="modal-dialog modal-dialog-centered">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title text-danger">Esti sigur ca vrei sa stergi contul?</h5>
                  <button type="button" className="btn-close" onClick={handleCloseModal}></button>
                </div>
                <div className="modal-body">
                  <p>Acastra actiune este <b>ireversibila</b>. Toate datele vor fi sterse permanent. </p>
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>Anulează</button>
                  <button type="button" className="btn btn-danger" onClick={handleDelete} disabled={loading}>
                    {loading ? "Stergere..." : "Sterge contul"}
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

      </div>
    </div>
  );
};

export default UserProfilePage;
