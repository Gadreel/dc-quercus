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
package com.caucho.config.util;

import java.io.Serializable;

import com.caucho.cache.annotation.CacheKey;

/**
 * Key for a cacheable method.
 */
@SuppressWarnings("serial")
public class CacheKeyImpl implements Serializable, CacheKey {
  private Object []_key;
  
  public CacheKeyImpl(Object ...key)
  {
    _key = key;
  }
  
  @Override
  public int hashCode()
  {
    int hashCode = 37;
    
    int length = _key.length;
    
    for (int i = 0; i < length; i++) {
      hashCode = hashCode * 65521;
      
      Object subKey = _key[i];
      
      if (subKey != null)
        hashCode += subKey.hashCode();
    }
    
    return hashCode;
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (o == null || o.getClass() != CacheKeyImpl.class)
      return false;
    
    CacheKeyImpl cacheKey = (CacheKeyImpl) o;
    Object []key = cacheKey._key;
    
    int length = _key.length;
    
    if (length != key.length)
      return false;
    
    for (int i = 0; i < length; i++) {
      Object keyA = _key[i];
      Object keyB = key[i];
      
      if (keyA == null) {
        if (keyB != null)
          return false;
      }
      else if (! keyA.equals(keyB))
        return false;
    }
    
    return true;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    
    sb.append(getClass().getSimpleName());
    sb.append("[");
    
    for (int i = 0; i < _key.length; i++) {
      if (i != 0)
        sb.append(", ");
      
      sb.append(_key[i]);
    }
    
    sb.append("]");
    
    return sb.toString();
  }
}
