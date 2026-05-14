import { useEffect, useState } from "react";
import {
  getProducts,
  createProduct,
  updateProduct,
  deleteProduct
} from "../api/productService";

import ProductModal from "../components/ProductModal";

const Products = () => {
  const [products, setProducts] = useState([]);
  const [open, setOpen] = useState(false);
  const [editProduct, setEditProduct] = useState(null);

  const warehouseId = 1;

  useEffect(() => {
    loadProducts();

    const handleFocus = () => loadProducts();
    window.addEventListener("focus", handleFocus);

    return () => window.removeEventListener("focus", handleFocus);
  }, []);

  const loadProducts = async () => {
    try {
      const res = await getProducts(warehouseId);
      setProducts(res.data.data || []);
    } catch (err) {
      console.log("LOAD PRODUCTS ERROR:", err);
    }
  };

  const handleSave = async (product) => {
    try {
      if (editProduct?.id) {
        await updateProduct(editProduct.id, product);
      } else {
        await createProduct(product);
      }

      setOpen(false);
      setEditProduct(null);
      await loadProducts();
    } catch (err) {
      alert("Save failed");
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteProduct(id);
      await loadProducts();
    } catch (err) {
      alert("Cannot delete product (it has order history)");
    }
  };

  return (
    <div style={styles.page}>
      {/* HEADER */}
      <div style={styles.header}>
        <h2 style={styles.title}>📦 Products</h2>

        <button onClick={() => setOpen(true)} style={styles.addBtn}>
          + Add Product
        </button>
      </div>

      {/* GRID */}
      <div style={styles.grid}>
        {products.map((p) => (
          <div key={p.id} style={styles.card}>

            {/* TOP BADGE */}
            <div style={styles.topRow}>
              <span style={styles.badge}>SKU: {p.sku}</span>

              <span
                style={{
                  ...styles.stockBadge,
                  background: (p.stock ?? 0) > 0 ? "#dcfce7" : "#fee2e2",
                  color: (p.stock ?? 0) > 0 ? "#166534" : "#991b1b"
                }}
              >
                {(p.stock ?? 0) > 0 ? "In Stock" : "Out of Stock"}
              </span>
            </div>

            {/* PRODUCT NAME */}
            <h3 style={styles.name}>{p.name}</h3>

            {/* PRICE */}
            <p style={styles.price}>₹{p.price ?? 0}</p>

            {/* STOCK */}
            <p style={styles.stockText}>
              Stock: <b>{p.stock ?? 0}</b>
            </p>

            {/* ACTIONS */}
            <div style={styles.actions}>
              <button
                style={styles.editBtn}
                onClick={() => {
                  setEditProduct(p);
                  setOpen(true);
                }}
              >
                Edit
              </button>

              <button
                style={styles.deleteBtn}
                onClick={() => handleDelete(p.id)}
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* MODAL */}
      {open && (
        <ProductModal
          onClose={() => {
            setOpen(false);
            setEditProduct(null);
          }}
          onSave={handleSave}
          initialData={editProduct}
        />
      )}
    </div>
  );
};

/* ================= UI STYLES ONLY ================= */

const styles = {
  page: {
    padding: "20px",
    background: "#f4f6f8",
    minHeight: "100vh"
  },

  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "20px"
  },

  title: {
    fontSize: "22px",
    fontWeight: "600"
  },

  addBtn: {
    padding: "10px 14px",
    background: "#111827",
    color: "white",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer"
  },

  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(240px, 1fr))",
    gap: "16px"
  },

  card: {
    background: "white",
    padding: "16px",
    borderRadius: "14px",
    boxShadow: "0 2px 10px rgba(0,0,0,0.08)",
    transition: "0.2s"
  },

  topRow: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: "10px"
  },

  badge: {
    fontSize: "12px",
    background: "#e5e7eb",
    padding: "4px 8px",
    borderRadius: "6px"
  },

  stockBadge: {
    fontSize: "11px",
    padding: "4px 8px",
    borderRadius: "20px",
    fontWeight: "600"
  },

  name: {
    fontSize: "16px",
    fontWeight: "600",
    marginBottom: "6px"
  },

  price: {
    fontSize: "15px",
    fontWeight: "bold",
    marginBottom: "6px"
  },

  stockText: {
    fontSize: "13px",
    color: "#555",
    marginBottom: "12px"
  },

  actions: {
    display: "flex",
    gap: "10px"
  },

  editBtn: {
    flex: 1,
    padding: "8px",
    border: "none",
    borderRadius: "8px",
    background: "#2563eb",
    color: "white",
    cursor: "pointer"
  },

  deleteBtn: {
    flex: 1,
    padding: "8px",
    border: "none",
    borderRadius: "8px",
    background: "#ef4444",
    color: "white",
    cursor: "pointer"
  }
};

export default Products;