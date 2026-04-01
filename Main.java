import java.sql.*;
import java.util.*;

class Bank {
    static Connection con;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/online_bank",
                    "root",
                    "password"
            );

            int choice;
            do {
                System.out.println("\n<----- ONLINE BANKING SYSTEM ----->");
                System.out.println("1. Create New Account");
                System.out.println("2. Deposit Amount");
                System.out.println("3. Withdraw Amount");
                System.out.println("4. Check Balance");
                System.out.println("5. Account Holder List");
                System.out.println("6. Exit");
                System.out.print("Enter Choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        checkBalance();
                        break;
                    case 5:
                        accountList();
                        break;
                    case 6:
                        System.out.println("Thank you for using Online Banking System");
                        break;
                    default:
                        System.out.println("Invalid Choice!");
                }

            } while (choice != 6);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // 1. Create Account
    static void createAccount() throws SQLException {
        System.out.print("Enter Name: ");
        String name = sc.next();

        System.out.print("Enter Mobile Number: ");
        long mob = sc.nextLong();

        System.out.print("Enter City: ");
        String city = sc.next();

        System.out.print("Enter Initial Balance: ");
        int balance = sc.nextInt();

        String query = "INSERT INTO bank(name, mob, city, balance) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, name);
        pst.setLong(2, mob);
        pst.setString(3, city);
        pst.setInt(4, balance);

        pst.executeUpdate();
        System.out.println("Account Created Successfully!");
    }

    // 2. Deposit
    static void deposit() throws SQLException {
        System.out.print("Enter Account Number: ");
        int ac = sc.nextInt();

        System.out.print("Enter Deposit Amount: ");
        int amt = sc.nextInt();

        String q = "UPDATE bank SET balance = balance + ? WHERE ac_no = ?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setInt(1, amt);
        pst.setInt(2, ac);

        int rows = pst.executeUpdate();
        if (rows > 0)
            System.out.println("Amount Deposited Successfully");
        else
            System.out.println("Account Not Found");
    }

    // 3. Withdraw
    static void withdraw() throws SQLException {
        System.out.print("Enter Account Number: ");
        int ac = sc.nextInt();

        System.out.print("Enter Withdraw Amount: ");
        int amt = sc.nextInt();

        String q1 = "SELECT balance FROM bank WHERE ac_no = ?";
        PreparedStatement pst1 = con.prepareStatement(q1);
        pst1.setInt(1, ac);
        ResultSet rs = pst1.executeQuery();

        if (rs.next()) {
            int bal = rs.getInt(1);

            if (bal >= amt) {
                String q2 = "UPDATE bank SET balance = balance - ? WHERE ac_no = ?";
                PreparedStatement pst2 = con.prepareStatement(q2);
                pst2.setInt(1, amt);
                pst2.setInt(2, ac);
                pst2.executeUpdate();
                System.out.println("Amount Withdrawn Successfully");
            } else {
                System.out.println("Insufficient Balance");
            }
        } else {
            System.out.println("Account Not Found");
        }
    }

    // 4. Check Balance
    static void checkBalance() throws SQLException {
        System.out.print("Enter Account Number: ");
        int ac = sc.nextInt();

        String q = "SELECT balance FROM bank WHERE ac_no = ?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setInt(1, ac);
        ResultSet rs = pst.executeQuery();

        if (rs.next())
            System.out.println("Current Balance: " + rs.getInt(1));
        else
            System.out.println("Account Not Found");
    }

    // 5. Account Holder List
    static void accountList() throws SQLException {
        String q = "SELECT * FROM bank";
        PreparedStatement pst = con.prepareStatement(q);
        ResultSet rs = pst.executeQuery();

        System.out.println("\nAC_NO  NAME  MOBILE  CITY  BALANCE");
        while (rs.next()) {
            System.out.println(
                    rs.getInt(1) + "  " +
                    rs.getString(2) + "  " +
                    rs.getLong(3) + "  " +
                    rs.getString(4) + "  " +
                    rs.getInt(5)
            );
        }
    }
}


