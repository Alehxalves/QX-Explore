package com.ufc.explorequixada.Repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.explorequixada.Entity.FriendEntity;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Interface.FriendInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

public class FriendDAO implements FriendInterface {

	private final FirebaseFirestore db = FirebaseFirestore.getInstance();
	private final CollectionReference friendCollection = db.collection("users");

	public void findFriends(String username, final OnFriendFindedListener listener) {
		friendCollection
				.whereEqualTo("username", username)
				.orderBy("name", Query.Direction.DESCENDING)
				.get()
				.addOnSuccessListener(queryDocumentSnapshots -> {
					if(!queryDocumentSnapshots.isEmpty()) {
						List<FriendEntity> friends = new ArrayList<>();
						for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
							friends.add(documentSnapshot.toObject(FriendEntity.class));
						}
						if(listener != null) {
							listener.onFriendFinded(friends);
						}
						return;
					}
					if(listener != null) {
						listener.onFriendFinded(Collections.emptyList());
					}
				})
				.addOnFailureListener(e -> {
					if(listener != null) {
						listener.onFriendFinded(Collections.emptyList());
					}
				});
	}

	@Override
	public void addFriend(UserEntity user, final OnFriendAddedListener listener) {
		String name = user.getUsername();
		findFriends(name, new OnFriendFindedListener() {
			@Override
			public void onFriendFinded(List<FriendEntity> friends) {
				if(friends.isEmpty()) {
					if(listener != null) {
						listener.onFriendAdded(false);
					}
					return;
				}

				FriendEntity friend = friends.get(0);
				friendCollection.document(friend.getId()).update("friends", user.getFriends());
				if(listener != null) {
					listener.onFriendAdded(true);
				}
			}
		});
	}

	public void removeFriend(String username, final OnFriendRemovedListener listener) {
		friendCollection
				.whereEqualTo("username", username)
				.get()
				.addOnSuccessListener(queryDocumentSnapshots -> {
					if(!queryDocumentSnapshots.isEmpty()) {
						for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
							FriendEntity friend = documentSnapshot.toObject(FriendEntity.class);
							friendCollection.document(friend.getId()).delete();
							if(listener != null) {
								listener.onFriendRemoved(true);
							}
							return;
						}
					}
					if(listener != null) {
						listener.onFriendRemoved(false);
					}
				})
				.addOnFailureListener(e -> {
					if(listener != null) {
						listener.onFriendRemoved(false);
					}
				});
	}

	public void getAllFriends(final OnFriendFindedListener listener) {
		friendCollection.orderBy("name", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
			@Override
			public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
				if(error != null) {
					if(listener != null) {
						listener.onFriendFinded(Collections.emptyList());
					}
					return;
				}

				List<FriendEntity> friends = new ArrayList<>();
				for(DocumentChange dc : value.getDocumentChanges()) {
					if(dc.getType() == DocumentChange.Type.ADDED) {
						friends.add(dc.getDocument().toObject(FriendEntity.class));
					}
				}

				if(listener != null) {
					listener.onFriendFinded(friends);
				}
			}
		});
	}

	public interface OnFriendFindedListener {
		void onFriendFinded(List<FriendEntity> friends);
	}
	public interface  OnFriendAddedListener {
		void onFriendAdded(boolean isAdded);
	}
	public interface OnFriendRemovedListener {
		void onFriendRemoved(boolean isRemoved);
	}
}
