import { useState } from "react";
import { loginUser } from "../api/authService";
import { saveToken } from "../utils/auth";
import { useNavigate, Link } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import "../styles/auth.css";

const Login = () => {
  const [form, setForm] = useState({
    username: "",
    password: ""
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await loginUser(form);
      const token = res.data.token;

      saveToken(token);

      const decoded = jwtDecode(token);

      const user = {
        username: decoded.sub,
        role: decoded.role,
        warehouseId: decoded.warehouseId ?? 1
      };

      localStorage.setItem("user", JSON.stringify(user));
      localStorage.setItem("role", user.role);
      localStorage.setItem("warehouseId", String(user.warehouseId));

      if (user.role === "ADMIN") {
        navigate("/admin/dashboard", { replace: true });
      } else {
        navigate("/operator/dashboard", { replace: true });
      }
    } catch (err) {
      alert("Invalid credentials");
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-box">
        <h2 className="auth-title">Sign-In</h2>

        <form onSubmit={handleSubmit}>
          <label>Email</label>
          <input
            name="username"
            placeholder="Enter email"
            onChange={handleChange}
          />

          <label>Password</label>
          <input
            type="password"
            name="password"
            placeholder="Enter password"
            onChange={handleChange}
          />

          <button className="primary-btn" type="submit">
            Login
          </button>
        </form>

        <div className="auth-footer">
          New user? <Link to="/register">Create account</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;