package shoppinglist.model;

public class UserInfo {
	private static UserInfo userInfo;
	private String id;
	private String name;
	private String userNum;
	
	private UserInfo() {
	}
	
	private UserInfo(String id, String name, String userNum) {
		this.id = id;
		this.name = name;
		this.userNum = userNum;
	}
	
	public static void setUserInfo(String id, String name, String userNum) {
		userInfo = new UserInfo(id, name, userNum);
	}
	
	public static UserInfo getUserInfo() {
		if(userInfo == null) {
			userInfo = new UserInfo();
		}
		
		return userInfo;
	}

	public String getId() {
		return id;
	}

	public String getUserNum() {
		return userNum;
	}
}
