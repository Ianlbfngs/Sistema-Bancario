import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom';

export default function ClientCredentialEdit() {
    const { dniId } = useParams();
    const urlCredential = `http://localhost:8080/api/credentials/${dniId}`;
    const [apiOnline, setApiOnline] = useState(true);

    const [credential, setCredential] = useState({
        id: "",
        dniClient: "",
        user: "",
        password: ""
    });

    const { id, dniClient, user, password } = credential;

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultCredential = await axios.get(urlCredential);


                setCredential(prev => ({
                    ...prev,
                    ...resultCredential.data
                }));

            } catch (error) {
                setApiOnline(false);
                alert("Error cargando la credencial");
                console.error("Error cargando la credencial", error);
            }
        };

        fetchData();
    }, [urlCredential]);

    const onInputChangeCredential = (e) => {
        const { name, value } = e.target;
        setCredential(prev => ({
            ...prev,
            ...prev.credential,
            [name]: value

        }));

    };

    const onSubmit = async (e) => {
        e.preventDefault();
        const urlUpdateCredential = `http://localhost:8080/api/credentials/${dniId}/update`;

        try {
            await axios.put(urlUpdateCredential, credential);
            alert('Credencial actualizada correctamente');
            window.location.reload();

        } catch (error) {
            console.error(error);
            alert("Hubo un error actualizando el cliente: " + error.response.data.error);

        }

    }

    return (
        <div className='container'>
            <div className='container text-center' style={{ margin: "30px" }}>
                <h3>Editar credencial</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="IDtxt" className="form-label">Id</label>
                        <input type="text" className="form-control" id="IDtxt" name='id' disabled={true} required value={id} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="DNItxt" className="form-label">DNI asociado</label>
                        <input type="text" className="form-control" id="DNItxt" name='dni' minLength={8} maxLength={8} disabled={true} pattern='^\d+$' required value={dniClient} />
                    </div>

                </div>

                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="usuarioTxt" className="form-label">Usuario</label>
                        <input type="text" className="form-control" id="usuarioTxt" name='user' maxLength={44} required value={user} onChange={onInputChangeCredential} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="contraseñaTxt" className="form-label">Contraseña</label>
                        <input type="password" className="form-control" id="contraseñaTxt" name='password' maxLength={44} required value={password} onChange={onInputChangeCredential} />
                    </div>
                </div>

                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: No fue posible establecer conexion con el sistema bancario
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-primary" disabled={!apiOnline}>Guardar</button>
                    <Link to={`/admin/client/list`} className='btn btn-danger m-3 '>Cancelar</Link>
                </div>
            </form>
        </div>

    )
}
