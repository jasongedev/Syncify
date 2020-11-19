
public class UserObject {
	public String name;
	public Double timestamp;
	public Boolean isPlaying;
	public Boolean isSearching;
	public String searchName;
	
	public UserObject() {
	
	}
	
	public UserObject(String name, double timestamp, boolean isPlaying, boolean isSearching, String searchName) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.timestamp = timestamp;
		this.isPlaying = isPlaying;
		this.isSearching = isSearching;
		this.searchName = searchName;
		
	}

	public String getName() {
		return name;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
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

	public Boolean getIsSearching() {
		return isSearching;
	}

	public void setIsSearching(Boolean isSearching) {
		this.isSearching = isSearching;
		
	}
	
}
