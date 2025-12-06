package com.dev.persistence;

import com.dev.def.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public final class ConecxaoMySQL {
    
    public static Connection conect(Properties properties) throws SQLException, ClassNotFoundException, Exception {
        Connection conexao = null;
        try {
            Class.forName(properties.getProperty("MYSQL_DRIVE8"));
            conexao = DriverManager.getConnection(properties.getProperty("MYSQL_URL"), properties.getProperty("MYSQL_USER"), properties.getProperty("MYSQL_PASSWORD"));
            conexao.setAutoCommit(Boolean.FALSE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "", "Erro de conexao", JOptionPane.ERROR_MESSAGE);
            throw new Exception("Erro de conexao");
        }
        return conexao;
    }
    
}
