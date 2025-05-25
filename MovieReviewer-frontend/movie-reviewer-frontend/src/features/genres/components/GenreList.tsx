// import React, { useEffect, useState } from 'react';
// import genreService from '../services/genreService';
// import type { Genre } from '../types';

// const GenreList: React.FC = () => {
//   const [genres, setGenres] = useState<Genre[]>([]);
//   const [newGenreName, setNewGenreName] = useState('');
//   const [errorMsg, setErrorMsg] = useState<string | null>(null);
//   const [successMsg, setSuccessMsg] = useState<string | null>(null);

//   useEffect(() => {
//     genreService.getAll()
//       .then(res => setGenres(res.data))
//       .catch(() => setErrorMsg('Eroare la încărcarea genurilor.'));
//   }, []);

//   const handleAddGenre = async (e: React.FormEvent) => {
//     e.preventDefault();
//     setErrorMsg(null);
//     setSuccessMsg(null);
//     if (!newGenreName.trim()) {
//       setErrorMsg('Numele genului nu poate fi gol.');
//       return;
//     }
//     try {
//       const response = await genreService.create({ name: newGenreName });
//       setGenres([...genres, response.data]);
//       setSuccessMsg(`Genul "${response.data.name}" a fost adăugat cu succes.`);
//       setNewGenreName('');
//     } catch (error: any) {
//       if (error.response) {
//         if (error.response.status === 400) {
//           const data = error.response.data;
//           const message = data.error || data.name || 'Eroare de validare.';
//           setErrorMsg(message);
//         } else if (error.response.status === 403) {
//           setErrorMsg('Nu aveți permisiunea de a adăuga genuri (necesar rol ADMIN).');
//         } else {
//           setErrorMsg('Eroare la adăugare. Încercați din nou.');
//         }
//       } else {
//         setErrorMsg('Eroare la adăugare. Încercați din nou.');
//       }
//     }
//   };

//   const handleDeleteGenre = async (id: number) => {
//     setErrorMsg(null);
//     setSuccessMsg(null);
//     try {
//       await genreService.delete(id);
//       setGenres(genres.filter(g => g.id !== id));
//       setSuccessMsg('Genul a fost șters.');
//     } catch (error: any) {
//       if (error.response && error.response.status === 403) {
//         setErrorMsg('Nu aveți permisiunea de a șterge genuri (necesar rol ADMIN).');
//       } else {
//         setErrorMsg('Eroare la ștergere. Încercați din nou.');
//       }
//     }
//   };

//   return (
//     <div>
//       <h2>Genuri</h2>
//       {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}
//       {successMsg && <div className="alert alert-success">{successMsg}</div>}

//       <form onSubmit={handleAddGenre} className="mb-3">
//         <div className="input-group">
//           <input 
//             type="text"
//             className="form-control"
//             placeholder="Nume gen nou"
//             value={newGenreName}
//             onChange={(e) => setNewGenreName(e.target.value)}
//           />
//           <button className="btn btn-primary" type="submit">Adaugă Gen</button>
//         </div>
//       </form>

//       <ul className="list-group">
//         {genres.map(genre => (
//           <li key={genre.id} className="list-group-item d-flex justify-content-between align-items-center">
//             {genre.name}
//             <button 
//               className="btn btn-sm btn-danger" 
//               onClick={() => handleDeleteGenre(genre.id)}
//             >
//               Șterge
//             </button>
//           </li>
//         ))}
//       </ul>
//     </div>
//   );
// };

// export default GenreList;
