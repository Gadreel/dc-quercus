/*
 * Copyright (c) 1998-2018 Caucho Technology -- all rights reserved
 *
 * This file is part of Resin(TM)
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Baratine is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Baratine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Resin; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */

package com.caucho.env.actor2;

import java.util.concurrent.TimeUnit;

/**
 * Ring blocking API used to block and wait for the producers and consumers.
 */
public interface RingBlocker
{
  long nextOfferSequence();

  boolean offerWait(long sequence,
                    long timeout,
                    TimeUnit unit);

  void offerWake();

  long nextPollSequence();

  boolean pollWait(long sequence,
                   long timeout,
                   TimeUnit unit);

  boolean isPollWait();

  void pollWake();

  boolean wake();

  void wakeAll();
}
