import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.SQLException;

public class Main {

    static int stud_id;
    static String stud_name;
    static String stud_phn;
    static String stud_city;

    Main(int stud_id, String stud_name, String stud_phn, String stud_city){
        this.stud_id = stud_id;
        this.stud_name = stud_name;
        this.stud_phn = stud_phn;
        this.stud_city = stud_city;
    }

    static Connection con;

    public static Connection create(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            String usr = "root";
            String pass = "mysql123";
            String url = "jdbc:mysql://localhost:3306/studentmanagmentsystem";
            con = DriverManager.getConnection(url, usr, pass);
        }
        catch (Exception e){
            System.out.println(e);
        }
        return con;
    }
    static boolean insertInDB(Main student){
        boolean flag = false;
        try{
            Connection con = create();
            String query =  "insert into students(stud_id, stud_name, stud_phn, stud_city) values(?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, student.stud_id);
            pstmt.setString(2, student.stud_name);
            pstmt.setString(3, student.stud_phn);
            pstmt.setString(4, student.stud_city);
            pstmt.executeUpdate();
            flag = true;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return flag;
    }

    public static void create_stud(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter student id: ");
        int stud_id = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter student name: ");
        String stud_name = sc.nextLine();

        if(checkName(stud_name)){
            System.out.println("Student already exists");
            return;
        }
        System.out.println("Enter student phone no. :");
        String stud_phn = sc.nextLine();

        System.out.println("Enter student city: ");
        String stud_city = sc.nextLine();

        Main stud = new Main(stud_id, stud_name, stud_phn, stud_city);
        boolean ans = insertInDB(stud);
        if(ans){
            System.out.println("Student added successfully");
        }
        else {
            System.out.println("Something went wrong");
        }

    }

    static boolean checkName(String stud_name){
        try{
            Connection con = create();

            String  query = "SELECT * FROM students WHERE stud_name = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, stud_name);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }





    static boolean update_stud(String stud_name){
        boolean flag = false;
        try {
            if(checkName(stud_name)){
                Connection con = create();
                String query = "UPDATE students SET stud_id = ?, stud_phn = ?, stud_city = ? WHERE stud_name = ?";
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter student id: ");
                int stud_id = sc.nextInt();
                sc.nextLine();
//                System.out.println("Enter student name: ");
//                stud_name = sc.nextLine();

                System.out.println("Enter student phone no. :");
                String stud_phn = sc.nextLine();

                System.out.println("Enter student city: ");
                String stud_city = sc.nextLine();


                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setInt(1, stud_id);
                pstmt.setString(2, stud_phn);
                pstmt.setString(3, stud_city);
                pstmt.setString(4, stud_name);
                pstmt.executeUpdate();
                flag = true;
            }
            else {
                System.out.println("Name not present.");
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    public static boolean delete_stud(String stud_name){
        boolean flag = false;
        try{
            if(checkName(stud_name)){
                Connection con = create();
                String deleteQuery = "DELETE FROM students WHERE stud_name = ?";
                PreparedStatement pstmt = con.prepareStatement(deleteQuery);
                pstmt.setString(1, stud_name);

                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    flag = true;
                }
            }
            else{
                System.out.println("Student not present");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return flag;
    }

    public static void fetch_all(){
        try{
            Connection con = create();
            String selectQuery = "SELECT * FROM students";
            PreparedStatement pstmt = con.prepareStatement(selectQuery);

            ResultSet resultSet = pstmt.executeQuery();

            System.out.println("Student Table:");
            System.out.printf("%-10s %-20s %-15s %-20s\n", "ID", "Name", "Phone", "City");
            System.out.println("------------------------------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("stud_id");
                String name = resultSet.getString("stud_name");
                String phone = resultSet.getString("stud_phn");
                String city = resultSet.getString("stud_city");

                System.out.printf("%-10d %-20s %-15s %-20s\n", id, name, phone, city);
            }
            System.out.println();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Press 1 to ADD student");
            System.out.println("Press 2 to UPDATE student");
            System.out.println("Press 3 to DELETE student");
            System.out.println("Press 4 to DISPLAY whole table");
            System.out.println("Press 5 to EXIT app");
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    create_stud();
                    break;

                case 2:
                {
                    sc.nextLine();
                    System.out.println("Enter name of student you want to update: ");
                    String stud_name = sc.nextLine();
                    if(update_stud(stud_name)){
                        System.out.println("Successfully updated");
                    }
                    else{
                        System.out.println("Try again");
                    }
                    break;
                }

                case 3: {
                    sc.nextLine();
                    System.out.println("Enter name of student you want to delete: ");
                    String stud_name = sc.nextLine();
                    if (delete_stud(stud_name)) {
                        System.out.println("Successfully deleted");
                    } else {
                        System.out.println("Try again");
                    }
                    break;
                }

                case 4:
                    fetch_all();
                    break;

                case 5:
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid input");
            }
        }
    }
}