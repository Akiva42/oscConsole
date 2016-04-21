import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import interfascia.*; 
import at.mukprojects.console.*; 
import oscP5.*; 
import netP5.*; 
import java.net.InetAddress; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class oscConsole extends PApplet {







OscP5 oscP5;
NetAddress myRemoteLocation;
Console console;
GUIController c;
IFTextField t;
IFLabel l;
IFLabel portLabel;
IFLabel ipLabel;
InetAddress inet;

public void setup() {
  
  frameRate(25);

  /* myRemoteLocation is a NetAddress. a NetAddress takes 2 parameters,
   * an ip address and a port number. myRemoteLocation is used as parameter in
   * oscP5.send() when sending osc packets to another computer, device, 
   * application. usage see below. for testing purposes the listening port
   * and the port of the remote location address are the same, hence you will
   * send messages back to this sketch.
   */
  myRemoteLocation = new NetAddress("127.0.0.1", 12000);

  console = new Console(this);
  console.start();

  c = new GUIController(this);
  t = new IFTextField("Text Field", 25, 30, 150);
  l = new IFLabel("", 25, 70);
  ipLabel = new IFLabel("this machine's ip: ", 190, 10);
  portLabel = new IFLabel("Listen on Port #", 25, 10);
  c.add(t);
  c.add(l);
  c.add(portLabel);
  c.add(ipLabel);
  t.addActionListener(this);
  
  String myIP;
  try {
    inet = InetAddress.getLocalHost();
    myIP = inet.getHostAddress();
  }
  catch (Exception e) {
    e.printStackTrace();
    myIP = "couldnt get IP"; 
  }
  ipLabel.setLabel("this machine's ip: " + myIP);
}

public void draw() {
  background(200);
  console.draw(0, 100, width, height);
  console.print();
}

/* incoming osc message are forwarded to the oscEvent method. */
public void oscEvent(OscMessage theOscMessage) {
  /* print the address pattern and the typetag of the received OscMessage */
  //println("### received an osc message.");
  println(theOscMessage.addrPattern());
  //println(" typetag: "+theOscMessage.typetag());
  for (int i = 0; i < theOscMessage.arguments().length; i++) {
    println(theOscMessage.arguments()[i]);
  }
  println("-----------");
}

public void actionPerformed(GUIEvent e) {
  if (e.getMessage().equals("Completed")) {
    l.setLabel(t.getValue());
    changePort();
  }
}

public void changePort() {
  /* start oscP5, listening for incoming messages at port 12000 */
  if (PApplet.parseInt(l.getLabel()) != 0) {
    oscP5 = new OscP5(this, PApplet.parseInt(l.getLabel()));
  }
}
  public void settings() {  size(400, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "oscConsole" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
