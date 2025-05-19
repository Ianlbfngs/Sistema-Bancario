import axios from 'axios';
import React, { useEffect, useState } from 'react'

export default function Transfer() {

  const [apiOnline, setApiOnline] = useState(true);

  const [accounts, setAccounts] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const resultAccounts = await axios.get("http://localhost:8080/api/accounts/dni/" + localStorage.getItem("dni"));
        console.log(resultAccounts.data);
        setAccounts(resultAccounts.data);
      } catch (error) {
        if (error.code === "ERR_NETWORK" || error.response?.status === 500) setApiOnline(false);
        else console.error("Error al obtener las cuentas:", error);
      }
    };

    fetchData();
  }, []);

  const now = new Date();
  const pad = n => n.toString().padStart(2, '0');

  const formattedDate = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;


  const [movement, setMovement] = useState({
    movementType: { id: 1 },
    idAccount: 0,
    date: formattedDate,
    detail: "",
    amount: 0,
    cbu: ""

  })


  const { detail, amount, cbu } = movement


  const onInputChangeMovement = (e) => {
    const { name, value } = e.target;
    if (name === "accountDDL" || name === "amount") {
      setMovement(prev => ({
        ...prev,
        [name]: parseInt(value)
      }));
    } else {
      setMovement(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    console.log(movement.date);
    if (movement.idAccount === 0) {
      alert('Seleccione una cuenta');
      return;
    }
    if (amount < 1) {
      alert('transferencias mayores a 1');
      return;
    }
    const urlMovement = "http://localhost:8080/api/movements";
    try {
      await axios.post(urlMovement, movement);
      setApiOnline(true);
      alert('Transferencia realizada exitosamente');
      window.location.reload();

    } catch (error) {
      console.error(error);
      if (error.code === "ERR_NETWORK" || error.response?.status === 500) setApiOnline(false);

      alert('Hubo un error realizando la transferencia: ' + error.response?.data?.error);
    }
  }
  return (
    <div className='container'>
      <div className='container text-center' style={{ margin: "30px" }}>
        <h3>Transferencias</h3>
      </div>

      <form onSubmit={(e) => onSubmit(e)}>
        <div className="row mb-3">
          <div className="col-md-6 mx-auto">
            <label htmlFor="accountDDL" className="form-label">Cuenta origen</label>
            <select id="accountDDL" name='idAccount' className="form-control" onChange={onInputChangeMovement}>
              <option value={0}>-- Seleccione una opcion --</option>
              {Array.isArray(accounts)&&accounts.map((account, index) => (
                <option key={index} value={account.id}>{account.id}</option>
              ))}
            </select>
          </div>
        </div>
        <div className="row mb-3">
          <div className="col-md-6 mx-auto">
            <label htmlFor="cbuTxt" className="form-label">CBU destino</label>
            <input type="text" className="form-control" id="cbuTxt" name='cbu' minLength={22} maxLength={22} pattern='^\d+$' value={cbu} required onChange={onInputChangeMovement} />
          </div>
        </div>
        <div className="row mb-3">
          <div className="col-md-6 mx-auto">
            <label htmlFor="detailTxt" className="form-label">Detalle</label>
            <input type="text" className="form-control" id="detailTxt" name='detail' maxLength={44} value={detail} required onChange={onInputChangeMovement} />
          </div>
        </div>
        <div className="row mb-3">
          <div className="col-md-6 mx-auto">
            <label htmlFor="amountTxt" className="form-label">Monto</label>
            <input type="text" className="form-control" id="amountTxt" name='amount' minLength={1} pattern='^\d+$' value={amount} required onChange={onInputChangeMovement} />
          </div>
        </div>
        {!apiOnline && (
          <div className='alert alert-danger text-center'>
            Error: No fue posible establecer conexion con el sistema bancario
          </div>
        )}
        <div className='text-center'>
          <button type="submit" className="btn btn-primary" disabled={!apiOnline}>Transferir</button>
        </div>
      </form>
    </div>
  )
}
