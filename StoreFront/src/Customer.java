import org.json.JSONObject;

public class Customer {
	private int id;
	private String name;
	private String email;

	public static Customer processObject(JSONObject customer) {
		Customer result = new Customer();
		try {
			try {
				int id = customer.getInt("id");
				result.setId(id);
			} catch (Exception e) {
				result.setId(new Long(System.currentTimeMillis()).intValue());
			}
			result.setName(customer.getString("name"));
			result.setEmail(customer.getString("email"));
			return result;
		} catch (Throwable t) {
			return null;
		}
	}

	public static JSONObject processObject(Customer customer) {
		JSONObject result = new JSONObject();
		try {
			result.put("id", customer.getId());
			result.put("name", customer.getName());
			result.put("email", customer.getEmail());
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
