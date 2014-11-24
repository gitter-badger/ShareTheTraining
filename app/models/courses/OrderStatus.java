package models.courses;

public enum OrderStatus {
	
	CONFIRMED,ORDERED,DONE,CANCELLED,PENDING;
	
	public static OrderStatus fromInteger(int x) {
        switch(x) {
        case 0:
            return CONFIRMED;
        case 1:
            return DONE;
        case 2:
            return CANCELLED;
        
        }
        return null;
    }
}
