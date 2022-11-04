package com.fandom.NarutoCult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic;
import ly.img.android.pesdk.assets.font.basic.FontPackBasic;
import ly.img.android.pesdk.assets.frame.basic.FramePackBasic;
import ly.img.android.pesdk.assets.overlay.basic.OverlayPackBasic;
import ly.img.android.pesdk.assets.sticker.emoticons.StickerPackEmoticons;
import ly.img.android.pesdk.assets.sticker.shapes.StickerPackShapes;
import ly.img.android.pesdk.backend.model.EditorSDKResult;
import ly.img.android.pesdk.backend.model.constant.Directory;
import ly.img.android.pesdk.backend.model.state.LoadSettings;
import ly.img.android.pesdk.backend.model.state.SaveSettings;
import ly.img.android.pesdk.backend.model.state.manager.SettingsList;
import ly.img.android.pesdk.ui.activity.EditorBuilder;
import ly.img.android.pesdk.ui.model.state.UiConfigFilter;
import ly.img.android.pesdk.ui.model.state.UiConfigFrame;
import ly.img.android.pesdk.ui.model.state.UiConfigOverlay;
import ly.img.android.pesdk.ui.model.state.UiConfigSticker;
import ly.img.android.pesdk.ui.model.state.UiConfigText;
import ly.img.android.pesdk.ui.utils.PermissionRequest;
import ly.img.android.serializer._3.IMGLYFileWriter;

public class SetUpProfileActivity extends AppCompatActivity implements PermissionRequest.Response {
    private static final String TAG = "SetUpProfileActivity";

    private TextInputLayout usernameField;
    private TextInputEditText editTextUsername;

    private AutoCompleteTextView autoCompleteTextView_character;
    private String favChar;

    private AutoCompleteTextView autoCompleteTextView_element;
    private String favElem;

    private DatabaseReference mDataRef;
    private StorageReference storageRef;

    private Toolbar toolbar;
    private ProgressBar progressBar;

    private MaterialButton btn;

    private String userId;

    private String finalUsername;

    private ImageView imageView;
    private String imageUrl;

    private Uri dpPath;

    // Important permission request for Android 6.0 and above, don't forget to add this!
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void permissionGranted() {
    }

    @Override
    public void permissionDenied() {
        /* TODO: The Permission was rejected by the user. The Editor was not opened,
         * Show a hint to the user and try again. */
        Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show();
    }

    public static int PESDK_RESULT = 1;
    public static int GALLERY_RESULT = 2;

