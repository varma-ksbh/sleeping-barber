import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.concurrent.TimeUnit.*;

public class BarberShop {
	public static final int NUM_WAITING_ROOM_CHAIRS = 3;
	public static final long SHOP_RUNTIME_MILLIS = SECONDS.toMillis(5);
	private final static AtomicBoolean shopOpen = new AtomicBoolean();
	private final static AtomicInteger totalHaircuts = new AtomicInteger();
	private final static AtomicInteger lostCustomers = new AtomicInteger();
	private final BlockingQueue<Object> waitingRoom = new ArrayBlockingQueue<Object>(
			NUM_WAITING_ROOM_CHAIRS);
	private static Barber barber = null; 
	// private final BlockingQueue<Object> waitingRoom = new
	// LinkedBlockingQueue<Object>(NUM_WAITING_ROOM_CHAIRS);
	
	public static void main(String[] args) throws InterruptedException {
		final BarberShop shop = new BarberShop();
		barber = new Barber(shop);
		
		ExecutorService executor = Executors.newFixedThreadPool(3);

		Runnable customerGenerator = new CustomerGenerator(shop);

		shop.open();
		executor.execute(new Runnable(){

			public void run() {
				// TODO Auto-generated method stub
				while(shop.isOpen()) {
					//Run indefinitely till the shop is closed..
					if(!shop.waitingRoom.isEmpty()){
						if(barber.cutHair(shop.waitingRoom.poll())){
							shop.recordHaircut();
						}
					}
				}
			}
			
		});
		
		executor.execute(customerGenerator);

		executor.shutdown();

		Thread.sleep(SHOP_RUNTIME_MILLIS);

		shop.close();
		System.out.println("--------------------Todays analysis---------------");
		System.out.println("Total openening time (milliseconds) of shop:" + SHOP_RUNTIME_MILLIS);
		System.out.println("Total no.of customers served:" + shop.haircuts());
		System.out.println("Total no.of customers declined:" + shop.lostCustomers());
		System.out.println("Total sleeping time of barber:");
	}
	
	
	private void close() {
		shopOpen.set(false);
		System.out.println("Closing the Shop..");
	}

	private void open() {
		System.out.println("Openening Barber Shop..");
		shopOpen.set(true);
	}

	public boolean isOpen() {
		return shopOpen.get();
	}

	public boolean isEmpty() {
		return waitingRoom.isEmpty();
	}

	public boolean addnewCustomer(Object customer) {
		if (waitingRoom.size() < NUM_WAITING_ROOM_CHAIRS) {
			// able to add new customer
			System.out.println("New Customer Added to the waiting Room..");
			waitingRoom.add(customer);
			return true;

			// ask to cut hair
		}
		// failed to add new customer
		System.out.println("Waiting Room is Full!! Lost a Customer..");
		lostCustomers.incrementAndGet();
		System.out.println("Total no.of customers lost until now is:"
				+ lostCustomers.get());
		// say you have lost a customer
		return false;
	}
	
	public void removeCustomer(){
		waitingRoom.poll();
	}
	
	public void recordHaircut() {
		totalHaircuts.incrementAndGet();
	}

	public Object lostCustomers() {
		return lostCustomers.get();
	}

	public Object haircuts() {
		return totalHaircuts.get();
	}

}
