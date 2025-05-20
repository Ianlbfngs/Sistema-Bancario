import axios from 'axios';
import { useNavigate } from 'react-router-dom';

import React, { useState } from 'react'

export default function Login() {

    const navigate = useNavigate();

    const [apiOnline, setApiOnline] = useState(true);

    const [credential, setCredential] = useState({
        user: "",
        password: ""
    });


    const { user, password } = credential

    const onInputChangeCredential = (e) => {
        const { name, value } = e.target;
        setCredential(prev => ({
            ...prev,
            [name]: value
        }));
    };
    const onSubmit = async (e) => {
        e.preventDefault();
        try {
            const result = await axios.post("http://localhost:8080/api/credentials/login", credential);
            alert("Inicio de sesion exitoso");
            console.log(result);
            if (result.data.type === "CLIENT") {
                localStorage.setItem("dni", result.data.DNI);
                localStorage.setItem("rol", "client");
                navigate("/client/menu");
            } else {
                navigate("/admin/menu");
                localStorage.setItem("rol", "admin");
            }
        } catch (error) {
            console.log(error);
            if (error.code === "ERR_NETWORK" || error.response?.status === 500) setApiOnline(false);
            else if (error.response?.status === 404) alert("Credenciales incorrectas");

        }

    }

    return (
        <div className='container'>
            <div className='container text-center' style={{ margin: "30px" }}>
                <h3>Iniciar sesión</h3>
            </div>
            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="userTxt" className="form-label">Usuario</label>
                        <input type="text" className="form-control" id="userTxt" name='user' minLength={1} maxLength={45} required value={user} onChange={onInputChangeCredential} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="passwordTxt" className="form-label">Contraseña</label>
                        <input type="password" className="form-control" id="passwordTxt" name='password' minLength={1} maxLength={45} required value={password} onChange={onInputChangeCredential} />
                    </div>
                </div>
                <div className='text-center'>
                    <button type="submit" className="btn btn-primary" disabled={!apiOnline}>Ingresar</button>
                </div>
            </form>
            {!apiOnline && (
                <div className='alert alert-danger text-center'>
                    Error: No fue posible establecer conexion con el sistema bancario
                </div>
            )}
        </div>
    )
}
