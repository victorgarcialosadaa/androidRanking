package com.stucom.flx.VCTR_DRRN;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.stucom.flx.VCTR_DRRN.model.Player;
import com.stucom.flx.VCTR_DRRN.model.Prefs;

import java.io.File;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edName, edEmail;
    ImageView imAvatar;
    Uri photoURI;
    Player player;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Capture needed layout views
        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        imAvatar = findViewById(R.id.imAvatar);
        // All buttons to this class (see implements in the class' declaration)
        findViewById(R.id.btnGallery).setOnClickListener(this);
        findViewById(R.id.btnCamera).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
        // Instantiate player object
        player = new Player();
        prefs = new Prefs(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load player info from SharedPrefs
        prefs.loadFromPrefs(this);
        edName.setText(player.getName());
        edEmail.setText(player.getEmail());
        setAvatarImage(player.getAvatar(), false);
    }

    @Override
    public void onPause() {
        // Save player info from SharedPrefs (save changes on name and email only)
        player.setName(edName.getText().toString());
        player.setEmail(edEmail.getText().toString());
        prefs.saveToPrefs(this);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        // All buttons come here, so we decide based on their ids
        switch(view.getId()) {
            case R.id.btnDelete: deleteAvatar(); break;
            case R.id.btnCamera: getAvatarFromCamera(); break;
            case R.id.btnGallery: getAvatarFromGallery(); break;
        }
    }

    // Needed for onActivityResult()
    private static final int AVATAR_FROM_GALLERY = 1;
    private static final int AVATAR_FROM_CAMERA = 2;

    public void deleteAvatar() {
        // In this case simply clear the image by pasing null
        setAvatarImage(null, true);
    }

    public void getAvatarFromGallery() {
        // Call the Open Document intent searching for images
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, AVATAR_FROM_GALLERY);
    }

    public void getAvatarFromCamera() {
        // Prepare for storage (see FileProvider background documentation)
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Always this path
        File photo = new File(storageDir, "photo.jpg");
        try {
            boolean ok = photo.createNewFile();
            if (ok) Log.d("flx", "Overwriting image");
        } catch (IOException e) {
            Log.e("flx", "Error creating image file " + photo);
            return;
        }
        Log.d("flx", "Writing photo to " + photo);
        // Pass the photo path to the Intent and start it
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoURI = FileProvider.getUriForFile(this, "com.stucom.flx.fileProvider", photo);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, AVATAR_FROM_CAMERA);
        }
        catch (IllegalArgumentException e) {
            Log.e("flx", e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Skip cancels & errors
        if (resultCode != RESULT_OK) return;

        if (requestCode == AVATAR_FROM_GALLERY) {
            // coming from gallery, the URI is in the intent's data
            photoURI = data.getData();
        }
        // if camera, no action needed, as we set the URI when the intent was created

        // now set the avatar
        String avatar = (photoURI == null) ? null : photoURI.toString();
        setAvatarImage(avatar, true);
    }

    public void setAvatarImage(String avatar, boolean saveToSharedPreferences) {
        Log.d("flx", "PlayerAvatar = " + avatar);
        if (avatar == null) {
            // if null, set the default "unknown" avatar picture
            imAvatar.setImageResource(R.drawable.unknown);
        }
        else {
            // the URI must be valid, so we set it to the ImageView
            Uri uri = Uri.parse(avatar);
            imAvatar.setImageURI(uri);
        }
        if (!saveToSharedPreferences) return;
        // comply if a save to prefs was requested
        player.setAvatar(avatar);
        prefs.saveToPrefs(this);
    }
}
