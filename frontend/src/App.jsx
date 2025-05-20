import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Navigation from "./shared/Navigation";
import Login from "./shared/Login";
import AdminRoutes from "./routes/AdminRoutes";
import ClientRoutes from "./routes/ClientRoutes";


function App() {
  return (
    <div className="container-fluid text-center">
      <BrowserRouter>
        <Navigation />
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route exact path="/login" element={<Login />} />
          <Route path="/admin/*" element={<AdminRoutes />} />
          <Route path="/client/*" element={<ClientRoutes />} />
        </Routes>
      </BrowserRouter>

    </div>
  );
}

export default App;
