package tw.org.by37.position;

public class LocationResult {
        String address;
        String lat;
        String lng;

        @Override
        public String toString() {
                return "[ Address : " + address + ", Lat : " + lat + ", Lng : " + lng + " ]";
        }
}
