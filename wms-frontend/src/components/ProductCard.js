const ProductCard = ({ product }) => {
  return (
    <div style={styles.card}>
      <h3>{product.name}</h3>
      <p><strong>SKU:</strong> {product.sku}</p>
      <p>ID: {product.id}</p>
    </div>
  );
};

const styles = {
  card: {
    border: "1px solid #ddd",
    padding: "15px",
    borderRadius: "10px",
    width: "200px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
    backgroundColor: "#fff"
  }
};

export default ProductCard;