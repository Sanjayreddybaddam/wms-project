import { useEffect, useState } from "react";
import api from "../api/axios";

const OperatorDashboard = () => {
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);

  // ✅ FETCH DATA (FIXED ONLY LOGIC)
  const fetchData = async () => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));
      const warehouseId = user?.warehouseId;

      if (!warehouseId) return;

      const productRes = await api.get("/products", {
        params: { warehouseId }
      });

      setProducts(productRes.data?.data || []);

      const orderRes = await api.get("/orders/my");
      setOrders(orderRes.data?.data || []);
    } catch (err) {
      console.log("API ERROR:", err);
    }
  };

  // ✅ AUTO REFRESH ON FOCUS (YOUR ORIGINAL LOGIC KEPT)
  useEffect(() => {
    fetchData();

    const handleFocus = () => fetchData();
    window.addEventListener("focus", handleFocus);

    return () => window.removeEventListener("focus", handleFocus);
  }, []);

  // ✅ PLACE ORDER (ONLY FIXED REFRESH)
  const placeOrder = async (productId) => {
    try {
      await api.post("/orders/place", { productId });

      fetchData(); // refresh after order
    } catch (err) {
      console.log(err);
      alert("Order failed");
    }
  };

  const inStock = products.filter(p => p.stock > 0).length;
  const outStock = products.filter(p => p.stock <= 0).length;

  return (
    <div>
      <h2 style={styles.title}>📊 Operator Dashboard</h2>

      {/* KPI (YOUR ORIGINAL UI KEPT) */}
      <div style={styles.kpiGrid}>
        <div style={styles.kpiCard}>
          <h4>Total Products</h4>
          <h2>{products.length}</h2>
        </div>

        <div style={styles.kpiCard}>
          <h4>In Stock</h4>
          <h2 style={{ color: "green" }}>{inStock}</h2>
        </div>

        <div style={styles.kpiCard}>
          <h4>Out of Stock</h4>
          <h2 style={{ color: "red" }}>{outStock}</h2>
        </div>

        <div style={styles.kpiCard}>
          <h4>My Orders</h4>
          <h2>{orders.length}</h2>
        </div>
      </div>

      {/* PRODUCTS GRID (YOUR ORIGINAL UI) */}
      <h3 style={{ marginTop: "30px" }}>Quick Order</h3>

      <div style={styles.grid}>
        {[...products]
          .sort((a, b) => b.id - a.id)
          .slice(0, 6)
          .map((p) => (
            <div key={p.id} style={styles.card}>
              <h4>{p.name}</h4>

              <p>
                <b style={{ color: p.stock > 0 ? "green" : "red" }}>
                  {p.stock > 0 ? "In Stock" : "Out of Stock"}
                </b>
              </p>

              <button
                disabled={p.stock <= 0}
                onClick={() => placeOrder(p.id)}
                style={{
                  ...styles.button,
                  background: p.stock > 0 ? "#2563eb" : "#9ca3af",
                  cursor: p.stock > 0 ? "pointer" : "not-allowed"
                }}
              >
                {p.stock > 0 ? "Order Now" : "Out of Stock"}
              </button>
            </div>
          ))}
      </div>
    </div>
  );
};

const styles = {
  title: {
    fontSize: "24px",
    marginBottom: "20px"
  },
  kpiGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(4, 1fr)",
    gap: "20px"
  },
  kpiCard: {
    background: "white",
    padding: "20px",
    borderRadius: "10px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(3, 1fr)",
    gap: "20px",
    marginTop: "15px"
  },
  card: {
    background: "white",
    padding: "15px",
    borderRadius: "10px",
    boxShadow: "0 2px 6px rgba(0,0,0,0.1)"
  },
  button: {
    marginTop: "10px",
    color: "white",
    border: "none",
    padding: "8px 12px",
    borderRadius: "6px"
  }
};

export default OperatorDashboard;