import { Navigate } from "react-router-dom";
import { getUser } from "../utils/auth";


const RoleRoute = ({ children, role }) => {
  const user = getUser();

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (user.role !== role) {
    return <Navigate to="/dashboard" />;
  }

  return children;
};

export default RoleRoute;