import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.google.firebase.database.*;

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
        // TODO Auto-generated constructor stub
        try {
			initFirebase();
		} catch (IOException e) {
			System.out.println("not supposed to happen");
			e.printStackTrace();
		}
    }

	private void initFirebase() throws IOException {
		FirebaseOptions options = FirebaseOptions.builder()
			    .setCredentials(GoogleCredentials.getApplicationDefault())
			    .setDatabaseUrl("https://syncify-b941d.firebaseio.com/")
			    .build();
		
		FirebaseApp app = FirebaseApp.initializeApp(options);
		database = FirebaseDatabase.getInstance();
		
		DatabaseReference ref = database.getReference("a/" + "b/" + "c");
		ref.setValueAsync("asdasdad");


		
		System.out.println(ref);
		

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ref.addValueEventListener(new ValueEventListener() {
			  @Override
			  public void onDataChange(DataSnapshot dataSnapshot) {
			    String val = (String) dataSnapshot.getValue();
			    /**/
			    
			    if (val == "asdasdad" ) {
			    	System.out.println("pass");
			    } else {

			    	System.out.println("fail");
			    }
			    
			    System.out.println(val);
			  }

			  @Override
			  public void onCancelled(DatabaseError databaseError) {
			    System.out.println("The read failed: " + databaseError.getCode());
			  }
			});
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
