package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

class ContentNegotiator extends Thread
  implements Runnable
{
  Sound sound;
  URL url;
  boolean locked;
  byte[] buffer;
  Object content;
  static final int SOUND_LOADER = 1;
  static final int URL_BYTE_LOADER = 2;
  int negotiation = 0;

  ContentNegotiator(URL url)
  {
    this.url = url;
    this.locked = true;
    this.negotiation = 2;
    start();
  }

  ContentNegotiator(Sound sound)
  {
    this.sound = sound;
    this.locked = true;
    this.negotiation = 1;
    start();
  }

  public void run()
  {
    startLoading();
  }

  synchronized Object getContent()
  {
    if (this.locked) {
      try {
        wait();
      }
      catch (InterruptedException ie)
      {
      }
    }

    return this.content;
  }

  synchronized void startLoading()
  {
    if (this.negotiation == 2)
    {
      try {
        int contentLength = -1;
        URLConnection urlc = this.url.openConnection();
        urlc.connect();
        urlc.getContentType();
        DataInputStream d = new DataInputStream(urlc.getInputStream());
        contentLength = urlc.getContentLength();
        this.buffer = new byte[contentLength];
        this.content = this.buffer;
        if (d != null) {
          d.readFully(this.buffer, 0, contentLength);
        }
        yield();
      }
      catch (IOException ie) {
        ie.printStackTrace();
      }
      this.locked = false;
      notify();
    }
    else if (this.negotiation == 1) {
      Browser.getBrowser(); if (Browser.debug) {
        System.out.println("Sound negotiation begin");
      }

      this.content = this.sound;
      this.locked = false;
      Browser.getBrowser(); if (Browser.debug) {
        System.out.println("Sound negotiation end");
      }
      notify();
    }
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ContentNegotiator
 * JD-Core Version:    0.6.0
 */