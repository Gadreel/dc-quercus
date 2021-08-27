/*
 * Copyright (c) 1998-2018 Caucho Technology -- all rights reserved
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
 * @author Scott Ferguson
 */

package com.caucho.vfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import com.caucho.util.L10N;

/**
 * The classpath scheme.
 */
public class ClasspathPath extends FilesystemPath {
  protected static L10N L = new L10N(ClasspathPath.class);

  private ClassLoader _loader;
  
  /**
   * Creates a new classpath sub path.
   *
   * @param root the classpath filesystem root
   * @param userPath the argument to the calling lookup()
   * @param newAttributes any attributes passed to http
   * @param path the full normalized path
   * @param query any query string
   */
  public ClasspathPath(ClassLoader loader)
  {
    this(null, "", "", loader);
  }
  
  /**
   * Creates a new classpath sub path.
   *
   * @param root the classpath filesystem root
   * @param userPath the argument to the calling lookup()
   * @param newAttributes any attributes passed to http
   * @param path the full normalized path
   * @param query any query string
   */
  public ClasspathPath(FilesystemPath root,
                       String userPath,
                       String path)
  {
    this(root, userPath, path, null);
  }
  
  /**
   * Creates a new classpath sub path.
   *
   * @param root the classpath filesystem root
   * @param userPath the argument to the calling lookup()
   * @param newAttributes any attributes passed to http
   * @param path the full normalized path
   * @param query any query string
   */
  public ClasspathPath(FilesystemPath root,
                       String userPath,
                       String path,
                       ClassLoader loader)
  {
    super(root, userPath, path);

    if (_root == null)
      _root = this;
    
    _loader = loader;
  }
  
  /**
   * Lookup the actual path relative to the filesystem root.
   *
   * @param userPath the user's path to lookup()
   * @param attributes the user's attributes to lookup()
   * @param path the normalized path
   *
   * @return the selected path
   */
  @Override
  public Path fsWalk(String userPath,
                        Map<String,Object> attributes,
                        String path)
  {
    return new ClasspathPath(_root, userPath, path, _loader);
  }

  /**
   * Returns the scheme, http.
   */
  public String getScheme()
  {
    return "classpath";
  }

  /**
   * Returns true if the file exists.
   */
  public boolean exists()
  {
    ClassLoader loader = getLoader();
    
    return loader.getResource(getPath()) != null;
  }
  
  private ClassLoader getLoader()
  {
    ClassLoader loader = _loader;
    
    if (loader != null) {
      return loader;
    }
    else {
      return Thread.currentThread().getContextClassLoader();
    }
  }

  /**
   * Returns true if the file exists.
   */
  public boolean isFile()
  {
    return exists();
  }

  /**
   * Returns true if the file is readable.
   */
  public boolean canRead()
  {
    return exists();
  }

  /**
   * Returns the last modified time.
   */
  public boolean isDirectory()
  {
    return false;
  }
  
  @Override
  public long getLength()
  {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    
    URL url = loader.getResource(getPath());
    
    if (url == null)
      return 0;
    else
      return lookup(url.toString()).getLength();
    
  }

  /**
   * Returns a read stream for a GET request.
   */
  @Override
  public StreamImpl openReadImpl() throws IOException
  {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();

    String path = getPath();
    if (path.startsWith("/"))
      path = path.substring(1);

    InputStream is = loader.getResourceAsStream(path);

    if (is == null)
      throw new FileNotFoundException(getFullPath());
    
    return new VfsStream(is, null);
  }
  
  /**
   * Returns the string form of the http path.
   */
  public String toString()
  {
    return getURL();
  }
}
