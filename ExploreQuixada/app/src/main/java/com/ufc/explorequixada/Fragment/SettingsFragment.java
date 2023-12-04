package com.ufc.explorequixada.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ufc.explorequixada.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import io.grpc.Context;

public class SettingsFragment extends Fragment {

	private EditText userName, password;
	private Button btnUpdate;
	private Button btnCancel;
	private ImageView userProfile;

	private FirebaseAuth mAuth;
	//private DatabaseReference userRef;
	//private StorageReference UserProfileImageRef;
	private FirebaseFirestore userRef;

	final static int Gallery_Pick = 1;
	String currentUserID;
	boolean isImageAdded = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container, false);

		mAuth = FirebaseAuth.getInstance();
		currentUserID = mAuth.getCurrentUser().getUid();
		userRef = FirebaseFirestore.getInstance();

		userName = (EditText) view.findViewById(R.id.setupName);
		//password = (EditText) view.findViewById(R.id.setupPassword);
		btnUpdate = (Button) view.findViewById(R.id.saveChanges);
		btnCancel = (Button) view.findViewById(R.id.cancelChanges);
		userProfile = (ImageView) view.findViewById(R.id.setProfileImage);

		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SaveInformation();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				returnProfile(new ProfileFragment());
			}
		});

		userProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent galleryIntent = new Intent();
				galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("image/*");
				getImage.launch(galleryIntent);
			}
		});

		/*userRef.addValueEventListener(new ValueEventListener(){
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if(dataSnapshot.exists()) {
					if(dataSnapshot.hasChild("profileImage")) {
						String image = dataSnapshot.child("profileImage").getValue().toString();
						userProfile.setImageResource(Integer.parseInt(image));
					} else {
						Toast.makeText(getActivity(), "Por favor, selecione uma imagem", Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});*/


		return view;
	}

	ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
		if (result.getResultCode() == Activity.RESULT_OK) {
			Intent data = result.getData();
			if (data != null && data.getData() != null) {
				Uri imageUri = data.getData();
				isImageAdded = true;

				uploadImage(imageUri, isImageAdded);
			}
		}
	});


	private void uploadImage(Uri imageUri, boolean imageAdded) {
		if(imageAdded != false && imageUri != null) {
			String downloadUrl = imageUri.toString();
			userRef.collection("users").document(currentUserID).update("profileImageUrl", downloadUrl).addOnCompleteListener(task -> {
				if(task.isSuccessful()) {
					updateImage(imageUri);
					Toast.makeText(getActivity(), "Imagem salva com sucesso", Toast.LENGTH_SHORT).show();
				} else {
					String message = task.getException().getMessage();
					Toast.makeText(getActivity(), "Erro: " + message, Toast.LENGTH_SHORT).show();
				}
			});
		}

		/*if(imageAdded != false && imageUri != null) {
			StorageReference fileRef = UserProfileImageRef.child(imageName);
			fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
				fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
					String downloadUrl = uri.toString();
					userRef.collection("users").document(currentUserID).update("profileImageUrl", downloadUrl).addOnCompleteListener(task -> {
						if(task.isSuccessful()) {
							Toast.makeText(getActivity(), "Imagem salva com sucesso", Toast.LENGTH_SHORT).show();
						} else {
							String message = task.getException().getMessage();
							Toast.makeText(getActivity(), "Erro: " + message, Toast.LENGTH_SHORT).show();
						}
					});
				});
			});
		} else {
			Toast.makeText(getActivity(), "Por favor, selecione uma imagem", Toast.LENGTH_SHORT).show();
		}*/
	}

	private void updateImage(Uri imageUri) {
		userRef.collection("users").document(currentUserID).get().addOnCompleteListener(task -> {
			if(task.isSuccessful()) {
				com.google.firebase.firestore.DocumentSnapshot snapshot = task.getResult();
				String text = snapshot.getString("profileImageUrl");
				Glide.with(requireContext()).load(text).into(userProfile);
			}
		});
	}

	//TENTATIVA DE USAR UM CROP NAS IMAGENS, IGNORAR!!
	/*ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
		if (result.isSuccessful()) {
			Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(getContext(), true));
			//Uri uri = result.getUriContent();

			//saveCroppedImage(cropped, uri);
			uploadImage(cropped);
		}
	});

	private void uploadImage(Bitmap cropped) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		cropped.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] data = baos.toByteArray();

		Task<Uri> uploadTask = UserProfileImageRef.putBytes(data).continueWithTask(task -> {
			if(!task.isSuccessful()) {
				throw task.getException();
			}

			return UserProfileImageRef.getDownloadUrl();
		}).addOnCompleteListener(task -> {
			if(task.isSuccessful()) {
				Uri downloadUri = task.getResult();
				String downloadUrl = downloadUri.toString();

			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Gallery_Pick && resultCode == Activity.RESULT_OK && data != null) {
			Uri ImageUri = data.getData();
			launchImageCropper(ImageUri);
		}
	}*/

	/*private void saveCroppedImage(Bitmap cropped, Uri uri) {
		StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");

		filePath.putFile(uri).addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				Toast.makeText(getActivity(), "Imagem salva com sucesso", Toast.LENGTH_SHORT).show();
				Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();

				result.addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						final String downloadUrl = uri.toString();

						userRef.child("profileImage").setValue(downloadUrl)
								.addOnCompleteListener(new OnCompleteListener<Void>() {
									@Override
									public void onComplete(@NonNull Task<Void> task) {
										if (task.isSuccessful()) {

											Toast.makeText(getActivity(), "Imagem salva no banco de dados", Toast.LENGTH_SHORT).show();
										} else {
											String message = task.getException().getMessage();
											Toast.makeText(getActivity(), "Erro: " + message, Toast.LENGTH_SHORT).show();
										}
									}
								});
					}
				});
			}
		});
	}


	private void launchImageCropper(Uri uri) {
		CropImageOptions cropImageOptions = new CropImageOptions();
		cropImageOptions.imageSourceIncludeGallery = true;
		cropImageOptions.imageSourceIncludeCamera = true;
		CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);
		cropImage.launch(cropImageContractOptions);
	}*/

	private void returnProfile(Fragment fragment) {
		FragmentManager fragmentManager = getParentFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.frame_layout, fragment);
		fragmentTransaction.commit();
	}

	private void SaveInformation() {

		String name = userName.getText().toString();
		//String pass = password.getText().toString();

		if(name.isEmpty()) {
			userName.setError("Campo obrigatório");
			userName.requestFocus();
			return;
		} /*else if(pass.isEmpty()) {
			password.setError("Campo obrigatório");
			password.requestFocus();
			return;
		}*/ else {
			HashMap userMap = new HashMap();
			userMap.put("username", name);
			//userMap.put("password", pass);

			userRef.collection("users").document(currentUserID).update(userMap).addOnCompleteListener(task -> {
				if(task.isSuccessful()) {
					Toast.makeText(getActivity(), "Informações atualizadas com sucesso", Toast.LENGTH_SHORT).show();
					returnProfile(new ProfileFragment());
				} else {
					Toast.makeText(getActivity(), "Erro ao atualizar informações", Toast.LENGTH_SHORT).show();
				}
			});

			/*userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
				@Override
				public void onComplete(@NonNull Task task) {
					if(task.isSuccessful()) {
						Toast.makeText(getActivity(), "Informações atualizadas com sucesso", Toast.LENGTH_SHORT).show();
						returnProfile(new ProfileFragment());
					} else {
						Toast.makeText(getActivity(), "Erro ao atualizar informações", Toast.LENGTH_SHORT).show();
					}
				}
			});*/

		}
	}
}