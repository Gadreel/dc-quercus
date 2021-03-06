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

import java.io.IOException;
import java.io.OutputStream;

import com.caucho.util.ByteAppendable;

/**
 * An OutputStream with an accessible buffer.
 */
abstract public class OutputStreamWithBuffer extends OutputStream 
  implements ByteAppendable {

  /**
   * Returns the stream's buffer.
   */
  abstract public byte []getBuffer()
    throws IOException;
  
  /**
   * Returns the stream's buffer offset.
   */
  abstract public int getBufferOffset()
    throws IOException;
  
  /**
   * Sets the stream's buffer length.
   */
  abstract public void setBufferOffset(int offset)
    throws IOException;
  
  /**
   * Returns the next buffer.
   *
   * @param length the length of the completed buffer
   *
   * @return the next buffer
   */
  abstract public byte []nextBuffer(int offset)
    throws IOException;
  
  abstract public boolean isClosed();
}
