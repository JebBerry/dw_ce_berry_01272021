import org.json.JSONObject;

public class Product {
	private int id;
	private String name;
	private String description;
	private String sku;
	private String price;
	private int quantity;

	public static JSONObject processObject(Product product) {
		JSONObject result = new JSONObject();
		try {
			result.put("id", product.getId());
			result.put("name", product.getName());
			result.put("description", product.getDescription());
			result.put("sku", product.getSku());
			result.put("price", product.price);
			return result;
		} catch (Throwable t) {
			return null;
		}
	}

	public static Product processObject(JSONObject product) {
		Product result = new Product();
		try {
			result.setId(product.getInt("id"));
			result.setName(product.getString("name"));
			result.setDescription(product.getString("description"));
			result.setSku(product.getString("sku"));
			result.setQuantity(product.getInt("quantity"));
			result.setPrice(product.getString("price"));
			return result;
		} catch (Throwable t) {
			return null;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
