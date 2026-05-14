import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

// 🔐 Auth Pages
import Login from "./pages/Login";
import Register from "./pages/Register";

// 🧱 Layouts
import AdminLayout from "./layout/AdminLayout";
import OperatorLayout from "./layout/OperatorLayout";

// 🔒 Route Guards
import PrivateRoute from "./components/PrivateRoute";
import RoleRoute from "./components/RoleRoute";

// 📊 Dashboards
import Dashboard from "./pages/Dashboard"; // Admin
import OperatorDashboard from "./pages/OperatorDashboard";
import OperatorProducts from "./pages/OperatorProducts";
import MyOrders from "./pages/MyOrders";

// 📦 Pages
import Products from "./pages/Products";
import OrdersAdmin from "./pages/OrdersAdmin";
import Warehouses from "./pages/Warehouse";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* 🔁 Root Redirect */}
        <Route path="/" element={<Navigate to="/login" />} />

        {/* 🌐 Public Routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* ================= ADMIN ================= */}
        <Route
          path="/admin"
          element={
            <PrivateRoute>
              <RoleRoute role="ADMIN">
                <AdminLayout />
              </RoleRoute>
            </PrivateRoute>
          }
        >
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="products" element={<Products />} />
          <Route path="orders" element={<OrdersAdmin />} />
          <Route path="warehouses" element={<Warehouses />} />
        </Route>

        {/* ================= OPERATOR ================= */}
        <Route
  path="/operator"
  element={
    <PrivateRoute>
      <RoleRoute role="OPERATOR">
        <OperatorLayout />
      </RoleRoute>
    </PrivateRoute>
  }
>
          <Route path="dashboard" element={<OperatorDashboard />} />
  <Route path="products" element={<OperatorProducts />} />
  <Route path="orders" element={<MyOrders />} />
  <Route path="products" element={<Products />} />
  <Route path="orders" element={<MyOrders />} />
        </Route>

        {/* ❌ Fallback */}
        <Route path="*" element={<Navigate to="/login" />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;