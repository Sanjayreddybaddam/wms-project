import React, { useState } from "react";

const Dashboard = () => {
  const [selected, setSelected] = useState(null);

  const data = {
    Products: {
      icon: "📦",
      details: "Total Products: 120 | Active: 98 | Out of Stock: 5",
    },
    Warehouses: {
      icon: "🏢",
      details: "Total Warehouses: 5 | Active: 4 | Locations: Hyderabad, Delhi",
    },
    Orders: {
      icon: "📑",
      details: "Total Orders: 320 | Pending: 40 | Delivered: 250",
    },
    "Low Stock": {
      icon: "⚠️",
      details: "15 products are below threshold stock level",
    },
  };

  return (
    <div style={styles.page}>
      <h2 style={styles.title}>📊 Admin Dashboard</h2>

      <div style={styles.grid}>
        {Object.keys(data).map((key) => (
          <div
            key={key}
            style={{
              ...styles.card,
              transform: selected === key ? "scale(1.03)" : "scale(1)",
              background:
                selected === key
                  ? "linear-gradient(135deg, #e0f2fe, #f8fafc)"
                  : "white",
              boxShadow:
                selected === key
                  ? "0 8px 20px rgba(0,0,0,0.15)"
                  : "0 3px 10px rgba(0,0,0,0.08)",
            }}
            onClick={() => setSelected(key)}
            onMouseEnter={() => setSelected(key)}
            onMouseLeave={() => setSelected(null)}
          >
            <div style={styles.header}>
              <span style={styles.icon}>{data[key].icon}</span>
              <span style={styles.label}>{key}</span>
            </div>

            {selected === key && (
              <div style={styles.details}>
                {data[key].details}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

const styles = {
  page: {
    padding: "20px",
    background: "#f4f6f8",
    minHeight: "100vh",
  },

  title: {
    fontSize: "24px",
    fontWeight: "600",
    marginBottom: "20px",
  },

  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
    gap: "20px",
  },

  card: {
    padding: "20px",
    borderRadius: "14px",
    cursor: "pointer",
    transition: "all 0.3s ease",
    border: "1px solid #eee",
  },

  header: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
  },

  icon: {
    fontSize: "22px",
  },

  label: {
    fontSize: "18px",
    fontWeight: "600",
  },

  details: {
    marginTop: "12px",
    fontSize: "14px",
    color: "#444",
    lineHeight: "1.5",
  },
};

export default Dashboard;