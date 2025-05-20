import axios from 'axios';
import React, { useEffect, useState } from 'react'

export default function AccountAdd() {


  const urlAccountTypes = "http://localhost:8080/api/accounts/types";
  const urlClients = "http://localhost:8080/api/clients";

  const [accountTypes, setAccountTypes] = useState([]);
  const [clients, setClients] = useState([]);
  const [apiOnline, setApiOnline] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const resultClients = await axios.get(urlClients);
        const resultAccountTypes = await axios.get(urlAccountTypes);

        setAccountTypes(resultAccountTypes.data);
        setClients(resultClients.data);
      } catch (error) {
        setApiOnline(false);
        console.error("Error: ", error);
      }
    };

    fetchData();
  }, [urlClients, urlAccountTypes]);


  const [account, setAccount] = useState({
    accountType: { id: 0 },
    dniClient: "",
    creationDate: new Date().toISOString().split('T')[0],
    cbu: "",
    balance: 10000,
    active: true

  })


  const { accountType, dniClient, cbu } = account


  const onInputChangeAccount = (e) => {
    const { name, value } = e.target;
    if (name === "typeDDL") {
      setAccount(prev => ({
        ...prev,
        accountType: { id: parseInt(value) }
      }));
    } else {
      setAccount(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    if (account.dniClient === "") {
      alert('Seleccione un dni');
      return;
    }
    if (account.accountType.id === 0) {
      alert('Seleccione un tipo de cuenta');
      return;
    }
    const urlAccount = "http://localhost:8080/api/accounts";
    try {
      await axios.post(urlAccount, account);
      alert('Cuenta agregada exitosamente');
      window.location.reload();

    } catch (error) {
      console.error(error);
      alert('Hubo un error agregando la cuenta: ' + error.response.data.error);
    }
  }

  return (
    <div className='container'>
      <div className='container text-center' style={{ margin: "30px" }}>
        <h3>Agregar cuenta</h3>
      </div>

      <form onSubmit={(e) => onSubmit(e)}>
        <div className="row mb-3">
          <div className="col-md-6">
            <label htmlFor="DNIDDL" className="form-label">DNI del cliente asociado</label>
            <select id="DNIDDL" name='dniClient' className="form-control" value={dniClient} onChange={onInputChangeAccount}>
              <option value="">Seleccione el dni</option>
              {Array.isArray(clients) && clients.map((t, index) => (
                <option key={index} value={t.id}>{t.dni}</option>
              ))}
            </select>
          </div>
          <div className="col-md-6">
            <label htmlFor="cbuTxt" className="form-label">CBU</label>
            <input type="text" className="form-control" id="cbuTxt" name='cbu' minLength={22} maxLength={22} pattern='^\d+$' required value={cbu} onChange={onInputChangeAccount} />
          </div>
        </div>




        <div className="row mb-3">
          <div className="col-md-6">
            <label htmlFor="typeDDL" className="form-label">Tipo de cuenta</label>
            <select id="typeDDL" name='typeDDL' className="form-control" value={accountType.id} onChange={onInputChangeAccount}>
              <option value={0}>Seleccione el tipo de cuenta</option>
              {Array.isArray(accountTypes) &&accountTypes.map((t, index) => (
                <option key={index} value={t.id}>{t.description}</option>
              ))}
            </select>
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
