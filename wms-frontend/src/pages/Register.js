import { useState } from "react";
import { registerUser } from "../api/authService";
import { useNavigate, Link } from "react-router-dom";
import "../styles/auth.css";

const Register = () => {
  const [form, setForm] = useState({
    username: "",
    password: "",
    role: "OPERATOR"
  });

  const [errors, setErrors] = useState({});

  const navigate = useNavigate();

  // ✅ validation rules
  const validate = (field, value) => {
    let newErrors = { ...errors };

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const passwordRegex =
      /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{6,}$/;

    if (field === "username") {
      if (!value) newErrors.username = "Email is required";
      else if (!emailRegex.test(value))
        newErrors.username = "Enter valid email";
      else delete newErrors.username;
    }

    if (field === "password") {
      if (!value) newErrors.password = "Password is required";
      else if (!passwordRegex.test(value))
        newErrors.password =
          "Min 6 chars, 1 uppercase, 1 number, 1 special char";
      else delete newErrors.password;
    }

    setErrors(newErrors);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm({ ...form, [name]: value });

    validate(name, value); // 🔥 live validation
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (Object.keys(errors).length > 0) return;

    try {
      const res = await registerUser(form);
      alert(res.data.message);
      navigate("/login");
    } catch (err) {
      alert("Registration failed");
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-box">
        <h2 className="auth-title">Create Account</h2>

        <form onSubmit={handleSubmit}>
          <label>Email</label>
          <input
            name="username"
            placeholder="Enter email"
            onChange={handleChange}
          />
          {errors.username && (
            <div className="error-text">{errors.username}</div>
          )}

          <label>Password</label>
          <input
            type="password"
            name="password"
            placeholder="At least 6 characters"
            onChange={handleChange}
          />
          {errors.password && (
            <div className="error-text">{errors.password}</div>
          )}

          <label>Role</label>
          <select name="role" onChange={handleChange}>
            <option value="OPERATOR">Operator</option>
            <option value="ADMIN">Admin</option>
          </select>

          <button className="primary-btn" type="submit">
            Register
          </button>
        </form>

        <div className="auth-footer">
          Already have an account?{" "}
          <Link to="/login">Sign in</Link>
        </div>
      </div>
    </div>
  );
};

export default Register;