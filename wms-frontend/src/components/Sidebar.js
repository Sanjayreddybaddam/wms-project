import React from "react";
import { useNavigate } from "react-router-dom";

const Sidebar = () => {
  const navigate = useNavigate();
  const role = localStorage.getItem("role");

  const adminMenu = [
    { name: "Dashboard", path: "/admin/dashboard" },
    { name: "Products", path: "/admin/products" },
    { name: "Orders", path: "/admin/orders" },
    { name: "Warehouses", path: "/admin/warehouses" },
  ];

  const operatorMenu = [
    { name: "Products", path: "/operator/products" },
    { name: "My Orders", path: "/operator/orders" },
  ];

  const menu = role === "ADMIN" ? adminMenu : operatorMenu;

  return (
    <div style={styles.sidebar}>
      <h2>WMS</h2>

      {menu.map((item) => (
        <div
          key={item.name}
          style={styles.item}
          onClick={() => navigate(item.path)}
        >
          {item.name}
        </div>
      ))}
    </div>
  );
};

const styles = {
  sidebar: {
    width: "220px",
    background: "#111827",
    color: "white",
    padding: "20px",
    minHeight: "100vh"
  },
  item: {
    padding: "12px",
    marginTop: "10px",
    borderRadius: "6px",
    cursor: "pointer",
    transition: "0.2s"
  }
};

export default Sidebar;