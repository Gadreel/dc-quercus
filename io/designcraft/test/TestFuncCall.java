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

package io.designcraft.test;

import com.caucho.quercus.env.*;
import com.caucho.quercus.function.AbstractFunction;

/**
 * Represents a function
 */
@SuppressWarnings("serial")
public class TestFuncCall extends AbstractFunction {
  public TestFuncCall()
  {
  }

  /**
   * Evaluates the function.
   */
  @Override
  public Value call(Env env, Value []args)
  {
    System.out.println("call info: " + args);

    if (args.length > 0) {
      if (args[0] instanceof StringValue) {
        System.out.println("first arg: " + ((StringValue)args[0]).toString());
      }
    }

    return StringValue.create("c1: " + args.length);
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + getName() + "]";
  }
}

