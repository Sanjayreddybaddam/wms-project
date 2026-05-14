import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import { Outlet } from "react-router-dom";

const AdminLayout = () => {
  return (
    <div style={styles.container}>
      
      <Sidebar />

      <div style={styles.main}>
        <Navbar />
        <div style={styles.content}>
          <Outlet />
        </div>
      </div>

    </div>
  );
};

const styles = {
  container: {
    display: "flex",
    minHeight: "100vh",
    fontFamily: "Arial"
  },
  main: {
    flex: 1,
    background: "#f4f6f8"
  },
  content: {
    padding: "20px"
  }
};

export default AdminLayout;