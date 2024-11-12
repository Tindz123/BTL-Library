package managesystem.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class CoreDatabase {
    protected Connection con = null;
    protected PreparedStatement st = null;
    protected ResultSet rs = null;

}
