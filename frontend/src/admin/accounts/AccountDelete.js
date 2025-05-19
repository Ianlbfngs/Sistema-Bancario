import axios from 'axios';
import React, { useState } from 'react'

export default function AccountDelete() {


  const [accountApiOnline, setAccountApiOnline] = useState(true);

  const [idToDelete, setIdToDelete] = useState("");
  const [showFirstButton, setShowFirstButton] = useState(true);
  const [isDisabled, setIsDisabled] = useState(false);

  const onSubmit = async (e) => {
    e.preventDefault();
    if (accountToDelete.balance || accountToDelete.balance !== 0) {
      alert("No se puede eliminar una cuenta con balance positivo/negativo");
      return window.location.reload();

    }
    const urlDelete = "http://localhost:8080/api/accounts/" + idToDelete + "/delete";
    try {
      await axios.put(urlDelete);
      setAccountApiOnline(true);

      alert('Cuenta eliminada exitosamente');
    } catch (error) {
      console.error(error);
      if (error.code === "ERR_NETWORK" || error.response?.status === 500) {
        setAccountApiOnline(false);
        return;
      }
      else alert('Hubo un error al eliminar la cuenta: ' + error.response.data.error);

    }
    setIdToDelete("");
    window.location.reload();

  }

  const [accountToDelete, setAccountToDelete] = useState(null);
  const loadAccountToDelete = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/accounts/id/" + idToDelete);
      setAccountToDelete(response.data);
    } catch (error) {
      console.error(error);
      if (error.code === "ERR_NETWORK" || error.response?.status === 500) setAccountApiOnline(false);
      else {
        alert('No se encontro la cuenta');
        window.location.reload();
      }
    }
  }
  const deleteButtonFunction = async () => {
    const input = document.getElementById("idTxt");
    if (!input.checkValidity()) {
      input.reportValidity();
      return;
    }
    loadAccountToDelete();
    setShowFirstButton(false);
    setIsDisabled(true);
  }


  function renderAccount() {
    try {
      return (
        <tr key={accountToDelete.id}>
          <th scope='row'>{accountToDelete.id}</th>
          <td>{accountToDelete.accountType.description}</td>
          <td>{accountToDelete.dniClient}</td>
          <td>{accountToDelete.creationDate}</td>
          <td>{accountToDelete.cbu}</td>
          <td>{accountToDelete.balance}</td>
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
        <h3>Eliminar cuenta</h3>
      </div>
      <form onSubmit={onSubmit}>
        <label htmlFor="idTxt" className="form-label">ID de la cuenta a eliminar</label>
        <input type="text" onKeyDown={(e)=>{if(e.key==='Enter'){e.preventDefault();}}} className="form-control" id="idTxt" name='idToDelete' required value={idToDelete} pattern='^\d+$' onChange={(e) => setIdToDelete(e.target.value)} disabled={isDisabled} />
        {showFirstButton ? (
          <button type="button" onClick={() => { deleteButtonFunction() }} className="btn btn-danger mt-3">Eliminar</button>
        ) : (
          accountToDelete ? (
            <div>
              <br></br>
              <p>¿Confirmar eliminacion?</p>
              <button type='button' onClick={() => window.location.reload()} className='btn btn-primary mt-0 m-3'>Cancelar</button> <button type='submit' className="btn btn-danger mt-0 m-3">Confirmar</button>
              <table className="table table-striped table-hover align-middle">
                <thead className='table-dark'>
                  <tr>
                    <th scope="col">Id</th>
                    <th scope="col">Tipo de cuenta</th>
                    <th scope="col">DNI del cliente</th>
                    <th scope="col">Fecha de creacion</th>
                    <th scope="col">CBU</th>
                    <th scope="col">Balance</th>
                  </tr>
                </thead>
                <tbody>
                  {renderAccount()}
                </tbody>
              </table>
            </div>

          ) : (
            <p></p>
          )
        )}
        {!accountApiOnline && (
          <div className='alert alert-danger text-center'>
            Error: No fue posible establecer conexion con el servicio de CUENTAS
          </div>
        )}
      </form>
    </div>

  )
}
