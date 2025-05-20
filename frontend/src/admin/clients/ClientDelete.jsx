import axios from 'axios';
import React, { useState } from 'react'

export default function ClientDelete() {


  const [clientApiOnline, setClientApiOnline] = useState(true);
  const [accountApiOnline, setAccountApiOnline] = useState(true);
  const [credentialApiOnline, setCredentialApiOnline] = useState(true);



  const [dniToDelete, setDniToDelete] = useState("");

  const [showFirstButton, setShowFirstButton] = useState(true);

  const [isDisabled, setIsDisabled] = useState(false);

  const onSubmit = async (e) => {
    e.preventDefault();
    if (accountsCounter !== 0) {
      alert("El cliente no puede tener cuentas activas");
      return;
    }
    try {
      await axios.put("http://localhost:8080/api/credentials/" + dniToDelete + "/delete");
      setCredentialApiOnline(true);

      try {
        await axios.put("http://localhost:8080/api/clients/" + dniToDelete + "/delete");
        setClientApiOnline(true);
        alert('Cliente eliminado exitosamente');
        window.location.reload();
      } catch (error) {
        console.error(error);
        if (error.code === "ERR_NETWORK" || error.response?.status === 500) setClientApiOnline(false);
        else {
          alert("Hubo un error eliminando el cliente:" + error.response.data.error);
          window.location.reload();

        }
      }

    } catch (error) {
      console.error(error);
      if (error.code === "ERR_NETWORK" || error.response?.status === 500) setCredentialApiOnline(false);
      else {
        alert("Hubo un error eliminando la credencial del cliente:" + error.response.data.error);
        window.location.reload();
      }
    }

  }

  const [clientToDelete, setClientToDelete] = useState(null);
  const [accountsCounter, setAccountCounter] = useState(null);
  const loadClientToDelete = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/clients/" + dniToDelete);
      setClientApiOnline(true);
      try {
        const responseCounter = await axios.get("http://localhost:8080/api/accounts/dni/" + dniToDelete + "/count");
        setAccountApiOnline(true);
        setClientToDelete(response.data);
        setAccountCounter(responseCounter.data);
      } catch (error) {
        console.log(error);
        if (error.code === "ERR_NETWORK" || error.response?.status === 500) setAccountApiOnline(false);
        alert("Hubo un error al contar las cuentas del cliente: " + error.message);

      }

    } catch (error) {
      console.log(error);
      if (error.code === "ERR_NETWORK" || error.response?.status === 500) {
        setClientApiOnline(false);
      }
      else if (error.response.status === 404) {
        alert('No se encontro el cliente solicitado');
        window.location.reload();
      }


    }
  }
  const deleteButtonFunction = async () => {
    const input = document.getElementById("DNItxt");
    if (!input.checkValidity()) {
      input.reportValidity();
      return;
    }
    loadClientToDelete();
    setShowFirstButton(false);
    setIsDisabled(true);
  }

  function renderClient() {
    try {
      return (
        <tr key={clientToDelete.dni}>
          <th scope='row'>{clientToDelete.dni}</th>
          <td>{clientToDelete.gender.description}</td>
          <td>{clientToDelete.cuil}</td>
          <td>{clientToDelete.surname}</td>
          <td>{clientToDelete.name}</td>
          <td>{clientToDelete.dob}</td>
          <td>{clientToDelete.locality.province.description}</td>
          <td>{clientToDelete.locality.description}</td>
          <td>{clientToDelete.address}</td>
          <td>{clientToDelete.email}</td>
          <td>{clientToDelete.phoneNumber}</td>
          <td>{accountsCounter}</td>
        </tr>
      )
    } catch (error) {
      console.error("Error al cargar clientes:", error);
      return (
        <tr>
          <td colSpan="6" style={{ color: 'red' }}>Error al cargar el cliente</td>
        </tr>
      )
    }
  }

  return (
    <div className='container'>
      <div className='container text-center' style={{ margin: "30px" }}>
        <h3>Eliminar cliente</h3>
      </div>
      <form onSubmit={onSubmit}>
        <label htmlFor="DNItxt" className="form-label">DNI del cliente a eliminar</label>
        <input type="text" onKeyDown={(e)=>{if(e.key==='Enter'){e.preventDefault();}}} className="form-control" id="DNItxt" name='dniToDelete' required value={dniToDelete} pattern='^\d+$' minLength={8} maxLength={8} onChange={(e) => setDniToDelete(e.target.value)} disabled={isDisabled} />
        {showFirstButton ? (
          <button type="button" onClick={() => { deleteButtonFunction() }} className="btn btn-danger mt-3">Eliminar</button>
        ) : (
          clientToDelete ? (
            <div>
              <br></br>
              <p>¿Confirmar eliminacion?</p>
              <button type='button' onClick={() => window.location.reload()} className='btn btn-primary mt-0 m-3'>Cancelar</button> <button type='submit' className="btn btn-danger mt-0 m-3">Confirmar</button>
              <table className="table table-striped table-hover align-middle">
                <thead className='table-dark'>
                  <tr>
                    <th scope="col">DNI</th>
                    <th scope="col">Genero</th>
                    <th scope="col">CUIL</th>
                    <th scope="col">Apellido</th>
                    <th scope="col">Nombre</th>
                    <th scope="col">Fecha de nacimiento</th>
                    <th scope="col">Provincia</th>
                    <th scope="col">Localidad</th>
                    <th scope="col">Direccion</th>
                    <th scope="col">Email</th>
                    <th scope="col">Telefono</th>
                    <th scope="col">Cantidad de cuentas activas</th>
                  </tr>
                </thead>
                <tbody>
                  {renderClient()}
                </tbody>
              </table>
            </div>

          ) : (
            <p></p>
          )
        )}
        {!clientApiOnline && (
          <div className='alert alert-danger text-center'>
            Error: No fue posible establecer conexion con el servicio de CLIENTES
          </div>
        )}
        {!accountApiOnline && (
          <div className='alert alert-danger text-center'>
            Error: No fue posible establecer conexion con el servicio de CUENTAS
          </div>
        )}
        {!credentialApiOnline && (
          <div className='alert alert-danger text-center'>
            Error: No fue posible establecer conexion con el servicio de CREDENCIALES
          </div>
        )}
      </form>

    </div>

  )
}
