
import java.util.concurrent.ThreadLocalRandom;

public class CustomerGenerator implements Runnable {
	public static final int ARRIVAL_INTERVAL_OFFSET_MILLIS = 100;
	public static final int ARRIVAL_INTERVAL_RANGE_MILLIS = 200;
	private final BarberShop shop;
	int i = 0;
	public CustomerGenerator(BarberShop shop) {
		this.shop = shop;
	}

	public void run() {
		while (shop.isOpen()) {
			try {
				Thread.sleep(nextRandomInterval());
				if(i==15){
					Thread.sleep(1000);
					i=0;
				}
				if(shop.isOpen()) shop.addnewCustomer(new Object());
				i++;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	public int nextRandomInterval() {
		return ThreadLocalRandom.current().nextInt(ARRIVAL_INTERVAL_RANGE_MILLIS) + ARRIVAL_INTERVAL_OFFSET_MILLIS;
	}

}
