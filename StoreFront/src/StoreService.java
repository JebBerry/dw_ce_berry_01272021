import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreService extends HttpServlet {
	StorageManager storageManager;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			storageManager = new StorageManager();
			String requestUrl = request.getRequestURI();
			// get products
			if (requestUrl.endsWith("/products")) {
				JSONArray products = storageManager.getProductData();
				if (products != null) {
					response.getOutputStream().println(products.toString(4));
				} else {
					JSONObject error = new JSONObject();
					error.put("message", "Unable to retrieve products");
					response.getOutputStream().println(error.toString(4));
					return;
				}
				// get orders
			} else {
				String[] params = requestUrl.split("/");
				int userId = Integer.parseInt(params[4]);
				JSONArray customers = storageManager.getCustomerData();
				// get correct customer
				for (int i = 0; i < customers.length(); ++i) {
					JSONObject customer = customers.getJSONObject(i);
					if (customer.getInt("id") != userId) {
						continue;
					}
					// get all of their orders
					JSONArray orders = customer.getJSONArray("orders");
					if (orders != null) {
						response.getOutputStream().println(customer.getJSONArray("orders").toString(4));
					} else {
						response.getOutputStream().println("{}");
					}
					return;
				}
			}
		} catch (Exception e) {
			JSONObject error = new JSONObject();
			try {
				error.put("message", e.getMessage());
				response.getOutputStream().println(error.toString(4));
			} catch (JSONException je) {
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			storageManager = new StorageManager();
			String requestUrl = request.getRequestURI();
			StringBuffer body = new StringBuffer();
			String line = null;
			JSONObject jsonBody = new JSONObject();
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				body.append(line);
			}
			jsonBody = new JSONObject(body.toString());
			// post new customer
			if (requestUrl.endsWith("/customers")) {
				Customer newCustomer = Customer.processObject(jsonBody);
				storageManager.addCustomerData(newCustomer);
				response.getOutputStream().println(Customer.processObject(newCustomer).toString(4));
				// post new order
			} else {
				Order order = new Order();
				String productSku = jsonBody.getString("sku");
				JSONArray products = storageManager.getProductData();
				// find the matching product
				for (int i = 0; i < products.length(); ++i) {
					JSONObject productJson = products.getJSONObject(i);
					Product product = Product.processObject(productJson);
					if (product.getSku().equals(productSku)) {
						if (product.getQuantity() > 0) {
							// create an order if there is stock
							order.setCreationDate(new Date());
							order.setId(new Long(System.currentTimeMillis()).intValue());
							order.setProductSku(productSku);
							order.setTotal(product.getPrice());
							storageManager.updateProductQuantity(product);
							break;
						} else {
							JSONObject error = new JSONObject();
							try {
								error.put("message", String.format("Sorry: %s is out of stock.", product.getName()));
								response.getOutputStream().println(error.toString(4));
							} catch (JSONException je) {
							}
						}
					}
				}
				String[] params = requestUrl.split("/");
				Customer customer = new Customer();
				int userId = Integer.parseInt(params[4]);
				JSONArray customersJson = storageManager.getCustomerData();
				// find the correct customer
				for (int i = 0; i < customersJson.length(); ++i) {
					JSONObject customerJson = customersJson.getJSONObject(i);
					if (customerJson.getInt("id") != userId) {
						continue;
					}
					customer = Customer.processObject(customerJson);
					storageManager.addOrderData(customer, order);
				}
				JSONObject orderToReturn = Order.processOrder(order);
				response.getOutputStream().println(orderToReturn.toString(4));
			}
		} catch (Exception e) {
			JSONObject error = new JSONObject();
			try {
				error.put("message", e.getMessage());
				response.getOutputStream().println(error.toString(4));
			} catch (JSONException je) {
			}
		}
		storageManager.updateData();
	}
}
