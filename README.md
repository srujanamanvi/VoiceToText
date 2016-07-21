# VoiceToText

This is an android application which converts speech to text and the text can further be sent as SMS or email.

The project contains the following main files:

1. AndroidManifest.xml : This is a basic Manifest file with required permissions.

2. MainActivity.java: This file is the basic MainActivity. Additionally, it contains code to check if 
                      required permission has access granted. If not, then make request to grant permission.

3. VTFragment: This file contains the code for the Fragment in the MainActivity. All the operations are performed
               in the fragment. It contains the code for conversion from speech to text. The layout for this
               is specified in the fragment_vt.xml file. 
               content_main.xml is the file which contains the code to include fragment in the MainActivity.

4. activity_main.xml : This is the layout file for MainActivity.java. 

5. result_match.xml : This file specifies the layout of the dialog box which is poppped after user has stopped
                      speaking. The user can choose one of the many matches and display it as TextView.


