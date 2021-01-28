import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StorageManager {
	private File data;
	private FileWriter dataWriter;
	private FileReader dataReader;
	private BufferedReader dataBufferedReader;
	private JSONObject dataJson;

	StorageManager() throws JSONException, IOException {
		try {
			data = new File("c://temp//storeData.txt");
			if (data.createNewFile() == true) {
				dataJson = new JSONObject();
				dataJson.put("customers", new JSONArray());
				// seed the product data
				dataJson.put("products", new JSONArray(
						"[{\"quantity\":5,\"price\":\"1.1\",\"name\":\"Bluetooth Keyboard\",\"description\":\"Longer description about a Bluetooth keyboard\",\"id\":1,\"sku\":\"A\"},{\"quantity\":5,\"price\":\"2.2\",\"name\":\"Greentooth Keyboard\",\"description\":\"Longer description about a Greentooth keyboard\",\"id\":2,\"sku\":\"B\"},{\"quantity\":5,\"price\":\"3.3\",\"name\":\"Redtooth Keyboard\",\"description\":\"Longer description about a Redtooth keyboard\",\"id\":3,\"sku\":\"C\"},{\"quantity\":5,\"price\":\"4.4\",\"name\":\"Purpletooth Keyboard\",\"description\":\"Longer description about a Purpletooth keyboard\",\"id\":4,\"sku\":\"D\"},{\"quantity\":0,\"price\":\"5.5\",\"name\":\"Orangetooth Keyboard\",\"description\":\"Longer description about a Orangetooth keyboard\",\"id\":5,\"sku\":\"E\"}]"));
				updateData();
			}
		} catch (IOException e) {
			throw e;
		}
		getData();
	}

	public String updateData() {
		try {
			dataWriter = new FileWriter("c://temp//storeData.txt");
			dataWriter.write(dataJson.toString());
			dataWriter.close();
			return null;
		} catch (Throwable e) {
			return String.format("Unable to save data.|%s", e.getMessage());
		}
	}

	private JSONObject getData() {
		JSONObject result;
		try {
			dataReader = new FileReader(data);
			dataBufferedReader = new BufferedReader(dataReader);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = dataBufferedReader.readLine()) != null) {
				sb.append(line);
			}
			dataJson = new JSONObject(sb.toString());
			result = dataJson;
			dataBufferedReader.close();
			dataReader.close();
			return result;
		} catch (Throwable e) {
			return null;
		}
	}

	public JSONArray getProductData() {
		try {
			return dataJson.getJSONArray("products");
		} catch (Exception e) {
			return null;
		}
	}

	public JSONArray getCustomerData() {
		try {
			return dataJson.getJSONArray("customers");
		} catch (Exception e) {
			return null;
		}
	}

	public void updateProductQuantity(Product product) throws JSONException {
		JSONArray products = dataJson.getJSONArray("products");
		for (int i = 0; i < products.length(); ++i) {
			JSONObject productJson = products.getJSONObject(i);
			if (productJson.getInt("id") == product.getId()) {
				int newQuantity = productJson.getInt("quantity") - 1;
				productJson.remove("quantity");
				productJson.put("quantity", newQuantity);
				products.remove(i);
				products.put(productJson);
				dataJson.remove("products");
				dataJson.put("products", products);
			}
		}
	}

	public void addCustomerData(Customer customer) throws JSONException {
		JSONArray customers = dataJson.getJSONArray("customers");
		customers.put(Customer.processObject(customer));
		dataJson.remove("customers");
		dataJson.put("customers", customers);
	}

	public void addOrderData(Customer customer, Order order) throws Exception {
		JSONArray customers = getCustomerData();
		try {
			for (int i = 0; i < customers.length(); ++i) {
				JSONObject cust = customers.getJSONObject(i);
				if (cust.getInt("id") != customer.getId()) {
					continue;
				}
				JSONArray orders;
				try {
					orders = cust.getJSONArray("orders");
				} catch (Exception e) {
					orders = new JSONArray();
				}
				if (orders != null) {
					orders.put(Order.processOrder(order));
					cust.remove("orders");
					cust.put("orders", orders);
					customers.remove(i);
					customers.put(cust);
					dataJson.remove("customers");
					dataJson.put("customers", customers);
				}
				return;
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
