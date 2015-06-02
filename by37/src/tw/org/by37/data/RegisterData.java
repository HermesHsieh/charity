package tw.org.by37.data;

public class RegisterData {

        public static String email;
        public static String password;
        public static String name;
        public static String source;
        public static String image;

        public static String showData() {
                StringBuffer sb = new StringBuffer();
                sb.append("email : " + email).append("\n");
                sb.append("password : " + password).append("\n");
                sb.append("name : " + name).append("\n");
                sb.append("source : " + source).append("\n");
                sb.append("image : " + image);
                return sb.toString();
        }

        public static void clean() {
                email = null;
                password = null;
                name = null;
                source = null;
                image = null;
        }

}
