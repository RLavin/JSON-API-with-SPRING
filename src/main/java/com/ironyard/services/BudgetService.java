package com.ironyard.services;

import com.ironyard.data.Budget;
import com.ironyard.data.BudgetTotal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 10/11/16.
 */
public class BudgetService {
    /**
     * Get all Budgets from the database
     * @return
     * @throws SQLException
     */


    public List<Budget> getAllBudgets() throws SQLException {
        Budget found = null;
        List<Budget> allOfThem = new ArrayList<Budget>();
        DbService myDba = new DbService();
        Connection conn = myDba.getConnection();
        PreparedStatement stmt = conn.prepareCall("SELECT * FROM financing.budget");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            found = new Budget();
            found.setDescription(rs.getString("bud_description"));
            found.setCategory(rs.getString("bud_category"));
            found.setBudgetamount(rs.getDouble("bud_budamount"));
            found.setActualamount(rs.getDouble("bud_actamount"));
            found.setId(rs.getLong("bud_id"));
            allOfThem.add(found);
        }
        return allOfThem;
    }

    /**
     * Calculates Budgets totals and Actual totals by category
     * @return
     * @throws SQLException
     */

    public List<BudgetTotal> getBudgetTotal() throws SQLException {
        List<BudgetTotal> found = new ArrayList<>();
        DbService  dbUtil = new DbService() ;
        Connection c = dbUtil.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("select bud_category, sum(bud_budamount) as bud_total, sum (bud_actamount) as act_total from financing.budget group by bud_category;");
        while (rs.next()) {
            BudgetTotal tmp = new BudgetTotal();
            tmp.setCat(rs.getString("bud_category"));
            tmp.setBudtotal(rs.getLong("bud_total")) ;
            tmp.setActtotal(rs.getLong("act_total")) ;
            found.add(tmp);
        }

        return found;
    }

    /**
     * Finds budget by using a starts with search on categories OR descriptions.
     * @param search
     * @return
     * @throws SQLException
     */
    public List<Budget> Search(String search) throws SQLException {
        List<Budget> found = new ArrayList<>();
        DbService dbUtil = new DbService();
        Connection c = dbUtil.getConnection();
        try {
            c = dbUtil.getConnection() ;
            // do a starts with search
            search = search + "%";
            PreparedStatement ps = c.prepareStatement("select * from financing.budget WHERE (bud_category ILIKE ?) OR (bud_description ILIKE ?);");
            ps.setString(1, search);
            ps.setString(2, search);
            ResultSet rs = ps.executeQuery();
            found = convertResultsToList(rs);
        } catch(SQLException  t) {
            t.printStackTrace();
            c.rollback();
            throw t;
        }finally {
            c.close();
        }
        return found;
    }

    /**
     * Converts the results from search into a list.
     * @param rs
     * @return
     * @throws SQLException
     */

    private List<Budget> convertResultsToList(ResultSet rs) throws SQLException {
        List<Budget> found = new ArrayList<>();
        while(rs.next()){
            Budget tmp = new Budget();
            tmp.setCategory(rs.getString("bud_category")) ;
            tmp.setId(rs.getLong("bud_id"));
            tmp.setDescription(rs.getString("bud_description")) ;
            tmp.setActualamount(rs.getInt("bud_actamount")) ;
            tmp.setBudgetamount(rs.getInt("bud_budamount")) ;
            found.add(tmp);
        }
        return found;
    }

    /**
     * Creates a budget object to database.  It will auto generate an id as well.
     * @param aBudget
     * @throws SQLException
     */
    public void createBudget(Budget aBudget) throws SQLException {
        DbService myDba = new DbService();
        Connection c = null;
        try {
            c = myDba.getConnection();
            PreparedStatement stmt = c.prepareStatement("INSERT INTO financing.budget " +
                            "(bud_id,bud_category,bud_description,bud_budamount, bud_actamount) VALUES (  nextval('financing.budget_SEQ'),?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, aBudget.getCategory());
            stmt.setString(2, aBudget.getDescription());
            stmt.setDouble(3, aBudget.getBudgetamount());
            stmt.setDouble(4, aBudget.getActualamount());
            stmt .executeUpdate();

            // now lets get the id
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                aBudget.setId(generatedKeys.getLong(1));
            }
        }catch(SQLException e){
            e.printStackTrace();
            c.rollback();
            throw e;
        }finally {
            c.close();

        }


    }

    /**
     * Retrieves budget by id from database.
     * @param idConv
     * @return
     * @throws SQLException
     */

    public  Budget getBudgetById(long idConv) throws SQLException {
        DbService  dbUtil = new DbService();
        Connection c = null;
        Budget  foundById = null;

        try {
            c = dbUtil.getConnection();
            // do a starts with search
            PreparedStatement ps = c.prepareStatement("select * from financing.budget WHERE bud_id = ?;");
            ps.setLong(1, idConv);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                foundById = new Budget();
                foundById.setDescription(rs.getString("bud_description")) ;
                foundById.setId(rs.getLong("bud_id"));
                foundById.setCategory(rs.getString("bud_category")) ;
                foundById.setBudgetamount(rs.getDouble("bud_budamount")) ;
                foundById.setActualamount(rs.getDouble("bud_actamount") ) ;
            }
        }catch(SQLException t){
            t.printStackTrace();
            c.rollback();
            throw t;
        }finally {
            c.close();

        }
        return foundById;  }

    /**
     * Updates the budget to database
     * @param aBudget
     * @throws SQLException
     */


    public void update(Budget aBudget) throws SQLException{
        DbService  dbUtil = new DbService();
        Connection c = null;
        try {
            c = dbUtil.getConnection();
            // do a starts with search
            PreparedStatement ps = c.prepareStatement("UPDATE financing.budget  SET bud_category =?, bud_description =?, bud_budamount =?, bud_actamount=? WHERE bud_id = ?;");
            ps.setString(1, aBudget.getCategory() );
            ps.setString(2, aBudget.getDescription() );
            ps.setDouble(3,aBudget.getBudgetamount()) ;
            ps.setDouble(4,aBudget.getActualamount() ) ;
            ps.setLong(5, aBudget.getId());
            ps.executeUpdate();
        }catch(SQLException t){
            t.printStackTrace();
            c.rollback();
            throw t;
        }finally {
            c.close();

        }
    }

    /**
     * Deletes Budget from database by id
     * @param id
     * @throws SQLException
     */

    public void delete(long id) throws SQLException{
        DbService  dbUtil = new DbService();
        Connection c = null;
        try {
            c = dbUtil.getConnection();
            // do a starts with search
            PreparedStatement ps = c.prepareStatement("DELETE FROM financing.budget where bud_id=?");
            ps.setLong(1, id);
            ps.executeUpdate();
        }catch(SQLException t){
            t.printStackTrace();
            c.rollback();
            throw t;
        }finally {
            c.close();

        }
    }
}
