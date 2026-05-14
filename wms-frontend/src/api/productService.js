import axios from "axios";
import { getToken } from "../utils/auth";

// ✅ BASE API URL
const API = "http://localhost:8080/api/products";

// ✅ AUTH HEADER HELPER
const authHeader = () => {
  return {
    headers: {
      Authorization: `Bearer ${getToken()}`
    }
  };
};

// 📦 GET ALL PRODUCTS (WITH WAREHOUSE ID)
export const getProducts = (warehouseId) => {
  return axios.get(API, {
    params: { warehouseId },
    ...authHeader()
  });
};

// ➕ CREATE PRODUCT
export const createProduct = (data) => {
  return axios.post(API, data, authHeader());
};

// ✏️ UPDATE PRODUCT
export const updateProduct = (id, data) => {
  return axios.put(`${API}/${id}`, data, authHeader());
};

// ❌ DELETE PRODUCT
export const deleteProduct = (id) => {
  return axios.delete(`${API}/${id}`, authHeader());
};