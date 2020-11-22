

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	FirebaseDatabase database;
	DatabaseReference ref;
	String userKey;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public Search() {
        super();
        try {
			initFirebase();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	private void initFirebase() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("/Users/crystal/Downloads/my-key.json");
		FirebaseOptions options = FirebaseOptions.builder()
			    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			    .setDatabaseUrl("https://syncify-bf9e2.firebaseio.com/")
			    .build();
		FirebaseApp app = FirebaseApp.initializeApp(options);
		database = FirebaseDatabase.getInstance(app);

		ref = database.getReference("users");
		
		
		ref.addChildEventListener(new ChildEventListener() {

			@Override
			public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
				UserObject u = snapshot.getValue(UserObject.class);
				userKey = snapshot.getKey();
				if(u.getGetUsers()) {
					String nameQuery = u.getUserQuery();
					Query q = database.getInstance().getReference("users").orderByChild("name").equalTo(nameQuery);
					q.addListenerForSingleValueEvent(new ValueEventListener() {

						@Override
						public void onDataChange(DataSnapshot snapshot) {
							Set<String> searchResults = new HashSet<String>();
							if(snapshot.exists()) {
								for(DataSnapshot d: snapshot.getChildren()) {
									//UserObject res = d.getValue(UserObject.class);
									//searchResults.add();
									searchResults.add(d.getKey());
									
								}
							}
							ArrayList<String> returnList = new ArrayList();
							
							for(String s: searchResults) {
								returnList.add(s);
							}
							
							ref.child(userKey).child("searchedUsers").setValueAsync(returnList, null);
						}

						@Override
						public void onCancelled(DatabaseError error) {
							// TODO Auto-generated method stub
							
						}
						
					}
					
					
					);
					ref.child(userKey).child("getUsers").setValueAsync(false);
				}
				
			}

			@Override
			public void onChildRemoved(DataSnapshot snapshot) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SyncifyTest test = new SyncifyTest(database);
		test.addUsersTest();
		test.updateUsersTest();
		test.searchUsersTest();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
