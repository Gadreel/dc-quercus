/*
 * Copyright (c) 1998-2016 Caucho Technology -- all rights reserved
 *
 * This file is part of Resin(R) Open Source
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Resin Open Source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Resin Open Source is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Resin Open Source; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Nam Nguyen
 */

package com.caucho.quercus.lib.curl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Represents a HttpURLConnection wrapper.
 */
public class HttpsConnection
  extends CurlHttpConnection
{
  public HttpsConnection(URL url,
                          String username,
                          String password)
    throws IOException
  {
    super(url, username, password);
  }
  
  @Override
  protected void init(CurlResource curl)
    throws IOException
  { 
    Proxy proxy = getProxy();

    URLConnection conn;
    
    HttpsURLConnection httpsConn = null;

    if (proxy != null)
      conn = getURL().openConnection(proxy);
    else
      conn = getURL().openConnection();
        
    if (conn instanceof HttpsURLConnection) {
      httpsConn = (HttpsURLConnection) conn;
      
      if (curl.getSslKey() != null && curl.getSslCert() != null) {
        httpsConn.setSSLSocketFactory(createSSLContext(curl).getSocketFactory());
      }
      else if (! curl.getIsVerifySSLPeer()) {
        httpsConn.setSSLSocketFactory(createSSLContextUntrusted(curl).getSocketFactory());
      }
      else if (curl.getCaInfo() != null) {
        httpsConn.setSSLSocketFactory(createSSLContextCaInfo(curl).getSocketFactory());
      }
      else {
        httpsConn.setSSLSocketFactory(createSSLContextDefault(curl).getSocketFactory());
      }
      
      if (! curl.getIsVerifySSLPeer()
          || ! curl.getIsVerifySSLCommonName()
          || ! curl.getIsVerifySSLHostname()) {
        HostnameVerifier hostnameVerifier
          = CurlHostnameVerifier.create(curl.getIsVerifySSLPeer(),
                                        curl.getIsVerifySSLCommonName(),
                                        curl.getIsVerifySSLHostname());
        
        httpsConn.setHostnameVerifier(hostnameVerifier);
      }
    }
    
    setConnection(conn);
  }
  
  /**
   * Connects to the server.
   */
  @Override
  public void connect(CurlResource curl)
    throws ConnectException, ProtocolException, SocketTimeoutException, IOException
  {
    try {
      super.connect(curl);
      
      ((HttpsURLConnection) getConnection()).getServerCertificates();
    }
    catch (SSLPeerUnverifiedException e) {
      if (curl.getIsVerifySSLPeer()) {
        e.printStackTrace();
        throw e;
      }
    }
    catch (IOException e) {
      e.printStackTrace();
      
      throw e;
    }
  }
  
  private static SSLContext createSSLContextCaInfo(CurlResource curl)
    throws IOException
  {
    String algorithm = curl.getSslVersion();
    String certFile = curl.getCaInfo();
    
    try {
      return CurlSSLContextFactory.createCaInfo(algorithm, certFile);
    }
    catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();

      throw new IOException(e);
    }
  }
  
  private static SSLContext createSSLContext(CurlResource curl)
    throws IOException
  {
    String algorithm = curl.getSslVersion();

    String keyPass = curl.getSslKeyPassword();
    String certFile = curl.getSslCert();
    String keyFile = curl.getSslKey();
    
    try {
      SSLContext context = CurlSSLContextFactory.create(certFile, keyFile, keyPass, algorithm);
      
      return context;
    }
    catch (IOException e) {
      throw e;
    }
    catch (Exception e) {
      throw new IOException(e);
    }
  }
  
  private static SSLContext createSSLContextUntrusted(CurlResource curl)
    throws IOException
  {
    try {
      String algorithm = curl.getSslVersion();
      
      SSLContext context = CurlSSLContextFactory.createUntrusted(algorithm);
      
      return context;
    }
    catch (IOException e) {
      throw e;
    }
    catch (Exception e) {
      throw new IOException(e);
    }
  }
  
  private static SSLContext createSSLContextDefault(CurlResource curl)
    throws IOException
  {
    try {
      String algorithm = curl.getSslVersion();
      
      SSLContext context = SSLContext.getInstance(algorithm);

      context.init(null, createTrustManagerDefault(), null);
      
      return context;
    }
    catch (Exception e) {
      throw new IOException(e);
    }
  }
  
  private static TrustManager []createTrustManagerDefault()
  {
    if (true) {
      return null;
    }
    
    TrustManager tm =
      new javax.net.ssl.X509TrustManager() {
        public java.security.cert.X509Certificate[]
          getAcceptedIssuers() {
          return null;
        }
        public void checkClientTrusted(
                                       java.security.cert.X509Certificate[] cert, String foo) {
        }
        public void checkServerTrusted(
                                       java.security.cert.X509Certificate[] cert, String foo) {
        }
      };
      
    return new TrustManager[] { tm };
  }
}
