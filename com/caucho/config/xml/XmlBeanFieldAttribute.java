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

package com.caucho.config.xml;

import java.lang.reflect.Field;

import com.caucho.config.ConfigException;
import com.caucho.config.attribute.Attribute;
import com.caucho.config.program.PropertyStringProgram;
import com.caucho.config.type.ConfigType;
import com.caucho.config.type.TypeFactory;
import com.caucho.util.L10N;
import com.caucho.xml.QName;

/**
 * Attribute for configuring an XML CanDI bean's field. The equivalent of
 * mybean.setFoo("stuff") is
 * 
 * <code><pre>
 * &lt;mypkg:MyBean>
 *   &lt;foo>stuff&lt;/foo>
 * &lt;/mypkg:MyBean>
 * </pre></code>
 *
 */
public class XmlBeanFieldAttribute extends Attribute {
  private static final L10N L = new L10N(XmlBeanFieldAttribute.class);

  private static final QName VALUE = new QName("value");

  private final Field _field;
  private final ConfigType<XmlBeanFieldConfig> _configType;

  public XmlBeanFieldAttribute(Class cl, Field field)
  {
    _field = field;
    _configType = TypeFactory.getType(XmlBeanFieldConfig.class);
  }

  public ConfigType getConfigType()
  {
    return _configType;
  }

  /**
   * Creates the child bean.
   */
  @Override
  public Object create(Object parent, QName qName)
    throws ConfigException
  {
    return new XmlBeanFieldConfig(_field);
  }
  
  /**
   * Sets the value of the attribute
   */
  public void setValue(Object bean, QName name, Object value)
    throws ConfigException
  {
    XmlBeanConfig customBean = (XmlBeanConfig) bean;

    customBean.addField((XmlBeanFieldConfig) value);
  }
  
  /**
   * Sets the value of the attribute
   */
  @Override
  public void setText(Object parent, QName name, String text)
    throws ConfigException
  {
    XmlBeanConfig customBean = (XmlBeanConfig) parent;

    customBean.addBuilderProgram(new PropertyStringProgram(name, text));
  }
}
