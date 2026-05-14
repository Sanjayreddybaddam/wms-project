import { useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../utils/auth";

const Warehouses = () => {
  const [warehouses, setWarehouses] = useState([]);

  useEffect(() => {
    loadWarehouses();
  }, []);

  const loadWarehouses = async () => {
    try {
      const token = getToken();

      const res = await axios.get(
        "http://localhost:8080/api/warehouses",
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      console.log("WAREHOUSE API RESPONSE:", res.data);

      // supports ApiResponse wrapper OR direct list
      setWarehouses(res.data.data || res.data || []);

    } catch (err) {
      console.log("WAREHOUSE ERROR:", err);
      alert("Failed to load warehouses (check login/token)");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>🏢 Warehouses</h2>

      {warehouses.length === 0 ? (
        <p>No warehouses found</p>
      ) : (
        <div style={styles.grid}>
          {warehouses.map((w) => (
            <div key={w.id} style={styles.card}>
              <h3>{w.name}</h3>
              <p>Bins: {w.binCount}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

const styles = {
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
    gap: "15px",
    marginTop: "20px"
  },
  card: {
    background: "white",
    padding: "15px",
    borderRadius: "10px",
    boxShadow: "0 2px 10px rgba(0,0,0,0.1)"
  }
};

export default Warehouses;