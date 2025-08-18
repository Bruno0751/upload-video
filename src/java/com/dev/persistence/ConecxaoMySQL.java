package com.dev.persistence;

import com.dev.def.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public final class ConecxaoMySQL {
    
    public static Connection conect() throws SQLException, ClassNotFoundException, Exception {
        Connection conexao = null;
        try {
            Class.forName(Constants.DRIVE_MYSQL8);
            conexao = DriverManager.getConnection(Constants.URL_MYSQL, Constants.USER_MYSQL, Constants.PASSWORD_MYSQL);
            conexao.setAutoCommit(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "", "Erro de conexao", JOptionPane.ERROR_MESSAGE);
            throw new Exception("Erro de conexao");
        }
        return conexao;
    }
    
}
