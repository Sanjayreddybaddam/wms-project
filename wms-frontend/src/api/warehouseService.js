import axios from "axios";
import { getToken } from "../utils/auth";

export const getWarehouses = () => {
  return axios.get("http://localhost:8080/api/warehouses", {
    headers: {
      Authorization: `Bearer ${getToken()}`
    }
  });
};