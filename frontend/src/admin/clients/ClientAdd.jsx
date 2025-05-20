import axios from 'axios';
import React, { useEffect, useState } from 'react'

export default function ClientAdd() {



    // carga de dropdownlists
    const urlGenders = "http://localhost:8080/api/genders";
    const urlProvinces = "http://localhost:8080/api/provinces";
    const urlLocalities = "http://localhost:8080/api/localities";
    const [apiOnline, setApiOnline] = useState(true);

    const [genders, setGenders] = useState([]);
    const [provinces, setProvinces] = useState([]);
    const [localities, setLocalities] = useState([]);
    const [selectedProvince, setSelectedProvince] = useState('');

    async function checkApiStatus(serviceUrl) {
        try {
            await axios.get(serviceUrl);
        } catch (error) {
            if (error.request) {
                setApiOnline(false);
            }
        }
    }

    checkApiStatus("http://localhost:8080/api/credentials/status");
    checkApiStatus("http://localhost:8080/api/register/status");


    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultGenders = await axios.get(urlGenders);
                const resultProvinces = await axios.get(urlProvinces);
                const resultLocalities = await axios.get(urlLocalities);

                setGenders(resultGenders.data);
                setProvinces(resultProvinces.data);
                setLocalities(resultLocalities.data);
            } catch (error) {
                setApiOnline(false);
                console.error("Error:", error);
            }
        };

        fetchData();
    }, [urlGenders, urlProvinces, urlLocalities]);


    const filteredLocalities = localities.filter(locality => locality.province.id === Number(selectedProvince));

    const [clientCredential, setClientCredential] = useState({
        client: {
            dni: "",
            idLocality: "",
            idGender: "",
            cuil: "",
            surname: "",
            name: "",
            dob: "",
            address: "",
            email: "",
            phoneNumber: "",
            active: true
        },
        credential: {
            dniClient: "",
            user: "",
            password: "",
            active: true
        }
    });




    const { client, credential } = clientCredential;
    const { user, password } = credential
    const { dni, idLocality, idGender, cuil, surname, name, dob, address, email, phoneNumber } = client

    const onInputChangeDNI = (e) => {
        const value = e.target.value;
        setClientCredential(prev => ({
            ...prev,
            client: { ...prev.client, dni: value },
            credential: { ...prev.credential, dniClient: value }
        }));
    };

    const onInputChangeCredential = (e) => {
        const { name, value } = e.target;
        setClientCredential(prev => ({
            ...prev,
            credential: {
                ...prev.credential,
                [name]: value
            }
        }));
    };

    const onInputChangeClient = (e) => {
        const { name, value } = e.target;
        setClientCredential(prev => ({
            ...prev,
            client: {
                ...prev.client,
                [name]: value
            }
        }));

    };

    const onSubmit = async (e) => {
        e.preventDefault();
        if (client.idGender === 0) {
            alert('Seleccione un genero');
            return;
        }
        if (client.idLocality === 0) {
            alert('Seleccione una localidad');
            return;
        }
        const urlRegister = "http://localhost:8080/api/register";

        console.log(clientCredential);
        try {
            await axios.post(urlRegister, clientCredential);
            alert('Cliente agregado correctamente');
            window.location.reload();

        } catch (error) {
            console.error(error);
            alert("Hubo un error agregando el cliente: " + error.response.data.error);

        }

    }




    return (
        <div className='container'>
            <div className='container text-center' style={{ margin: "30px" }}>
                <h3>Agregar cliente</h3>
            </div>

            <form onSubmit={(e) => onSubmit(e)}>
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="DNItxt" className="form-label">DNI</label>
                        <input type="text" className="form-control" id="DNItxt" name='dni' minLength={8} maxLength={8} pattern='^\d+$' required value={dni} onChange={onInputChangeDNI} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="usuarioTxt" className="form-label">Usuario</label>
                        <input type="text" className="form-control" id="usuarioTxt" name='user' maxLength={44} required value={user} onChange={onInputChangeCredential} />
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="contraseñaTxt" className="form-label">Contraseña</label>
                        <input type="password" className="form-control" id="contraseñaTxt" name='password' maxLength={44} required value={password} onChange={onInputChangeCredential} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="CUILtxt" className="form-label">CUIL</label>
                        <input type="text" className="form-control" id="CUILtxt" name='cuil' minLength={11} maxLength={11} pattern='^\d+$' required value={cuil} onChange={onInputChangeClient} />
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="apellidoTxt" className="form-label">Apellido</label>
                        <input type="text" className="form-control" id="apellidoTxt" name='surname' maxLength={44} required value={surname} onChange={onInputChangeClient} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="nombreTxt" className="form-label">Nombre</label>
                        <input type="text" className="form-control" id="nombreTxt" name='name' required value={name} maxLength={44} onChange={onInputChangeClient} />
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="dobTxt" className="form-label">Fecha de nacimiento</label>
                        <input type="date" className="form-control" id="dobTxt" name='dob' min="1920-01-01" max="2006-12-31" required value={dob} onChange={onInputChangeClient} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="generoDDL" className="form-label">Género</label>
                        <select id="generoDDL" name='idGender' className="form-control" value={idGender} onChange={onInputChangeClient}>
                            <option value={0}>Seleccione su género</option>
                            {genders.map((g, index) => (
                                <option key={index} value={g.id}>{g.description}</option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="provinciaDDL" className="form-label">Provincia</label>
                        <select onChange={(e) => {setSelectedProvince(e.target.value);  setClientCredential(prev => ({ ...prev, client: { ...prev.client, idLocality: 0 } }));}} className="form-control" id='provinciaDDL' name='provinciaDDL'>
                            <option value="0">Seleccione su provincia</option>
                            {Array.isArray(provinces) && provinces.map((province, index) => (
                                <option key={index} value={province.id}>{province.description}</option>
                            ))}
                        </select>
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="localidadDDL" className="form-label">Localidad</label>
                        <select id='localidadDDL' name='idLocality' className="form-control" value={idLocality} onChange={onInputChangeClient}>
                            <option value={0} >Seleccione su localidad</option>
                            {Array.isArray(filteredLocalities) && filteredLocalities.map((locality, index) => (
                                <option key={index} value={locality.id}>{locality.description}</option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-md-6">
                        <label htmlFor="direccionTxt" className="form-label">Dirección</label>
                        <input type="text" className="form-control" id="direccionTxt" name='address' maxLength={44} required value={address} onChange={onInputChangeClient} />
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="email" className="form-label">Correo electrónico</label>
                        <input type="email" className="form-control" id="email" name='email' required value={email} maxLength={44} onChange={onInputChangeClient} />
                    </div>
                </div>

                <div className="row mb-4">
                    <div className="col-md-6">
                        <label htmlFor="telefonoTxt" className="form-label">Teléfono</label>
                        <input type="text" className="form-control" id="telefonoTxt" name='phoneNumber' maxLength={44} pattern='^\d+$' required value={phoneNumber} onChange={onInputChangeClient} />
                    </div>
                </div>

                {!apiOnline && (
                    <div className='alert alert-danger text-center'>
                        Error: No fue posible establecer conexion con el sistema bancario
                    </div>
                )}
                <div className='text-center'>
                    <button type="submit" className="btn btn-primary" disabled={!apiOnline}>Agregar</button>
                </div>
            </form>
        </div>

    )
}
