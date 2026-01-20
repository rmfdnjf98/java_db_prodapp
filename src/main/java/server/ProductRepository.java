package server;

import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    // 1. insert(String name, int price, int qty)
    public void insert(String name, int price, int qty){
        Connection conn = DBConnection.getConnection();
        String sql = "insert into product_tb(name, price, qty) values(?,?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setInt(2, price);
            pstmt.setInt(3, qty);

            int result = pstmt.executeUpdate();
            System.out.println("결과: "+ result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. deleteById(int id)
    public void deleteById(int id){
        Connection conn = DBConnection.getConnection();

        try{
            String sql = "delete from product_tb where id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,id);

            int result = pstmt.executeUpdate();

            if(result == 0){
                throw new RuntimeException("id를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    // 3. findById(int id)
    public Product findById(int id) {
        Connection conn = DBConnection.getConnection();

        try {
            String sql = "select * from product_tb where id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // 2. 비어있던 자리에 매개변수 id를 넣어줍니다.
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            boolean isRow = rs.next();

            if(isRow){
                int c1 = rs.getInt("id");
                String c2 = rs.getString("name");
                int c3 = rs.getInt("price");
                int c4 = rs.getInt("qty");
                Product product = new Product(c1,c2,c3,c4);
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 4. findAll()
    public List<Product> findAll(){
        Connection conn = DBConnection.getConnection();

        try{
            String sql = "select * from product_tb";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            List<Product> list = new ArrayList<>();
            while (rs.next()){
                int c1 = rs.getInt("id");
                String c2 = rs.getString("name");
                int c3 = rs.getInt("price");
                int c4 = rs.getInt("qty");
                Product product = new Product(c1,c2,c3,c4);
                list.add(product);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
