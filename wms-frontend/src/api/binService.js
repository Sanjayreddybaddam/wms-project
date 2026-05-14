import axios from "axios";
import { getToken } from "../utils/auth";

const API = "http://localhost:8080/api/storage-bins";

// AUTH HEADER
const authHeader = () => ({
  headers: {
    Authorization: `Bearer ${getToken()}`
  }
});

// GET ALL BINS
export const getBins = () => {
  return axios.get(API, authHeader());
};