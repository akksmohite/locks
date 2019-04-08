import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ciscopractice.Locks.ReadWriteLcok;


public class Lockex1 {

	public static void main(String[] args) {
		
		ReentrantReadWriteLock wr = new ReentrantReadWriteLock();
		HashMap<String, Integer> map = new HashMap<>();
		Thread1 w =  new Thread1(map, wr);
		Thread1 w1 =  new Thread1(map, wr);
		Thread2 r =  new Thread2(map, wr);
		Thread2 r1 =  new Thread2(map, wr);
		Thread2 r2 =  new Thread2(map, wr);
		r.start();
		r1.start();
		r2.start();

		w.start();
		w1.start();
		

	}
	
	

}

class Thread1 extends Thread {
	
	public static int counter=0;
	HashMap<String, Integer> map;
	ReentrantReadWriteLock wr;

	Thread1(HashMap<String, Integer> map, ReentrantReadWriteLock wr) {
		this.map = map;
		this.wr=wr;
	}

	public void run() {
		while(counter < 10) {
			try {
				put();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void put() throws InterruptedException {
		wr.writeLock().lock();
		System.out.println(" " + Thread.currentThread().getName() + " aquired lock for write");
		System.out.println("Push " + this.counter + " by " +Thread.currentThread().getName());
		Thread.sleep(500);
		map.put("" + this.counter, this.counter);
		this.counter++;
		wr.writeLock().unlock();
		System.out.println(" " + Thread.currentThread().getName() + " released lock lock for write");
	}
}


class Thread2 extends Thread {
	
	
	HashMap<String, Integer> map;
	ReentrantReadWriteLock wr;
	
	Thread2(HashMap<String, Integer> map, ReentrantReadWriteLock wr) {
		this.map = map;
		this.wr=wr;
	}

	public void run() {
		while(true){
			try {
				Integer a = get();
				if (a != null && a >= 10)
					break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	Integer get() throws InterruptedException {
		wr.readLock().lock();
		System.out.println(" " + Thread.currentThread().getName() + " aquired lock lock for read");
		Integer aa = map.get((Thread1.counter-1) + "");
		System.out.println("Read " + aa + " by " + Thread.currentThread().getName()  + " read");
		Thread.sleep(0);
		wr.readLock().unlock();
		System.out.println(" " + Thread.currentThread().getName() + " released	 lock lock for read ");
		return aa;
	}
}

