
public class UserObject {
	public String name;
	public Double timestamp;
	public Boolean isPlaying;
	public String userQuery;
	public Boolean getUsers;
	
	public UserObject() {
	
	}
	
	public UserObject(String name, double timestamp, boolean isPlaying, String userQuery, Boolean getUsers) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.timestamp = timestamp;
		this.isPlaying = isPlaying;
		this.userQuery = userQuery;
		this.getUsers = getUsers;
		
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public Double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Double timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getIsPlaying() {
		return isPlaying;
	}

	public void setIsPlaying(Boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public String getUserQuery() {
		return userQuery;
	}

	public void setUserQuery(String userQuery) {
		this.userQuery = userQuery;
	}

	public Boolean getGetUsers() {
		return getUsers;
	}

	public void setGetUsers(Boolean getUsers) {
		this.getUsers = getUsers;
	}

	
}
