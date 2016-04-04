import java.util.LinkedList;

public class WorkQueue {
	
	volatile boolean shutdown = false;
	
	
	private int nThreads;
	private PoolWorker[] threads;
	private LinkedList queue;
	
	public WorkQueue (int nThreads) {
		this.nThreads = nThreads;
		queue = new LinkedList();
		threads = new PoolWorker[nThreads];
		
		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
		
		
	}
	
	public void execute (Runnable r) {
// no new runnable when shut down
		if (shutdown == true) {
			return;
		}
		else {
			synchronized(queue) {
				queue.addLast(r);
				queue.notify();
			}
		}
		
	}
	
	public void shutdown () {
		synchronized(queue) {
			queue.notifyAll();
			shutdown = true;
		}
	}
	
	public void awaitTermination () {
		
		for (int i = 0; i < nThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class PoolWorker extends Thread {
		
		public void run() {
			Runnable r;
			
			while (true) {
                synchronized(queue) {
                    while (queue.isEmpty() && !shutdown) {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }
                    if (queue.isEmpty() && shutdown) {
                    	break;
                    }

                    r = (Runnable) queue.removeFirst();
                }

                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                    r.run();
                }
                catch (RuntimeException e) {
                    
                	e.printStackTrace();
                }
            }
			
		}
	}
	

}
