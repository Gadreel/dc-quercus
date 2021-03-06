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
 * @author Nam Nguyen
 */

package com.caucho.quercus.lib.curl;

import java.io.IOException;
import java.io.OutputStream;

import com.caucho.quercus.env.Callable;
import com.caucho.quercus.env.Callback;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.StringValue;
import com.caucho.quercus.env.Value;

public class UrlEncodedBody extends PostBody
{
  private StringValue _body;
  private int _length;
  
  private String _contentType = "application/x-www-form-urlencoded";

  public UrlEncodedBody (Env env, Value body)
  {
    _body = body.toStringValue(env);
    _length = _body.length();
  }

  public String getContentType()
  {
    return _contentType;
  }
  
  public void setContentType(String type)
  {
    _contentType = type;
  }

  @Override
  public long getContentLength()
  {
    return (long) _length;
  }

  public void writeTo(Env env, OutputStream os)
    throws IOException
  {
    for (int i = 0; i < _length; i++) {
      os.write(_body.charAt(i));
    }
  }
}
