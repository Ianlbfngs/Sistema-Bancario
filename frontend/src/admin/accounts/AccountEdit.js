import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'

export default function AccountEdit() {

  const { id } = useParams();
  const urlAccount = `http://localhost:8080/api/accounts/id/${id}`;
  const urlAccountTypes = "http://localhost:8080/api/accounts/types"
  const [accountTypes, setAccountTypes] = useState([]);
  const [apiOnline, setApiOnline] = useState(true);


  const [account, setAccount] = useState({
    accountType: { id: 0 },
    dniClient: "",
    creationDate: "",
    cbu: "",
    balance: 10000,
    active: true

  })

  const { accountType, dniClient, cbu, balance, creationDate } = account

  useEffect(() => {
    const fetchData = async () => {
      try {
        const resultAccount = await axios.get(urlAccount);
        const resultTypes = await axios.get(urlAccountTypes);

        setAccount(prev => ({
          ...prev,

          ...resultAccount.data

        }));
        setAccountTypes(resultTypes.data);
      } catch (error) {
        setApiOnline(false);
        console.error("Error cargando el cliente", error);
      }
    };

    fetchData();
  }, [urlAccount, urlAccountTypes]);


  const onInputChangeAccount = (e) => {
    const { name, value } = e.target;
    if (name === 'typeDDL') {
      setAccount(prev => ({
        ...prev,
        accountType:{
          ...prev.accountType,
          id: parseInt(value)
        }
        
      }));
    } else {
      setAccount(prev =>({
        ...prev,
        [name]:value
      }))
    }

  }

  const onSubmit = async (e) => {
    e.preventDefault();
    if (account.accountType === "") {
      alert('Seleccione un tipo de cuenta');
      return;
    }
    const urlUpdateAccount = `http://localhost:8080/api/accounts/${id}/update`;

    try {
      await axios.put(urlUpdateAccount, account);
      alert('Cuenta actualizada correctamente');
      window.location.reload();

    } catch (error) {
      console.error(error);
      alert("Hubo un error actualizando la cuenta: " + error.response.data.error);

    }

  }

  return (
    <div className='container'>
      <div className='container text-center' style={{ margin: "30px" }}>
        <h3>Editar cuenta</h3>
      </div>

      <form onSubmit={(e) => onSubmit(e)}>
        <div className="row mb-3">
          <div className="col-md-6">
            <label htmlFor="IDtxt" className="form-label">Id</label>
            <input type="text" className="form-control" id="IDtxt" name='id' disabled={true} required value={id} />
          </div>
          <div className="col-md-6">
            <label htmlFor="DNItxt" className="form-label">DNI</label>
            <input type="text" className="form-control" id="DNItxt" name='dni' minLength={8} maxLength={8} disabled={true} pattern='^\d+$' required value={dniClient} />
          </div>

        </div>
        <div className="row mb-3">
          <div className="col-md-6">
            <label htmlFor="balanceTxt" className="form-label">Balance</label>
            <input type="text" className="form-control" id="balanceTxt" name='balance' pattern='^\d+$' required value={balance} onChange={onInputChangeAccount} />
          </div>
          <div className="col-md-6">
            <label htmlFor="creationDateTxt" className="form-label">Creation date</label>
            <input type="date" className="form-control" id="creationDateTxt" name='creationDate' disabled={true} required value={creationDate} onChange={onInputChangeAccount} />
          </div>
        </div>
        <div className="row mb-3">
          <div className="col-md-6">
            <label htmlFor="cbuTxt" className="form-label">CBU</label>
            <input type="text" className="form-control" id="cbuTxt" name='cbu' minLength={22} maxLength={22} pattern='^\d+$' required value={cbu} onChange={onInputChangeAccount} />
          </div>
          <div className="col-md-6">
            <label htmlFor="typeDDL" className="form-label">Tipo de cuenta</label>
            <select id="typeDDL" name='typeDDL' className="form-control" value={accountType.id} onChange={onInputChangeAccount}>
              <option value="">Seleccione el tipo de cuenta</option>
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
          <button type="submit" className="btn btn-primary" disabled={!apiOnline}>Guardar</button>
          <Link to={`/admin/account/list`} className='btn btn-danger m-3 '>Cancelar</Link>
        </div>
      </form>
    </div>
  )
}
