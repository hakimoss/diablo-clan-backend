package com.hakim.diabloclan.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	
	PasswordEncoder passwordEncoder;
	
	public String saveAccount(UserModel userModel) throws InterruptedException, ExecutionException {
		System.out.println(userModel.getEmail());
		System.out.println(userModel.getBattleTag());
		System.out.println(userModel.getPassword());
		System.out.println(userModel.getUserName());
		
		passwordEncoder = new BCryptPasswordEncoder();
		
		String encodedPassword = passwordEncoder.encode(userModel.getPassword());
		userModel.setPassword(encodedPassword);
		
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
		docData.put("image", "1");
		docData.put("uid", userRecord.getUid());

		ApiFuture<WriteResult> future = dbFirestore.collection("users").document(userRecord.getUid()).set(docData);

		return "Successfully created new user" + userRecord.getUid();
	}
	
	public String[] connection(UserModel userModel) throws InterruptedException, ExecutionException {
		UserRecord userRecord;
		try {
			userRecord = FirebaseAuth.getInstance().getUserByEmail(userModel.getEmail());
			System.out.println("Successfully fetched user data: " + userRecord.getEmail());
			
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  

			System.out.println(userModel.getPassword());
			var email2 = userRecord.getEmail();
			var uid = userRecord.getUid();
			var name = userRecord.getDisplayName();
			
			
			Firestore dbFirestore = FirestoreClient.getFirestore();
			DocumentReference docRef = dbFirestore.collection("users").document(uid);
			ApiFuture<DocumentSnapshot> future2 = docRef.get();
			DocumentSnapshot document2 = future2.get();
						
			String dbPassword = document2.getData().get("password").toString();

			if(encoder.matches(userModel.getPassword(), dbPassword)) {
				
				if (document2.exists()) {
					 
					 System.out.println(document2.getData().get("battleTag"));
					 
					 // ajout
					 String[] userConnectionString = {
							 "true",
							 userModel.getEmail(),
							 userModel.getPassword(),
							 document2.getData().get("userName").toString(),
							 document2.getData().get("battleTag").toString(),
							 document2.getData().get("image").toString()
							 };
					 return userConnectionString;
				 } else {
					  System.out.println("No such document!");
					  String[] userConnectionString = {"false"};
					return userConnectionString;
				 }	
			} else {
				System.out.println("Marche pas !");
				String[] userConnectionString = {"false"};
				return userConnectionString;
			}
	
		} catch (FirebaseAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exception");
		String[] userConnectionString = {"false"};
		return userConnectionString;
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
