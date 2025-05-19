import axios from 'axios';
import React, { useEffect, useState } from 'react'
export default function AccountInformation() {
  const dniLogged = localStorage.getItem("dni");

  const urlClient = `http://localhost:8080/api/clients/` + dniLogged;

  const [apiOnline, setApiOnline] = useState(true);


  const [client, setClient] = useState({
    dni: "",
    localityDescription: "",
    provinceDescription: "",
    genderDescription: "",
    cuil: "",
    surname: "",
    name: "",
    dob: "",
    address: "",
    email: "",
    phoneNumber: "",
    active: true

  });

  const { dni, localityDescription, provinceDescription, genderDescription, cuil, surname, name, dob, address, email, phoneNumber } = client;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const resultClient = await axios.get(urlClient);

        setClient(prev => ({
          ...prev,
          ...resultClient.data,
          genderDescription: resultClient.data.gender.description,
          localityDescription: resultClient.data.locality.description,
          provinceDescription: resultClient.data.locality.province.description

        }));
      } catch (error) {
        setApiOnline(false);
        console.error("Error cargando el cliente", error);
      }
    };

    fetchData();
  }, [urlClient]);


  return (
    <div className='container'>
      <div className='container text-center' style={{ margin: "30px" }}>
        <h3>Informacion de cuenta</h3>
      </div>
      <div className="row mb-3">
        <div className="col-md-6">
          <label htmlFor="DNItxt" className="form-label">DNI</label>
          <input type="text" className="form-control" id="DNItxt" name='dni' value={dni} disabled/>
        </div>
        <div className="col-md-6">
          <label htmlFor="CUILtxt" className="form-label">CUIL</label>
          <input type="text" className="form-control" id="CUILtxt" name='cuil' value={cuil} disabled/>
        </div>
      </div>

      <div className="row mb-3">
        <div className="col-md-6">
          <label htmlFor="apellidoTxt" className="form-label">Apellido</label>
          <input type="text" className="form-control" id="apellidoTxt" name='surname' value={surname} disabled/>
        </div>
        <div className="col-md-6">
          <label htmlFor="nombreTxt" className="form-label">Nombre</label>
          <input type="text" className="form-control" id="nombreTxt" name='name' value={name} disabled/>
        </div>
      </div>

      <div className="row mb-3">
        <div className="col-md-6">
          <label htmlFor="dobTxt" className="form-label">Fecha de nacimiento</label>
          <input type="date" className="form-control" id="dobTxt" name='dob' value={dob} disabled/>
        </div>
        <div className="col-md-6">
          <label htmlFor="generoTxt" className="form-label">Género</label>
          <input type="text" className="form-control" id="generoTxt" name='gender' value={genderDescription} disabled/>
        </div>
      </div>

      <div className="row mb-3">
        <div className="col-md-6">
          <label htmlFor="provinciaTxt" className="form-label">Provincia</label>
          <input type="text" className="form-control" id="provinciaTxt" name='province' value={provinceDescription} disabled/>
        </div>
        <div className="col-md-6">
          <label htmlFor="localityTxt" className="form-label">Localidad</label>
          <input type="text" className="form-control" id="localityTxt" name='locality' value={localityDescription} disabled/>
        </div>
      </div>

      <div className="row mb-3">
        <div className="col-md-6">
          <label htmlFor="direccionTxt" className="form-label">Dirección</label>
          <input type="text" className="form-control" id="direccionTxt" name='address' value={address} disabled/>
        </div>
        <div className="col-md-6">
          <label htmlFor="email" className="form-label">Correo electrónico</label>
          <input type="email" className="form-control" id="email" name='email' value={email} disabled/>
        </div>
      </div>

      <div className="row mb-4">
        <div className="col-md-6">
          <label htmlFor="telefonoTxt" className="form-label">Teléfono</label>
          <input type="text" className="form-control" id="telefonoTxt" name='phoneNumber' value={phoneNumber} disabled/>
        </div>
      </div>
      {!apiOnline && (
        <div className='alert alert-danger text-center'>
          Error: No fue posible establecer conexion con el sistema bancario
        </div>
      )}
    </div>
  )
}
