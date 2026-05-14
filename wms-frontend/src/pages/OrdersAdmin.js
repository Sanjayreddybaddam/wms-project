import { useEffect, useState } from "react";
import api from "../api/axios";

const OrdersAdmin = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    try {
      const res = await api.get("/orders");
      setOrders(res.data.data || []);
    } catch (err) {
      console.log(err);
    }
  };

  const updateStatus = async (id, status) => {
    try {
      await api.put(`/orders/${id}/status`, null, {
        params: { status }
      });

      alert("Updated to " + status);
      loadOrders();
    } catch (err) {
      console.log(err);
      alert(err.response?.data?.message || "Update failed");
    }
  };

  const getNextSteps = (status) => {
    switch (status) {
      case "CREATED": return ["PICKING"];
      case "PICKING": return ["PACKED"];
      case "PACKED": return ["DISPATCHED"];
      case "DISPATCHED": return ["DELIVERED"];
      default: return [];
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "CREATED": return "#3b82f6";
      case "PICKING": return "#f59e0b";
      case "PACKED": return "#f97316";
      case "DISPATCHED": return "#8b5cf6";
      case "DELIVERED": return "#10b981";
      default: return "#6b7280";
    }
  };

  return (
    <div style={styles.page}>
      <h2 style={styles.title}>🛒 Admin Orders Panel</h2>

      {orders.length === 0 ? (
        <div style={styles.empty}>
          <h3>No orders found</h3>
        </div>
      ) : (
        <div style={styles.grid}>
          {orders.map((o) => (
            <div key={o.id} style={styles.card}>

              {/* HEADER */}
              <div style={styles.header}>
                <h3>Order #{o.id}</h3>

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
              <p style={styles.subText}>
                Product / Order tracking details here
              </p>

              {/* ACTIONS */}
              <div style={styles.actions}>
                {getNextSteps(o.status).map((next) => (
                  <button
                    key={next}
                    onClick={() => updateStatus(o.id, next)}
                    style={styles.btn}
                  >
                    Mark {next}
                  </button>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

const styles = {
  page: {
    padding: "20px",
    background: "#f4f6f8",
    minHeight: "100vh"
  },

  title: {
    fontSize: "22px",
    fontWeight: "600",
    marginBottom: "15px"
  },

  empty: {
    padding: "40px",
    background: "white",
    borderRadius: "10px",
    textAlign: "center",
    boxShadow: "0 2px 8px rgba(0,0,0,0.08)"
  },

  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))",
    gap: "15px"
  },

  card: {
    background: "white",
    padding: "15px",
    borderRadius: "12px",
    boxShadow: "0 2px 10px rgba(0,0,0,0.08)",
    transition: "0.2s"
  },

  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center"
  },

  badge: {
    color: "white",
    padding: "4px 10px",
    borderRadius: "20px",
    fontSize: "12px"
  },

  subText: {
    marginTop: "10px",
    fontSize: "13px",
    color: "#666"
  },

  actions: {
    display: "flex",
    gap: "8px",
    marginTop: "12px",
    flexWrap: "wrap"
  },

  btn: {
    padding: "6px 10px",
    border: "none",
    borderRadius: "6px",
    background: "#111827",
    color: "white",
    fontSize: "12px",
    cursor: "pointer"
  }
};

export default OrdersAdmin;