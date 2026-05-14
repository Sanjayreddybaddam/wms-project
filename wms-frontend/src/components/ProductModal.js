import { useState, useEffect } from "react";
import { getBins } from "../api/binService";

const ProductModal = ({ onClose, onSave, initialData }) => {
  const [bins, setBins] = useState([]);

  const [form, setForm] = useState({
    name: "",
    sku: "",
    stock: 0,
    price: 0,
    storageBinId: ""
  });

  // ✅ LOAD BINS
  useEffect(() => {
    const loadBins = async () => {
      try {
        const res = await getBins();
        setBins(res.data.data || []);
      } catch (err) {
        console.log("BIN LOAD ERROR:", err);
      }
    };

    loadBins();
  }, []);

  // ✅ POPULATE FOR EDIT
  useEffect(() => {
    if (initialData) {
      setForm({
        name: initialData.name || "",
        sku: initialData.sku || "",
        stock: initialData.stock ?? 0,
        price: initialData.price ?? 0,
        storageBinId: initialData.storageBinId || ""
      });
    } else {
      setForm({
        name: "",
        sku: "",
        stock:"",
        price: "",
        storageBinId: ""
      });
    }
  }, [initialData]);

  // ✅ INPUT CHANGE HANDLER
  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]:
        name === "price" || name === "stock"
          ? Number(value)
          : value
    }));
  };

  // ✅ SUBMIT
  const handleSubmit = (e) => {
    e.preventDefault();

    onSave({
      name: form.name,
      sku: form.sku,
      price: Number(form.price),
      stock: Number(form.stock),
      storageBinId: Number(form.storageBinId)
    });
  };

  return (
    <div style={styles.overlay}>
      <div style={styles.modal}>
        <h3>{initialData ? "Edit Product" : "Add Product"}</h3>

        <form onSubmit={handleSubmit}>
          <input
            name="name"
            placeholder="Product Name"
            value={form.name}
            onChange={handleChange}
            required
          />

          <input
            name="sku"
            placeholder="SKU"
            value={form.sku}
            onChange={handleChange}
            required
          />

          {/* STOCK */}
          <input
            name="stock"
            
            placeholder="Stock"
            value={form.stock}
            onChange={handleChange}
            required
          />

          {/* PRICE */}
          <input
            name="price"
            
            placeholder="Price"
            value={form.price}
            onChange={handleChange}
            required
          />

          {/* BIN SELECTION */}
          <select
            name="storageBinId"
            value={form.storageBinId}
            onChange={handleChange}
            required
          >
            <option value="">Select Storage Bin</option>
            {bins.map((b) => (
              <option key={b.id} value={b.id}>
                {b.binCode}
              </option>
            ))}
          </select>

          <div style={styles.actions}>
            <button type="submit">Save</button>
            <button type="button" onClick={onClose}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

const styles = {
  overlay: {
    position: "fixed",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    background: "rgba(0,0,0,0.5)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center"
  },
  modal: {
    background: "white",
    padding: "20px",
    borderRadius: "10px",
    width: "320px"
  },
  actions: {
    display: "flex",
    justifyContent: "space-between",
    marginTop: "10px"
  }
};

export default ProductModal;