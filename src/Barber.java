
public class Barber {
	private static final int HAIRCUT_TIME_MILLIS = 300;
	private final BarberShop shop;
    private boolean isbarbersleeping = true;
	public Barber(BarberShop shop) {
		this.shop = shop;
	}

	private void wakeupBarber(){
		System.out.println("New Customer arrived..wakingup barber..");
		isbarbersleeping = false;
	}
	
	public boolean cutHair(Object customer){
		if(isbarbersleeping){
			wakeupBarber();
			System.out.println("Waking up Barber..");
		}
		System.out.println("Barber Cutting the Hair..");
		try {
			Thread.sleep(HAIRCUT_TIME_MILLIS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if(shop.isEmpty()){
			isbarbersleeping = true;
			System.out.println("No Customers in waiting Room.. Barber going to Sleep..");
			System.out.println();
		}
		return true;
	}
	
}
