import React, { useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../utils/auth";

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState("");

  const statusFlow = {
    PENDING: ["PICKING"],
    PICKING: ["PACKED"],
    PACKED: ["SHIPPED"],
    SHIPPED: []
  };

  useEffect(() => {
    loadOrders();

    const interval = setInterval(() => {
      loadOrders();
    }, 5000);

    return () => clearInterval(interval);
  }, []);

  const loadOrders = async () => {
    try {
      const token = getToken();

      const res = await axios.get("http://localhost:8080/api/orders", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      setOrders(res.data.data || res.data);
      setError("");
    } catch (err) {
      setError("Failed to load orders");
    }
  };

  const updateStatus = async (orderId, newStatus) => {
    try {
      const token = getToken();

      await axios.put(
        `http://localhost:8080/api/orders/${orderId}/status`,
        null,
        {
          params: { status: newStatus },
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      await loadOrders();
    } catch (err) {
      alert("Failed to update order status");
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "PENDING": return "#3b82f6";
      case "PICKING": return "#f59e0b";
      case "PACKED": return "#8b5cf6";
      case "SHIPPED": return "#10b981";
      default: return "#6b7280";
    }
  };

  return (
    <div style={styles.page}>
      <h2 style={styles.title}>🛒 Orders Dashboard</h2>

      {error && <p style={styles.error}>{error}</p>}

      <div style={styles.grid}>
        {orders.length === 0 ? (
          <div style={styles.empty}>
            No orders found
          </div>
        ) : (
          orders.map((o) => (
            <div key={o.id} style={styles.card}>

              {/* HEADER */}
              <div style={styles.row}>
                <h3 style={styles.orderId}>Order #{o.id}</h3>

                <span
                  style={{
                    ...styles.badge,
                    background: getStatusColor(o.status)
                  }}
                >
                  {o.status}
                </span>
              </div>

              {/* BODY */}
              <p style={styles.text}>
                Product: <b>{o.productName || "N/A"}</b>
              </p>

              {/* ACTION */}
              <div style={styles.action}>
                <select
                  value=""
                  disabled={(statusFlow[o.status] || []).length === 0}
                  onChange={(e) => updateStatus(o.id, e.target.value)}
                  style={styles.select}
                >
                  <option value="">Update Status</option>

                  {(statusFlow[o.status] || []).map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </select>
              </div>

            </div>
          ))
        )}
      </div>
    </div>
  );
};

/* 🎨 UI ONLY STYLES */
const styles = {
  page: {
    padding: "20px",
    background: "#f4f6fb",
    minHeight: "100vh"
  },

  title: {
    fontSize: "22px",
    fontWeight: "700",
    marginBottom: "20px"
  },

  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
    gap: "15px"
  },

  card: {
    background: "white",
    borderRadius: "12px",
    padding: "15px",
    boxShadow: "0 2px 10px rgba(0,0,0,0.08)"
  },

  row: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center"
  },

  orderId: {
    fontSize: "16px",
    margin: 0
  },

  badge: {
    color: "white",
    fontSize: "12px",
    padding: "5px 10px",
    borderRadius: "20px"
  },

  text: {
    marginTop: "10px",
    fontSize: "14px",
    color: "#333"
  },

  action: {
    marginTop: "15px"
  },

  select: {
    width: "100%",
    padding: "8px",
    borderRadius: "8px",
    border: "1px solid #ddd"
  },

  empty: {
    padding: "30px",
    background: "white",
    borderRadius: "10px",
    textAlign: "center"
  },

  error: {
    color: "red"
  }
};

export default Orders;