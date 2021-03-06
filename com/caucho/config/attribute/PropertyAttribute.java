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

package com.caucho.config.attribute;

import java.lang.reflect.*;

import com.caucho.config.*;
import com.caucho.config.type.*;
import com.caucho.util.L10N;
import com.caucho.xml.QName;

public class PropertyAttribute extends Attribute {
  private static final L10N L = new L10N(PropertyAttribute.class);
  
  private final Method _putMethod;
  private final ConfigType<?> _type;

  public PropertyAttribute(Method putMethod, ConfigType<?> type)
  {
    _putMethod = putMethod;
    _type = type;
  }
  
  /**
   * Returns the config type of the attribute value.
   */
  @Override
  public ConfigType<?> getConfigType()
  {
    return _type;
  }
  
  /**
   * Sets the value of the attribute
   */
  @Override
  public void setText(Object bean, QName name, String value)
    throws ConfigException
  {
    if ("#text".equals(name.getLocalName())) {
      if (value == null || value.trim().length() == 0)
        return;
      
      throw new ConfigException(L.l("text is not allowed for bean {0}\n  '{1}'",
                                    bean.getClass().getName(), value.trim()));
    }
    
    try {
      _putMethod.invoke(bean, name.getLocalName(), _type.valueOf(value));
    } catch (Exception e) {
      throw ConfigException.create(_putMethod, e);
    }
  }
  
  /**
   * Sets the value of the attribute
   */
  @Override
  public void setValue(Object bean, QName name, Object value)
    throws ConfigException
  {
    if ("#text".equals(name.getLocalName()))
      throw new ConfigException(L.l("text is not allowed in this context\n  '{0}'",
                                    value));
    
    try {
      _putMethod.invoke(bean, name.getLocalName(), value);
    } catch (Exception e) {
      throw ConfigException.create(_putMethod, e);
    }
  }
}
