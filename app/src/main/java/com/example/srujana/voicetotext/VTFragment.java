package com.example.srujana.voicetotext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class VTFragment extends Fragment {

    ImageButton mic;
    private EditText resultText;
    Button sendsmsBtn;
    Button sendemailBtn;
    EditText txtphoneNo;
    EditText txtemail;
    Dialog match_text;
    ListView textlist;
    ArrayList<String> result;

    public VTFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_vt, container, false);//attach the fragment_vt xml file to a java View.

        //connect the visual components to my java code.
        //Use the java View v to find the following components and put them in java variables.
        resultText=(EditText)v.findViewById(R.id.VTresult);
        sendsmsBtn = (Button) v.findViewById(R.id.sms);
        sendemailBtn = (Button)v.findViewById(R.id.email);
        txtphoneNo = (EditText)v.findViewById(R.id.editText);
        txtemail = (EditText)v.findViewById(R.id.sendemail);

        //We have created a mic button which is the ImageButton and linked
        // it to the xml corresponding to this fragment.
        mic = (ImageButton)v.findViewById(R.id.imageButton);

        //Below piece of code handles the mic onclick event i.e a method
        // called promptspeechinput()is called which performs the necessary actions.
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptspeechinput(v);//call to the method which performs the speech-to-text conversion.
            }
        });


        //the following code handles the onClick of send sms button.
        sendsmsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //checks to see if the message or the phone number fields are empty. If so displays as alert to the user.
                if(txtphoneNo.getText().toString().equals("")||resultText.getText().toString().equals("")){
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Invalid Input..!!")
                            .setMessage("Please enter phone number and make sure there is message to be sent")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //do Nothing
                                }
                            })
                            .setIcon(R.drawable.ic_dialog)
                            .show();
                }
                else
                sendSMSMessage();//call to the method which performs send sms.
            }
        });

        //the following code handles the onClick of send email.
        sendemailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks to see if the email address field or the message is empty. If so displays an alert to the user.
                if(txtemail.getText().toString().equals("") || resultText.getText().toString().equals("")){
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Invalid Input..!!")
                            .setMessage("Please enter the email address and make sure there is message to be sent")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                 //do nothing.
                                }
                            })
                            .setIcon(R.drawable.ic_dialog)
                            .show();
                }
                else
                sendEmail();//call to the method which performs send email.
            }
        });
        return v;
    }

    /*Google speech input Dialog*/
    public void promptspeechinput(View view){
        if(view.getId()==R.id.imageButton){
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//initialize the recognizerintent and
                // Starts an activity that will prompt the user for speech and send it through a speech recognizer.

                //setting parameters to the intent.
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);//Informs the recognizer
                // which speech model to prefer when performing ACTION_RECOGNIZE_SPEECH and we are choosing
                // LANGUAGE_MODEL_FREE_FORM language model.

                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Start Speaking!!!");//text that appears in the small window which
                // opens up.

                i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,4);//This specifies the number of match results for the recognized word.
                // 1 indicate the most optimum or first match given by google.
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);//This specifies the language of speech.
                // We use ENGLISH as the language of speech.

                try {
                    startActivityForResult(i, 100);//we start the activity by passing the intent and a request code.
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getContext(), "Sorry! your device does not support speech language.", Toast.LENGTH_LONG).show();
                }
        }
    }

    //this method sends the speech-to-text converted message to others using the phone number.
    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
        String phoneNo = txtphoneNo.getText().toString();
        String message = resultText.getText().toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();//sending text can be done using SmsManager API.
            smsManager.sendTextMessage(phoneNo, null, message, null, null);//sendTextMessage is the method used to send text.
            Toast.makeText(getContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //this method is used to email the speech-to-text converted message by specifying the email address of the recipient.
    protected void sendEmail(){
        Log.i("Send email", "");
        String[] TO = {txtemail.getText().toString()};
        String[] CC = {""};
        String message = resultText.getText().toString();
        Log.d("the address is:::",txtemail.getText().toString());

        Intent emailIntent = new Intent(Intent.ACTION_SEND);//create an intent with ACTION_SEND.

        emailIntent.setData(Uri.parse("mailto:"));//To send an email you need to specify mailto: as URI using setData() method.
        emailIntent.setType("text/plain");//data type will be to text/plain using setType() method.

        //assigning extra data to the intent.
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);//Specifying the intented recipient.
        emailIntent.putExtra(Intent.EXTRA_CC, CC);//Specifying the whom to add in cc.
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");//Specifying the subject line.
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);//the message to be sent.

        try {
            //this is allow you to choose one of the email clients from your phone and it will have all the
            //provided fields available as below.
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }

    //Receiving speech input. This captures the result from the promptspeechinput and populates in the editable text.
    public void onActivityResult(int request_code, int result_code, Intent intent){
        super.onActivityResult(request_code, result_code, intent);
        switch(request_code){
            case 100: if(intent!=null){
                //use a dialog box to list the result of the speech
                match_text = new Dialog(getActivity());
                match_text.setContentView(R.layout.result_match);//use the xml for the dialog. Here we use a ListView in the xml
                match_text.setTitle("Select Matching Text");

                textlist = (ListView)match_text.findViewById(R.id.list);//
                //create an array called result which stores the results from speech to text conversion.
                result = intent
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                //create and set the adapter for the listview
                ArrayAdapter<String> adapter =    new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, result);
                textlist.setAdapter(adapter);

                //Upon choosing an item in the list, the item gets populated in the corresponding TextView.
                textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        resultText.setText(result.get(position));//We set the result to the resultText which is an editable text.
                        match_text.hide();
                    }
                });
                match_text.show();

            }
                break;
        }
    }

}
