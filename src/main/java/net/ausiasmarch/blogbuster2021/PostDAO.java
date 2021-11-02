package net.ausiasmarch.blogbuster2021;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class PostDAO {

    private Connection oConnection = null;

    public PostDAO(Connection oConnection) {
        this.oConnection = oConnection;
    }

    private LocalDateTime convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public PostBean getOne(int id) throws SQLException {
      String srtSQL = "SELECT * FROM post WHERE id=?";
        PreparedStatement oPreparedStatement = oConnection.prepareStatement(srtSQL);
        oPreparedStatement.setInt(1, id);
        ResultSet oResultSet = oPreparedStatement.executeQuery();
        PostBean oPostBean = null;
        if (oResultSet.next()) {
            oPostBean = new PostBean();
            oPostBean.setId(id);
            oPostBean.setTitulo(oResultSet.getString("titulo"));
            oPostBean.setCuerpo(oResultSet.getString("cuerpo"));
            //oPostBean.setFecha(convertToLocalDateViaInstant(oResultSet.getDate("fecha")));
            oPostBean.setFecha(oResultSet.getTimestamp("fecha").toLocalDateTime());
            oPostBean.setEtiquetas(oResultSet.getString("etiquetas"));
            oPostBean.setVisible(oResultSet.getBoolean("visible"));
        }
        oResultSet.close();
        oPreparedStatement.close();
        return oPostBean;
    }

    public Integer delete(int id) throws SQLException {
        String srtSQL = "DELETE FROM post WHERE id=?";
        PreparedStatement oPreparedStatement = oConnection.prepareStatement(srtSQL);
        oPreparedStatement.setInt(1, id);
        int result = oPreparedStatement.executeUpdate();
        oPreparedStatement.close();
        return result;
    }
    
    public void create(PostBean objeto) throws SQLException {
        String srtSQL = "insert into post (titulo,cuerpo,fecha,etiquetas,visible) values (?,?,?,?,?)";
        PreparedStatement oPreparedStatement = oConnection.prepareStatement(srtSQL);
        oPreparedStatement.setString(1, objeto.getTitulo());
        oPreparedStatement.setString(2, objeto.getCuerpo());
        oPreparedStatement.setDate(3, java.sql.Date.valueOf(LocalDateTime.now().toLocalDate()));
      // oPreparedStatement.setDate(3, java.sql.Date.valueOf(objeto.getFecha().toLocalDate()));
        oPreparedStatement.setString(4, objeto.getEtiquetas());
        oPreparedStatement.setBoolean(5, objeto.getVisible());
        int result = oPreparedStatement.executeUpdate();
        oPreparedStatement.close();
        
        
    }
    
       public void update(PostBean objeto) throws SQLException {
        String srtSQL = "update post set titulo=?,cuerpo=?,fecha=?,etiquetas=?,visible=? WHERE id=?";
        PreparedStatement oPreparedStatement = oConnection.prepareStatement(srtSQL);
        oPreparedStatement.setString(1, objeto.getTitulo());
        oPreparedStatement.setString(2, objeto.getCuerpo());
        oPreparedStatement.setDate(3, java.sql.Date.valueOf(LocalDateTime.now().toLocalDate()));
      // oPreparedStatement.setDate(3, java.sql.Date.valueOf(objeto.getFecha().toLocalDate()));
        oPreparedStatement.setString(4, objeto.getEtiquetas());
        oPreparedStatement.setBoolean(5, objeto.getVisible());
          oPreparedStatement.setInt(6, objeto.getId());
        int result = oPreparedStatement.executeUpdate();
        oPreparedStatement.close();
        
        
    }
       
        public PostBean[] getPage(int valores, int page) throws SQLException {
        PostBean[] registros=new PostBean[valores];
        int inicio=(page*valores)-valores;
        
        String srtSQL = "SELECT * FROM post ORDER BY id LIMIT ? OFFSET ?";
        PreparedStatement oPreparedStatement = oConnection.prepareStatement(srtSQL);
        oPreparedStatement.setInt(1, valores);
        oPreparedStatement.setInt(2, inicio);
        PostBean oPostBean = null;
        ResultSet oResultSet = oPreparedStatement.executeQuery();
        
        int cont=0;
        while(oResultSet.next()) {
            oPostBean = new PostBean();
            oPostBean.setId(oResultSet.getInt("id"));
            oPostBean.setTitulo(oResultSet.getString("titulo"));
            oPostBean.setCuerpo(oResultSet.getString("cuerpo"));
            oPostBean.setFecha(oResultSet.getTimestamp("fecha").toLocalDateTime());
            //oPostBean.setFecha(convertToLocalDateViaInstant(oResultSet.getDate("fecha")));
            oPostBean.setEtiquetas(oResultSet.getString("etiquetas"));
            oPostBean.setVisible(oResultSet.getBoolean("visible"));
            
            registros[cont]=oPostBean;
            cont++;
        }
      
        oResultSet.close();
        oPreparedStatement.close();
        return registros;
    }
    
  /*  public void create(String titulo, String cuerpo, LocalDateTime fecha, String etiquetas, Boolean visible) throws SQLException {
        String srtSQL = "insert into post (titulo,cuerpo,fecha,etiquetas,visible) values (?,?,?,?,?)";
        PreparedStatement oPreparedStatement = oConnection.prepareStatement(srtSQL);
        oPreparedStatement.setString(1, titulo);
        oPreparedStatement.setString(2, cuerpo);
        //falta parsear  localdatetime a date 
        oPreparedStatement.setDate(3, null);
        oPreparedStatement.setString(4, etiquetas);
        oPreparedStatement.setBoolean(5, visible);
        
        int result = oPreparedStatement.executeUpdate();
        oPreparedStatement.close();
     
    }*/

}