    private SettingsList createPesdkSettingsList() {


        // Create a empty new SettingsList and apply the changes on this referance.
        SettingsList settingsList = new SettingsList();

        // If you include our asset Packs and you use our UI you also need to add them to the UI,
        // otherwise they are only available for the backend
        // See the specific feature sections of our guides if you want to know how to add our own Assets.

        settingsList.getSettingsModel(UiConfigFilter.class).setFilterList(
                FilterPackBasic.getFilterPack()
        );

        settingsList.getSettingsModel(UiConfigText.class).setFontList(
                FontPackBasic.getFontPack()
        );

        settingsList.getSettingsModel(UiConfigFrame.class).setFrameList(
                FramePackBasic.getFramePack()
        );

        settingsList.getSettingsModel(UiConfigOverlay.class).setOverlayList(
                OverlayPackBasic.getOverlayPack()
        );

        settingsList.getSettingsModel(UiConfigSticker.class).setStickerLists(
                StickerPackEmoticons.getStickerCategory(),
                StickerPackShapes.getStickerCategory()
        );

        // Set custom editor image export settings
        settingsList.getSettingsModel(SaveSettings.class)
                .setExportDir(Directory.DCIM, "Block13")
                .setExportPrefix("result_")
                .setSavePolicy(SaveSettings.SavePolicy.RETURN_ALWAYS_ONLY_OUTPUT);

        return settingsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        progressBar = findViewById(R.id.progressBar_setUp);
        progressBar.setVisibility(View.INVISIBLE);

        toolbar = findViewById(R.id.setUpToolbar);
        setSupportActionBar(toolbar);

        setBackNavigation();

        usernameField = findViewById(R.id.textField_username);
        editTextUsername = findViewById(R.id.editTextField_username);

        autoCompleteTextView_character = findViewById(R.id.autoComplete);

        autoCompleteTextView_element = findViewById(R.id.autoComplete_element);

        imageView = findViewById(R.id.login_imageView);

        btn = findViewById(R.id.enterApp);
        btn.setEnabled(false);

        usernameField.setEndIconVisible(false);

        mDataRef = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference("user_images");

        setCharacterField();

        setElementField();

        handlingImage();

        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn.setEnabled(false);
                usernameField.setEndIconVisible(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                btn.setEnabled(false);
                usernameField.setEndIconVisible(false);
                String username = s.toString().trim();
                if (!username.isEmpty()) {
                    usernameField.setCounterEnabled(true);
                    if (username.length() > 4 && username.length() <= 20) {
//                        usernameField.setHelperText("Only '_' and '.' allowed");
                        createUsername(username);
                    } else if (username.length() > 20)
                        usernameField.setError("Too lengthy");
                    else
                        usernameField.setHelperText("Too short");
                } else {
                    btn.setEnabled(false);
                    usernameField.setEndIconVisible(false);
                    usernameField.setHelperText(" ");
                }
            }
        });

    }

    private void createUsername(String username) {
        mDataRef.child("usernames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.exists()) {
                        if (postSnapshot.getValue(String.class).equals(username))
                            count = 1;
                    }
                }
                Log.d(TAG, "onDataChange: count: " + count);
                if (count == 1) {
                    usernameField.setEndIconVisible(false);
                    usernameField.setError("Username already exists");
                    usernameField.setErrorEnabled(true);
                } else {
                    {
                        finalUsername = username;
                        usernameField.setHelperText("Available");
                        usernameField.setEndIconVisible(true);
                        checkButton();
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UploadProfile upload = new UploadProfile(username, favChar, imageUrl, favElem);
                                mDataRef.child("usernames").child(userId).setValue(username);
                                mDataRef.child("user_data").child(userId).setValue(upload);
                                Intent intent1 = new Intent(SetUpProfileActivity.this, MainActivity.class);
                                startActivity(intent1);
                                Intent intent2 = new Intent("finish_launch_activity");
                                sendBroadcast(intent2);
                                finish();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SetUpProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkButton() {
        if (favChar != null && favElem != null && finalUsername != null) {
            if (!favChar.isEmpty() && !favElem.isEmpty() && !finalUsername.isEmpty())
                btn.setEnabled(true);
            else
                btn.setEnabled(false);
        }
    }

    public void setCharacterField() {
        List<String> content = Arrays.asList("Itachi", "Jiraiya", "Naruto", "Kakashi", "Orochimaru", "Sasuke", "Shikamaru", "Pain"
                , "Chouji", "Obito", "Kurama", "Gai", "Lee");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, content);
        autoCompleteTextView_character.setAdapter(adapter);
        autoCompleteTextView_character.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                favChar = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemClick: favChar: " + favChar);
                checkButton();
            }
        });
    }

    public void setElementField() {
        List<String> content = Arrays.asList("Fire", "Water", "Earth", "Lightning", "Wind");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, content);
        autoCompleteTextView_element.setAdapter(adapter);
        autoCompleteTextView_element.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                favElem = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemClick: favChar: " + favElem);
                checkButton();
            }
        });
    }

    private void setBackNavigation() {

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

    }

    public void handlingImage() {
        Glide.with(this)
                .load(R.drawable.profile_picture_blank)
                .centerCrop()
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setEnabled(false);
                openSystemGalleryToSelectAnImage();
            }
        });
    }

    private void openSystemGalleryToSelectAnImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, GALLERY_RESULT);
        } else {
            Toast.makeText(
                    this,
                    "No Gallery APP installed",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void openEditor(Uri inputImage) {
        SettingsList settingsList = createPesdkSettingsList();

        // Set input image
        settingsList.getSettingsModel(LoadSettings.class).setSource(inputImage);

        new EditorBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, PESDK_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK && requestCode == GALLERY_RESULT) {
            // Open Editor with some uri in this case with an image selected from the system gallery.
            Uri selectedImage = intent.getData();
            openEditor(selectedImage);

        } else if (resultCode == RESULT_OK && requestCode == PESDK_RESULT) {
            // Editor has saved an Image.
            EditorSDKResult data = new EditorSDKResult(intent);

            // This adds the result and source image to Android's gallery
            data.notifyGallery(EditorSDKResult.UPDATE_RESULT & EditorSDKResult.UPDATE_SOURCE);

            Log.i("PESDK", "Source image is located here " + data.getSourceUri());
            Log.i("PESDK", "Result image is located here " + data.getResultUri());

            // TODO: Do something with the result image

            dpPath = data.getResultUri();

            Glide.with(this)
                    .load(dpPath)
                    .centerCrop()
                    .into(imageView);

            new Thread(() -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    imageView.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                });
                uploadImage(dpPath);
            }).start();

            // OPTIONAL: read the latest state to save it as a serialisation
            SettingsList lastState = data.getSettingsList();
            try {
                new IMGLYFileWriter(lastState).writeJson(new File(
                        Environment.getExternalStorageDirectory(),
                        "serialisationReadyToReadWithPESDKFileReader.json"
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_CANCELED && requestCode == PESDK_RESULT) {
            // Editor was canceled
            EditorSDKResult data = new EditorSDKResult(intent);

            Uri sourceURI = data.getSourceUri();
            // TODO: Do something with the source...

        }
    }

    public void uploadImage(Uri imgPath) {

        StorageReference reference = storageRef.child(userId);
        reference.putFile(imgPath)
                .addOnCompleteListener(task -> {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        imageView.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SetUpProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        checkButton();
//                                Glide.with(SetUpProfileActivity.this)
//                                        .load(imgPath)
//                                        .centerCrop()
//                                        .into(imageView);
                    });
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrl = uri.toString();
                        Log.d(TAG, "onSuccess: imageUrl: " + imageUrl);
                    });
                }).addOnFailureListener(e -> Toast.makeText(SetUpProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }
}
