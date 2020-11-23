

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

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
			System.out.println("Servlet is running.");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void createUser(String accessToken, String userKey) {
    	final SpotifyApi spotifyApi = new SpotifyApi.Builder()
			    .setAccessToken(accessToken)
			    .build();
	  
    	final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile()
			    .build();
	  
	  	try {
		    final CompletableFuture<User> userFuture = getCurrentUsersProfileRequest.executeAsync();
		    final User user = userFuture.join();
		    
		    DatabaseReference userRef = database.getReference("users").child(userKey);
		    
		    DatabaseReference keyRef = userRef.child("key").getRef();
		    keyRef.setValueAsync(userKey);
		    
		    DatabaseReference isHostingRef = userRef.child("isHosting").getRef();
		    isHostingRef.setValueAsync(false);
		    
		    DatabaseReference isPlayingRef = userRef.child("isPlaying").getRef();
		    isPlayingRef.setValueAsync(false);
		    
		    DatabaseReference songUriRef = userRef.child("songUri").getRef();
		    songUriRef.setValueAsync("null");
		    
		    DatabaseReference timestampRef = userRef.child("timestamp").getRef();
		    timestampRef.setValueAsync(0);
		    
		    DatabaseReference listeningToRef = userRef.child("listeningTo").getRef();
		    listeningToRef.setValueAsync("null");
		    
		    DatabaseReference prevListeningToRef = userRef.child("prevListeningTo").getRef();
		    prevListeningToRef.setValueAsync("null");
		    
		    DatabaseReference numOtherListenersRef = userRef.child("numOtherListeners").getRef();
		    numOtherListenersRef.setValueAsync("0");
		    
		    DatabaseReference isGetUsersRef = userRef.child("isGetUsers").getRef();
		    isGetUsersRef.setValueAsync(false);
		    
		    DatabaseReference searchQuery = userRef.child("searchQuery").getRef();
		    searchQuery.setValueAsync("null");
		    
		    ArrayList<String> searchResults = new ArrayList<String>();
		    searchResults.add("spooters");
		    
		    DatabaseReference searchedUsersRef = userRef.child("isGetUsersRef").getRef();
		    searchedUsersRef.setValueAsync(searchResults);
		    
		    Map<String, Boolean> listeners = new HashMap<String, Boolean>();
		    listeners.put("spooters", true);
		    
		    DatabaseReference listenersRef = userRef.child("listeners").getRef();
		    listenersRef.setValueAsync(listeners);
		    
		    System.out.println("User found: " + user.getDisplayName());
			
		    DatabaseReference userIdRef = userRef.child("userId").getRef();
		    userIdRef.setValueAsync(user.getId());
		    
		    DatabaseReference userNameRef = userRef.child("name").getRef();
		    userNameRef.setValueAsync(user.getDisplayName());
		    
		    DatabaseReference profilePicRef = userRef.child("profilePic").getRef();
		    profilePicRef.setValueAsync(user.getImages()[0].getUrl());
	  	} catch (CompletionException e) {
		    System.out.println("Error: " + e.getCause().getMessage());
	  	} catch (CancellationException e) {
		    System.out.println("Async operation cancelled.");
	  	}
	    
	    final GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi.getListOfCurrentUsersPlaylists()
	    		.build();
	    
	    try {
	        final CompletableFuture<Paging<PlaylistSimplified>> pagingFuture = getListOfCurrentUsersPlaylistsRequest.executeAsync();
	        final Paging<PlaylistSimplified> playlistSimplifiedPaging = pagingFuture.join();
	        
	        PlaylistSimplified[] listOfCurrentPlaylists = playlistSimplifiedPaging.getItems(); 
	        
	        for (int i = 0; i < listOfCurrentPlaylists.length; i++) {
			    Map<String, String> playList = new HashMap<String, String>();
			    playList.put("name", listOfCurrentPlaylists[i].getName());
			    playList.put("id", listOfCurrentPlaylists[i].getId());
			    playList.put("imageUrl", listOfCurrentPlaylists[i].getImages()[0].getUrl());
	        	
			    DatabaseReference playListRef = database.getReference("users").child(userKey).child("playlists")
			    		.child(Integer.toString(i)).getRef();
			    playListRef.setValueAsync(playList);
	        }

	      } catch (CompletionException e) {
	        System.out.println("Error: " + e.getCause().getMessage());
	      } catch (CancellationException e) {
	        System.out.println("Async operation cancelled.");
	      }
    }
   
    
    private void addListenerToHost(String listeningTo, String userKey) {
    	DatabaseReference prevListeningToRef = database.getReference("users").child(userKey).child("prevListeningTo").getRef();
    	prevListeningToRef.setValueAsync(listeningTo);
    	DatabaseReference hostRef = database.getReference("users").child(listeningTo).child("listeners").child(userKey).getRef();
    	hostRef.setValueAsync(true);
    	
		//System.out.println("added Listener");
    }
    	
    private void removeListenerFromHost(String prevListeningTo, String userKey) {
    	DatabaseReference prevHostRef = database.getReference("users").child(prevListeningTo).child("listeners").child(userKey).getRef();
    	prevHostRef.removeValueAsync();
    	DatabaseReference prevListeningToRef = database.getReference("users").child(userKey).child("prevListeningTo").getRef();
    	prevListeningToRef.setValueAsync("null");
    	DatabaseReference numOtherListenersRef = database.getReference("users").child(userKey).child("numOtherListeners").getRef();
    	numOtherListenersRef.setValueAsync("0");
    	
		//System.out.println("removed Listener");
    }
    
    private void removeAllListenersFromHost(Map<String, Boolean> listeners, String userKey) {
    	Iterator<Entry<String, Boolean>> it = listeners.entrySet().iterator();
    	
    	while (it.hasNext()) {
    		@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
    		String listenerKey = (String) pair.getKey();
    		
    		if (listenerKey.contentEquals("spooters") == false) {
        		removeListenerFromHost(userKey, listenerKey);
            	DatabaseReference listenerListeningToRef = database.getReference("users").child(listenerKey).child("listeningTo").getRef();
            	listenerListeningToRef.setValueAsync("null");
    		}
    		
            it.remove(); 
    	}
    }
    
    private void sync(Map<String, Boolean> listeners, Boolean isPlaying, String songUri, Number timestamp, String userKey) {
    	Integer numOtherListeners = listeners.size() - 1;
    	String numOtherListenersString = Integer.toString(numOtherListeners);
    	Iterator<Entry<String, Boolean>> it = listeners.entrySet().iterator();
    	
    	while (it.hasNext()) {
    		@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
    		
    		String listenerKey = (String) pair.getKey();
    		DatabaseReference listenerIsPlayingRef = database.getReference("users").child(listenerKey).child("isPlaying").getRef();
    		listenerIsPlayingRef.setValueAsync(isPlaying);
    		DatabaseReference listenerSongUriRef = database.getReference("users").child(listenerKey).child("songUri").getRef();
    		listenerSongUriRef.setValueAsync(songUri);
    		DatabaseReference timestampRef = database.getReference("users").child(listenerKey).child("timestamp").getRef();
    		timestampRef.setValueAsync(timestamp);
    		DatabaseReference numOtherListenersRef = database.getReference("users").child(listenerKey).child("numOtherListeners").getRef();
    		numOtherListenersRef.setValueAsync(numOtherListenersString);
    		
            it.remove(); 
    	}
    }
    
    private void search(String searchQuery, String userKey) {
    	DatabaseReference isGetUsersRef = database.getReference("users").child(userKey).child("isGetUsers").getRef();
    	isGetUsersRef.setValueAsync(false);
    	
		Query q = database.getReference("users").orderByChild("name").equalTo(searchQuery);
		q.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				ArrayList<String> searchResults = new ArrayList<String>();
				if(snapshot.exists()) {
					
					for(DataSnapshot d: snapshot.getChildren()) {
						searchResults.add(d.getKey());
					}
					
				}
				
				database.getReference("users").child(userKey).child("searchedUsers").setValueAsync(searchResults, null);
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
			}
		});
		
		DatabaseReference searchQueryRef = database.getReference("users").child(userKey).child("searchQuery").getRef();
		searchQueryRef.setValueAsync("null");
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
				@SuppressWarnings("unchecked")
				Map<String, Object> user = (Map<String, Object>) snapshot.getValue();
				
				String userKey = snapshot.getKey();
				String accessToken = (String) user.get("accessToken");
				
				createUser(accessToken, userKey);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
				// TODO Auto-generated method stub
	
				Map<String, Object> user = (Map<String, Object>) snapshot.getValue();

				Map<String, Boolean> listeners = (Map<String, Boolean>) user.get("listeners");
				
				String listeningTo = (String) user.get("listeningTo");
				String prevListeningTo = (String) user.get("prevListeningTo");
				String userKey = (String) user.get("key");
				String songUri = (String) user.get("songUri");
				Number timestamp = (Number) user.get("timestamp");
				Boolean isHosting = (Boolean) user.get("isHosting");
				Boolean isPlaying = (Boolean) user.get("isPlaying");
				
				Boolean isGetUsers = (Boolean) user.get("isGetUsers");
				String searchQuery  = (String) user.get("searchQuery");
				
				if (listeningTo.contentEquals("null") == false) {
					addListenerToHost(listeningTo, userKey);
				} else {
					removeListenerFromHost(prevListeningTo, userKey);
				}
				
				if (isHosting == true) {
					sync(listeners, isPlaying, songUri, timestamp, userKey);
				} else {
					removeAllListenersFromHost(listeners, userKey);
				}
				
				if (isGetUsers == true) {
					search(searchQuery, userKey);
				}
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
		
		//SyncifyTest test = new SyncifyTest(database);
		//test.addUsersTest();
		//test.updateUsersTest();
		//test.searchUsersTest();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
