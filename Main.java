import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {

        String url = "jdbc:mysql://localhost:3306/atmdb";
        String user = "root";
        String pass = "";

        Connection con = DriverManager.getConnection(url, user, pass);
        System.out.println("ATM Started...");

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Account Number: ");
        String acc = sc.nextLine();

        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM accounts WHERE accountNumber=? AND pin=?"
        );
        ps.setString(1, acc);
        ps.setString(2, pin);

        ResultSet rs = ps.executeQuery();

        if(!rs.next()) {
            System.out.println("Invalid login");
            return;
        }

        while(true) {
            System.out.println("\n1. Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt();

            if(ch == 1) {
                System.out.println("Balance: " + rs.getDouble("balance"));
            }

            if(ch == 2) {
                System.out.print("Amount: ");
                double amt = sc.nextDouble();
                double newBal = rs.getDouble("balance") + amt;

                PreparedStatement up = con.prepareStatement(
                    "UPDATE accounts SET balance=? WHERE accountNumber=?"
                );
                up.setDouble(1, newBal);
                up.setString(2, acc);
                up.executeUpdate();

                rs = ps.executeQuery();
                rs.next();
                System.out.println("Deposited!");
            }

            if(ch == 3) {
                System.out.print("Amount: ");
                double amt = sc.nextDouble();
                double bal = rs.getDouble("balance");

                if(amt > bal) {
                    System.out.println("Insufficient funds");
                } else {
                    double newBal = bal - amt;
                    PreparedStatement up = con.prepareStatement(
                        "UPDATE accounts SET balance=? WHERE accountNumber=?"
                    );
                    up.setDouble(1, newBal);
                    up.setString(2, acc);
                    up.executeUpdate();

                    rs = ps.executeQuery();
                    rs.next();
                    System.out.println("Withdrawn!");
                }
            }

            if(ch == 4) {
                System.out.println("Thank you");
                break;
            }
        }

        sc.close();
        con.close();
    }
}
