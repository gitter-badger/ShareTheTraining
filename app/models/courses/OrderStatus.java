package models.courses;

public enum OrderStatus {
	
	CONFIRMED,ORDERED,DONE,CANCELLED,PENDING;
	
	public static OrderStatus fromInteger(int x) {
        switch(x) {
        case 0:
            return CONFIRMED;
        case 1:
            return ORDERED;
        case 2:
            return DONE;
        case 3:
            return CANCELLED;
        case 4:
            return PENDING;
        }
        return null;
    }
}
