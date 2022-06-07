package com.hakim.diabloclan.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.hakim.diabloclan.models.UserModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;



@Service
public class AccountService {
	
	public String saveAccount(UserModel userModel) throws InterruptedException, ExecutionException {
		System.out.println(userModel.getEmail());
		System.out.println(userModel.getBattleTag());
		System.out.println(userModel.getPassword());
		System.out.println(userModel.getUserName());

		CreateRequest request = new CreateRequest()
				.setEmail(userModel.getEmail())
				.setEmailVerified(false)
				.setPassword(userModel.getPassword())
				.setDisplayName(userModel.getUserName())
				.setDisabled(false);
		
		UserRecord userRecord = null;
		try {
			userRecord = FirebaseAuth.getInstance().createUser(request);
		} catch (FirebaseAuthException e1) {
			e1.printStackTrace();
		}
		
		Firestore dbFirestore = FirestoreClient.getFirestore();
		
		Map<String, Object> docData = new HashMap<>();
		docData.put("email", userModel.getEmail());
		docData.put("password", userModel.getPassword());
		docData.put("userName", userModel.getUserName());
		docData.put("battleTag", userModel.getBattleTag());
		docData.put("uid", userRecord.getUid());

		ApiFuture<WriteResult> future = dbFirestore.collection("users").document(userRecord.getUid()).set(docData);

		return "Successfully created new user" + userRecord.getUid();
	}
	
	public boolean connection(UserModel userModel) throws InterruptedException, ExecutionException {
		UserRecord userRecord;
		try {
			userRecord = FirebaseAuth.getInstance().getUserByEmail(userModel.getEmail());
			System.out.println("Successfully fetched user data: " + userRecord.getEmail());
			
			var email2 = userRecord.getEmail();
			var uid = userRecord.getUid();
			var name = userRecord.getDisplayName();
				
			if(getDataUser(uid).getPassword().equals(userModel.getPassword())) {
				System.out.println("marche !");
				return true;
						
			} else {
				System.out.println("Marche pas !");
				return false;
			}
	
		} catch (FirebaseAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exception");
		return false;
	}
	
	public UserModel getDataUser(String uid) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		DocumentReference documentReference = dbFirestore.collection("users").document(uid);
		ApiFuture<DocumentSnapshot> future = documentReference.get();
		DocumentSnapshot document = future.get();
		
		UserRecord userRecord = null;
		try {
			userRecord = FirebaseAuth.getInstance().getUser(uid);
		} catch (FirebaseAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Iterable<CollectionReference> collections =
				dbFirestore.collection("users").document(userRecord.getEmail()).listCollections();

		int count = 0;
		for (CollectionReference collRef : collections) {
		  count ++;
		}
		UserModel userModel = null;
		if(document.exists()) {
			userModel = document.toObject(UserModel.class);
			return userModel;
		} else {
			return null;
		}
		
		
	}

}
