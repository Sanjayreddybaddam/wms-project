import React, { useEffect, useState } from "react";
import api from "../api/axios";

const OperatorProducts = () => {
  const [products, setProducts] = useState([]);

  // FETCH PRODUCTS
  const fetchProducts = async () => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));
      const warehouseId = user?.warehouseId;

      if (!warehouseId) return;

      const res = await api.get("/products", {
        params: { warehouseId }
      });

      setProducts(res.data?.data || []);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    fetchProducts();

    const handleFocus = () => fetchProducts();
    window.addEventListener("focus", handleFocus);

    return () => window.removeEventListener("focus", handleFocus);
  }, []);

  // PLACE ORDER
  const placeOrder = async (productId) => {
    try {
      await api.post("/orders/place", { productId });
      alert("🎉 Order placed successfully"); 
      fetchProducts();
    } catch (err) {
      alert("Order failed");
    }
  };

  return (
    <div style={styles.page}>
      <h2 style={styles.title}>🛍️ Products</h2>

      <div style={styles.grid}>
        {products.map((p) => (
          <div key={p.id} style={styles.card}>
            
            {/* Product Image Placeholder */}
            <div style={styles.imageBox}>
              📦
            </div>

            {/* Product Name */}
            <h3 style={styles.name}>{p.name}</h3>

            {/* Price */}
            <p style={styles.price}>₹{p.price}</p>

            {/* Stock */}
            <p style={p.stock > 0 ? styles.inStock : styles.outStock}>
              {p.stock > 0 ? "In Stock" : "Out of Stock"}
            </p>

            {/* Button */}
            <button
              disabled={p.stock <= 0}
              onClick={() => placeOrder(p.id)}
              style={{
                ...styles.button,
                background: p.stock > 0 ? "#ff5722" : "#9ca3af",
                cursor: p.stock > 0 ? "pointer" : "not-allowed"
              }}
            >
              {p.stock > 0 ? "Buy Now" : "Unavailable"}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

const styles = {
  page: {
    padding: "20px",
    background: "#f1f3f6",
    minHeight: "100vh"
  },

  title: {
    fontSize: "22px",
    marginBottom: "15px",
    fontWeight: "600"
  },

  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
    gap: "15px"
  },

  card: {
    background: "white",
    borderRadius: "10px",
    padding: "12px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
    transition: "0.2s"
  },

  imageBox: {
    height: "120px",
    background: "#e0e0e0",
    borderRadius: "8px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: "40px",
    marginBottom: "10px"
  },

  name: {
    fontSize: "16px",
    fontWeight: "600",
    marginBottom: "5px"
  },

  price: {
    fontSize: "16px",
    fontWeight: "bold",
    color: "#111",
    marginBottom: "5px"
  },

  inStock: {
    color: "green",
    fontSize: "13px",
    marginBottom: "10px"
  },

  outStock: {
    color: "red",
    fontSize: "13px",
    marginBottom: "10px"
  },

  button: {
    width: "100%",
    padding: "8px",
    border: "none",
    color: "white",
    borderRadius: "6px",
    fontWeight: "600"
  }
};

export default OperatorProducts;