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

package com.caucho.quercus.expr;

import com.caucho.quercus.Location;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.Value;

/**
 * Represents a PHP post increment expression.
 */
public class UnaryPostIncrementExpr extends AbstractUnaryExpr {
  protected final int _incr;

  public UnaryPostIncrementExpr(Location location, Expr expr, int incr)
  {
    // super(expr.createRef());
    super(location, expr);

    _incr = incr;
  }

  public UnaryPostIncrementExpr(Expr expr, int incr)
  {
    super(expr);

    _incr = incr;
  }

  public Value eval(Env env)
  {
    return _expr.evalPostIncrement(env, _incr);
  }

  /**
   * Return true for a double value
   */
  public boolean isDouble()
  {
    return _expr.isDouble();
  }

  /**
   * Return true for a long value
   */
  public boolean isLong()
  {
    return _expr.isLong();
  }

  /**
   * Return true for a number
   */
  public boolean isNumber()
  {
    return true;
  }

  public String toString()
  {
    if (_incr > 0)
      return _expr + "++";
    else
      return _expr + "--";
  }
}

