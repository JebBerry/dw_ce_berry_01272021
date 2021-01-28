import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class Order {
	private int id;
	private String total;
	private Date creationDate;
	private String productSku;

	public static JSONObject processOrder(Order order) {
		JSONObject result = new JSONObject();
		try {
			result.put("id", order.getId());
			result.put("sku", order.getProductSku());
			result.put("total", order.getTotal());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			result.put("created_at", formatter.format(date));
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

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
}
