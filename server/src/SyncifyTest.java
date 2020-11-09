import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SyncifyTest {
	static DatabaseReference ref;
	static DatabaseReference testUserRef1;
	static DatabaseReference testUserRef2;
	static DatabaseReference testUserRef3;
	
	public SyncifyTest(FirebaseDatabase database) {
		ref = database.getReference("users");
	}
	
	public void addUsersTest() {
		UserObject user1 = new UserObject("a", 0.1, false);
		UserObject user2 = new UserObject("b", 0.2, false);
		UserObject user3 = new UserObject("c", 0.3, false);
		
		//Map<String, UserObject> userMap1 = new HashMap<String, UserObject>();
		
		//DatabaseReference userRef1 = ref1.push();
		//userRef1.setValueAsync(user1);
		testUserRef1 = ref.push();
		testUserRef1.setValueAsync(user1);

		testUserRef2 = ref.push();
		testUserRef2.setValueAsync(user2);

		testUserRef3 = ref.push();
		testUserRef3.setValueAsync(user3);
	}
	
	public void updateUsersTest() {
		DatabaseReference testUserChildUpdate = testUserRef1.push();
		testUserChildUpdate.setValueAsync("test");
	}
	
	public void searchUsersTest() {
		
	}
	/*
	 * 
	
	
	DatabaseReference ref1 = database.getReference("users/"); 
	
	DatabaseReference userRef1 = ref1.push();
	userRef1.setValueAsync(user1);
	ref.push().setValueAsync(user2);
	ref.push().setValueAsync(user3);
	

	
	userRef1.addChildEventListener(listener);
	DatabaseReference newRef = userRef1;
	userRef1.setValueAsync(new UserObject("a", 1.0, true));
	*/
}
