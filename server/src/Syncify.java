

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import com.google.firebase.database.ValueEventListener;

/**
 * Servlet implementation class Syncify
 */
@WebServlet("/Syncify")
public class Syncify extends HttpServlet {
	private static final long serialVersionUID = 1L;
	FirebaseDatabase database;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public Syncify() {
        super();
        try {
			initFirebase();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	private void initFirebase() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("/Users/jason/Downloads/syncify-bf9e2-firebase-adminsdk-dvlw9-4bd872eead.json");
		FirebaseOptions options = FirebaseOptions.builder()
			    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			    .setDatabaseUrl("https://syncify-bf9e2.firebaseio.com/")
			    .build();
		FirebaseApp app = FirebaseApp.initializeApp(options);
		database = FirebaseDatabase.getInstance(app);

		DatabaseReference ref = database.getReference("users");
		
		ChildEventListener listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
				// TODO Auto-generated method stub

				System.out.println(snapshot.getKey());
				//UserObject user = (UserObject) snapshot.getValue();
				//System.out.println(user.name + ": " + user.isPlaying.toString());
			}

			@Override
			public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
				// TODO Auto-generated method stub
				//UserObject user = (UserObject) snapshot.getValue();
				System.out.println(snapshot.getValue());
				
				//System.out.println(snapshot.getValue());
				//UserObject user = (UserObject) snapshot.getValue();
			}

			@Override
			public void onChildRemoved(DataSnapshot snapshot) {}
			@Override
			public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
			@Override
			public void onCancelled(DatabaseError error) {}	
		};

		ref.addChildEventListener(listener);
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
