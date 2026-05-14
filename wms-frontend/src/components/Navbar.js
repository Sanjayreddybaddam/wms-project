import { useNavigate } from "react-router-dom";
import { removeToken, getUser } from "../utils/auth";

const Navbar = () => {
  const navigate = useNavigate();
  const user = getUser();

  const logout = () => {
    removeToken();
    navigate("/login");
  };

  return (
    <div style={styles.nav}>
      
      {/* 🔷 LEFT SIDE */}
      <div style={styles.left}>
        <h2 style={styles.logo}>WMS</h2>
        <span style={styles.title}>Warehouse Management System</span>
      </div>

      {/* 🔶 RIGHT SIDE */}
      <div style={styles.right}>
        
        {/* 👤 USER INFO */}
        <div style={styles.userBox}>
          <span style={styles.userName}>
            {user?.username || "User"}
          </span>

          <span style={{
            ...styles.roleBadge,
            background:
              user?.role === "ADMIN" ? "#2563eb" : "#16a34a"
          }}>
            {user?.role}
          </span>
        </div>

        {/* 🚪 LOGOUT */}
        <button style={styles.logoutBtn} onClick={logout}>
          Logout
        </button>
      </div>
    </div>
  );
};

const styles = {
  nav: {
    height: "60px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "0 20px",
    background: "white",
    boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
  },

  left: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
  },

  logo: {
    margin: 0,
    color: "#2563eb",
  },

  title: {
    fontSize: "14px",
    color: "#555",
  },

  right: {
    display: "flex",
    alignItems: "center",
    gap: "15px",
  },

  userBox: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    background: "#f1f5f9",
    padding: "6px 10px",
    borderRadius: "8px",
  },

  userName: {
    fontWeight: "bold",
  },

  roleBadge: {
    padding: "4px 8px",
    borderRadius: "6px",
    color: "white",
    fontSize: "12px",
    fontWeight: "bold",
  },

  logoutBtn: {
    padding: "6px 12px",
    border: "none",
    borderRadius: "6px",
    background: "#ef4444",
    color: "white",
    cursor: "pointer",
  },
};

export default Navbar;