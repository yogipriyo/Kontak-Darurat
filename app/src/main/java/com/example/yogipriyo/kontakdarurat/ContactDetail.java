package com.example.yogipriyo.kontakdarurat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


//universal image loader libs
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ContactDetail extends AppCompatActivity implements View.OnClickListener {

    Button QrButton,SaveButton;
    EditText NameText,BpjsText,NoIdText,AddressText,MotherText,FatherText,OfficeText,OtherText;
    String copiedStr;

    ImageLoader imgLoader;
    ImageView qrImg, FooterImg;

    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ENGLISH);
    String cDateTime=dateFormat.format(new Date());

    File image, sticker;

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        //image handling
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(config);
        qrImg = (ImageView)findViewById(R.id.qrImg);
        FooterImg = (ImageView)findViewById(R.id.FooterImg);

        // Access the Button defined in layout XML
        // and listen for it here
        QrButton = (Button) findViewById(R.id.qr_button);
        QrButton.setOnClickListener(this);
        SaveButton = (Button) findViewById(R.id.save_button);
        SaveButton.setOnClickListener(this);

        // Access the EditText defined in layout XML
        NameText = (EditText) findViewById(R.id.editname);
        NoIdText = (EditText) findViewById(R.id.editnoid);
        BpjsText = (EditText) findViewById(R.id.editbpjs);
        MotherText = (EditText) findViewById(R.id.editmother);
        FatherText = (EditText) findViewById(R.id.editfather);
        OfficeText = (EditText) findViewById(R.id.editoffice);
        OtherText = (EditText) findViewById(R.id.editother);

        //set field value if data exist
        List<Contact> contacts = db.getAllContacts();

        if(contacts!=null) {
            for (Contact cn : contacts) {
                NameText.setText(cn.getName());
                NoIdText.setText(cn.getNoId());
                BpjsText.setText(cn.getBpjs());
                MotherText.setText(cn.getMother());
                FatherText.setText(cn.getFather());
                OfficeText.setText(cn.getOffice());
                OtherText.setText(cn.getOther());
            }
        }
        //

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent a = new Intent(ContactDetail.this, About.class);
            startActivity(a);
            //return true;
        }
        if (id == R.id.action_tutorial) {
            Intent t = new Intent(ContactDetail.this, Tutorial.class);
            startActivity(t);
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String BASE_QR_URL = "http://chart.apis.google.com/chart?cht=qr&chs=400x600&chld=M&choe=UTF-8&chl=";
        String fullUrl = BASE_QR_URL;

        switch (v.getId()) {
            case R.id.qr_button:
                //generate the qr code
                if((null != NameText) && (NameText.length() > 0)){
                    try {
                        copiedStr = "Nama : "+NameText.getText().toString()+ "\n";
                        if(NoIdText.length()>0)
                            copiedStr = copiedStr+"No.KTP : "+NoIdText.getText().toString()+ "\n";
                        if(BpjsText.length()>0)
                            copiedStr = copiedStr+"No.BPJS : "+BpjsText.getText().toString()+ "\n";
                        if(MotherText.length()>0)
                            copiedStr = copiedStr+"No.Ibu/Istri : "+MotherText.getText().toString()+ "\n";
                        if(FatherText.length()>0)
                            copiedStr = copiedStr+"No.Ayah/Suami : "+FatherText.getText().toString()+ "\n";
                        if(OfficeText.length()>0)
                            copiedStr = copiedStr+"No.Kantor : "+OfficeText.getText().toString()+ "\n";
                        if(OtherText.length()>0)
                            copiedStr = copiedStr+"No.Darurat Lainnya : "+OtherText.getText().toString()+ "\n";

                        fullUrl += URLEncoder.encode(copiedStr, "UTF-8");
                        imgLoader.displayImage(fullUrl, qrImg);
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }else{ //If no text display a dialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("QRMaker")
                            .setCancelable(true)
                            .setMessage("Harap mengisi kolom yang tersedia")
                            //.setIcon(R.drawable.nuke)
                            .setNeutralButton("Ok", null);
                    AlertDialog diag = builder.create();
                    diag.show();
                }

                // Inserting Contacts
//                Log.d("Insert: ", "Inserting ..");
                db.addContact(new Contact(NameText.getText().toString(), NoIdText.getText().toString(), BpjsText.getText().toString(),MotherText.getText().toString(), FatherText.getText().toString(), OfficeText.getText().toString(), OtherText.getText().toString()));
            break;

            case R.id.save_button:
                //save the image

                BitmapDrawable drawable = (BitmapDrawable) qrImg.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                BitmapDrawable drawable2 = (BitmapDrawable) FooterImg.getDrawable();
                Bitmap bitmap2 = drawable2.getBitmap();

                ////
                Bitmap cs = combineImages(bitmap, bitmap2);
                ////


                //check external storage availability
                Boolean haveSd= android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                if(haveSd)
                {
                    // Work on device-sd
                    File sdCardDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    if (!sdCardDirectory.exists()) {
                        sdCardDirectory.mkdirs();
                    }
                    image = new File(sdCardDirectory, "qr_lockscreen.png");
                    sticker = new File(sdCardDirectory, "qr_sticker.png");
                }
                else
                {
                    //Work on device
                    //File internalCard = getFilesDir();
                    File internalCard = Environment.getDataDirectory();
                    image = new File(internalCard, "qr_lockscreen.png");
                    sticker = new File(internalCard, "qr_sticker.png");
                }

                boolean success = false;

                // Encode the file as a PNG image.
                FileOutputStream outStream;
                FileOutputStream outStream2;
                try {

                    outStream = new FileOutputStream(image);
                    outStream2 = new FileOutputStream(sticker);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    cs.compress(Bitmap.CompressFormat.PNG, 100, outStream2);

                    MediaScannerConnection.scanFile(this, new String[]{image.getPath()}, new String[]{"image/jpeg"}, null);
                    MediaScannerConnection.scanFile(this, new String[]{sticker.getPath()}, new String[]{"image/jpeg"}, null);

                    outStream.flush();
                    outStream.close();

                    outStream2.flush();
                    outStream2.close();
                    success = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (success) {
                    Toast.makeText(getApplicationContext(), "Berhasil disimpan, silahkan lihat di gallery",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Terjadi kesalahan saat penyimpanan!", Toast.LENGTH_LONG).show();
                }
            break;
        }
    }

    public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth();
            height = c.getHeight() + s.getHeight();
        } else {
            width = s.getWidth();
            height = c.getHeight() + s.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, 0f, c.getHeight(), null);

        return cs;
    }
}
