
public class UserObject {
	public String name;
	public Long timestamp;
	public Boolean isPlaying;
	public String userQuery;
	public Boolean getUsers;
	private String accessToken;
	

	
	public UserObject(String string, double d, boolean b, String string2, boolean c) {
		// TODO Auto-generated constructor stub
	}
	
	public String accessToken() {
		return accessToken;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
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
