import React, { useEffect, useState } from "react";
import api from "../api/axios";

const MyOrders = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    let mounted = true;

    const fetchOrders = async () => {
      try {
        const res = await api.get("/orders/my");

        if (mounted) {
          setOrders(res.data?.data || []);
        }
      } catch (err) {
        console.log("FETCH ORDERS ERROR:", err);
      }
    };

    fetchOrders();

    const interval = setInterval(fetchOrders, 5000);

    return () => {
      mounted = false;
      clearInterval(interval);
    };
  }, []);

  return (
    <div style={styles.page}>
      <h2 style={styles.title}>📦 My Orders</h2>

      {orders.length === 0 ? (
        <div style={styles.emptyBox}>
          <h3>No Orders Found</h3>
          <p>You haven't placed any orders yet.</p>
        </div>
      ) : (
        <div style={styles.list}>
          {orders.map((order) => (
            <div key={order.id} style={styles.card}>
              
              {/* Header */}
              <div style={styles.header}>
                <div>
                  <h3 style={styles.productName}>{order.productName}</h3>
                  <p style={styles.orderId}>Order ID: #{order.id}</p>
                </div>

                <span
                  style={{
                    ...styles.status,
                    background: getStatusColor(order.status || "CREATED")
                  }}
                >
                  {order.status}
                </span>
              </div>

              <p style={styles.qty}>Quantity: {order.quantity || 1}</p>

              {/* TRACKING */}
              <div style={styles.tracker}>
                {["CREATED", "PICKING", "PACKED", "DISPATCHED", "DELIVERED"].map(
                  (step, index) => (
                    <div key={step} style={styles.stepWrapper}>
                      
                      <div
                        style={{
                          ...styles.circle,
                          background: isCompleted(
                            order.status || "CREATED",
                            step
                          )
                            ? "#22c55e"
                            : "#e5e7eb"
                        }}
                      >
                        {isCompleted(order.status || "CREATED", step) ? "✓" : ""}
                      </div>

                      <span style={styles.stepText}>{step}</span>

                      {index !== 4 && <div style={styles.line}></div>}
                    </div>
                  )
                )}
              </div>

            </div>
          ))}
        </div>
      )}
    </div>
  );
};

// STATUS COLOR (UNCHANGED LOGIC)
const getStatusColor = (status) => {
  switch (status) {
    case "CREATED":
      return "#3b82f6";
    case "PICKING":
      return "#f59e0b";
    case "PACKED":
      return "#f97316";
    case "DISPATCHED":
      return "#8b5cf6";
    case "DELIVERED":
      return "#10b981";
    default:
      return "#6b7280";
  }
};

const statusOrder = ["CREATED", "PICKING", "PACKED", "DISPATCHED", "DELIVERED"];

const isCompleted = (current, step) => {
  return statusOrder.indexOf(current) >= statusOrder.indexOf(step);
};

// 🎨 MODERN UI STYLES ONLY
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

  emptyBox: {
    textAlign: "center",
    padding: "40px",
    background: "white",
    borderRadius: "12px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.08)"
  },

  list: {
    display: "flex",
    flexDirection: "column",
    gap: "15px"
  },

  card: {
    background: "white",
    padding: "18px",
    borderRadius: "14px",
    boxShadow: "0 2px 10px rgba(0,0,0,0.08)"
  },

  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "8px"
  },

  productName: {
    margin: 0,
    fontSize: "18px"
  },

  orderId: {
    fontSize: "12px",
    color: "#666",
    marginTop: "4px"
  },

  qty: {
    fontSize: "14px",
    marginBottom: "12px",
    color: "#444"
  },

  status: {
    color: "white",
    padding: "6px 10px",
    borderRadius: "20px",
    fontSize: "12px",
    fontWeight: "600"
  },

  tracker: {
    display: "flex",
    alignItems: "center",
    marginTop: "15px"
  },

  stepWrapper: {
    display: "flex",
    alignItems: "center",
    flex: 1
  },

  circle: {
    width: "22px",
    height: "22px",
    borderRadius: "50%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: "12px",
    color: "white"
  },

  line: {
    height: "3px",
    background: "#e5e7eb",
    flex: 1,
    margin: "0 6px",
    borderRadius: "5px"
  },

  stepText: {
    fontSize: "10px",
    marginLeft: "5px",
    color: "#555"
  }
};

export default MyOrders;