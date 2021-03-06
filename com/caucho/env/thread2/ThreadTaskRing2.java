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

package com.caucho.env.thread2;

import java.util.concurrent.TimeUnit;

import com.caucho.util.RingValueQueue;

public class ThreadTaskRing2 {
  private static final int RING_SIZE = 16 * 1024;
  
  private final RingValueQueue<TaskItem2> _queue;
  
  public ThreadTaskRing2()
  {
    _queue = new RingValueQueue<TaskItem2>(RING_SIZE);
  }
  
  public final boolean isEmpty()
  {
    return _queue.isEmpty();
  }
  
  public final int getSize()
  {
    return _queue.size();
  }
  
  public final boolean offer(Runnable task, ClassLoader loader)
  {
    return _queue.offer(new TaskItem2(task, loader), 1, TimeUnit.SECONDS);
  }
  
  public final boolean takeAndSchedule(ResinThread2 thread)
  {
    TaskItem2 item = _queue.poll();
    
    if (item == null) {
      return false;
    }
    else {
      return item.schedule(thread);
    }
  }
  
  private static class TaskItem2 {
    private final Runnable _task;
    private final ClassLoader _loader;
    
    TaskItem2(Runnable task, ClassLoader loader)
    {
      _task = task;
      _loader = loader;
    }

    final boolean schedule(ResinThread2 thread)
    {
      thread.scheduleTask(_task, _loader);
      
      return true;
    }
  }
}
