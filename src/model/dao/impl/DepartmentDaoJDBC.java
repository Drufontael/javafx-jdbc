package model.dao.impl;

import com.mysql.cj.protocol.Resultset;
import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st=null;
        try{
            st= conn.prepareStatement("INSERT INTO department (Name) values (?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1,obj.getName());
            int rowsAffected=st.executeUpdate();
            if(rowsAffected>0){
                ResultSet rs=st.getGeneratedKeys();
                if(rs.next()){
                    obj.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Department obj) {
        PreparedStatement st=null;
        try {
            st=conn.prepareStatement("UPDATE department SET Name=? WHERE Id=?");
            st.setString(1,obj.getName());
            st.setInt(2,obj.getId());
            st.executeUpdate();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st=null;
        try {
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            st.setInt(1,id);
            st.executeUpdate();
        }catch (SQLIntegrityConstraintViolationException e){
            throw new DbIntegrityException(e.getMessage());
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT* FROM department WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return new Department(rs.getInt("Id"),rs.getString("Name"));
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT* FROM department");
            rs = st.executeQuery();
            List<Department> list=new ArrayList<>();
            while (rs.next()) {
                list.add(new Department(rs.getInt("Id"),rs.getString("Name")));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
