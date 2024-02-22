package com.utils;
import com.datatypes.Song;
import com.genius.GeniusAPI;
import com.datatypes.GeniusInfo;
import org.apache.commons.text.StringEscapeUtils;
import java.sql.*;

public class DbFunctions {
    private final String url = "jdbc:postgresql://testdb-1.c9k0s64g0471.eu-north-1.rds.amazonaws.com:5432/postgres";
    private final String user = "postgres";
    private final String password = "jhyu1982.1";
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if(conn!=null){
                System.out.println("Connection Established");
            }
            else{
                System.out.println("Connection Failed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert_song(Connection conn, String table_name, Song song){
        Statement artistStatement, artistCheckStatment;
        try {
            //create postgres compatible arrays for artists / uris
            Array artistsName = conn.createArrayOf("varchar", song.getArtistsName());
            Array artistsURI = conn.createArrayOf("varchar", song.getArtistsURI());
            //formatting query and executing update
            PreparedStatement songStatement = conn.prepareStatement(String.format("insert into public.%s(songTitle, spotifyuri, artistsname, artistsuri, albumtitle, albumuri, releasedate, popularity, explicit, duration) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", table_name), Statement.RETURN_GENERATED_KEYS);
            songStatement.setString(1, song.getSongTitle());
            songStatement.setString(2, song.getSpotifyURI());
            songStatement.setArray(3, artistsName);
            songStatement.setArray(4, artistsURI);
            songStatement.setString(5, song.getAlbumTitle());
            songStatement.setString(6, song.getAlbumURI());
            songStatement.setDate(7, Date.valueOf(song.getReleaseDate()));
            songStatement.setInt(8, song.getPopularity());
            songStatement.setBoolean(9, song.isExplicit());
            songStatement.setInt(10, song.getDuration());
            System.out.println(songStatement.toString());
            int affectedRows = songStatement.executeUpdate();
            try (ResultSet generatedKeys = songStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    song.setSongID(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating song failed, no ID obtained.");
                }
            }
            System.out.println("Song Added to DB.");

            for (int i = 0; i < song.getArtistsName().length; i++) {
                artistCheckStatment = conn.createStatement();
                String artistCheckQuery = String.format("SELECT * FROM \"artists\" where artisturi = '%s'", song.getArtistsURI()[i]);
                ResultSet artistResultset = artistCheckStatment.executeQuery(artistCheckQuery);
                if(!artistResultset.next()){
                    //add artist
                    System.out.println("Artist not in DB");
                    Statement artistInsertStatement = conn.createStatement();
                    String artistInsertQuery = String.format("insert into public.artists(artistName, artistURI, artistType, listOfSongIDs, listOfSongTitles, listOfSongRoles, listOfSongURIs) values('%s', '%s','%s','{%s}','{%s}','{%s}','{%s}');",
                            song.getArtistsName()[i], song.getArtistsURI()[i], "Artist", song.getSongID(), song.getSongTitle(), "Artist", song.getSpotifyURI());
                    artistInsertStatement.executeUpdate(artistInsertQuery);
                    System.out.println("Artist added to DB.");
                } else if (artistResultset.next()) {
                    //check if song in current songURI list and add if not, skip if it is
                    artistResultset.getString("listOfSongIDs");
                    //String[] artistCurrentSongs = new String[];
                    if (artistResultset.getString("listOfSongIDs").contains(song.getSpotifyURI())) {
                        System.out.println(artistResultset.getString("listOfSongIDs"));
                        System.out.println("Not null artist URI");
                    } else {
                        System.out.println("Song already in artist record.");
                    }
                    System.out.println("Artist in db");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


    public void updateWithGenius(Connection conn, int tableID){
        try {
            Statement idEmptyStatement = conn.createStatement();
            String idEmptyQuery = String.format("SELECT * FROM \"songs\" where id = %s", tableID);
            ResultSet songResultset = idEmptyStatement.executeQuery(idEmptyQuery);
            while (songResultset.next()) {
                if(songResultset.getString("geniusID") == null){
                    GeniusAPI api = new GeniusAPI();
                    GeniusInfo result = api.ingest(songResultset.getString("songtitle"), songResultset.getString("artistsname"));
                    String[] producersTemp = result.getProducersName();
                    String[] writersTemp = result.getWritersName();
                    String regexTarget = "\\b'\\b";
                    for (int i = 0; i < producersTemp.length; i++) {
                        producersTemp[i] = producersTemp[i].replaceAll(regexTarget, "''");
                    }
                    for (int i = 0; i < writersTemp.length; i++) {
                        writersTemp[i] = writersTemp[i].replaceAll(regexTarget, "''");
                    }
                    Array producersSQLARR = conn.createArrayOf("varchar", producersTemp);
                    Array writersSQLARR = conn.createArrayOf("varchar", writersTemp);
                    Statement updateWithGeniusStatement = conn.createStatement();
                    String updateWithGeniusQuery = String.format("UPDATE songs SET geniusid = %s, producersname = '%s', writers = '%s'  WHERE id = '%s'", result.getGeniusID(), producersSQLARR, writersSQLARR, tableID);
                    System.out.println(updateWithGeniusQuery);
                    updateWithGeniusStatement.executeUpdate(updateWithGeniusQuery);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        //
    }

    public void createTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query="create table "+table_name+"(empid SERIAL,name varchar(200),address varchar(200),primary key(empid));";
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void insert_row(Connection conn,String table_name,String name, String address){
        Statement statement;
        try {
            String query=String.format("insert into %s(name,address) values('%s','%s');",table_name,name,address);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row Inserted");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void read_data(Connection conn, String table_name){
        Statement statement;
        ResultSet rs=null;
        try {
            String query=String.format("select * from %s",table_name);
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
            while(rs.next()){
                System.out.print(rs.getString("empid")+" ");
                System.out.print(rs.getString("name")+" ");
                System.out.println(rs.getString("Address")+" ");
            }

        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public void update_name(Connection conn,String table_name, String old_name,String new_name){
        Statement statement;
        try {
            String query=String.format("update %s set name='%s' where name='%s'",table_name,new_name,old_name);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data Updated");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void search_by_name(Connection conn, String table_name,String name){
        Statement statement;
        ResultSet rs=null;
        try {
            String query=String.format("select * from %s where name= '%s'",table_name,name);
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
            while (rs.next()){
                System.out.print(rs.getString("empid")+" ");
                System.out.print(rs.getString("name")+" ");
                System.out.println(rs.getString("address"));

            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void search_by_id(Connection conn, String table_name,int id){
        Statement statement;
        ResultSet rs=null;
        try {
            String query=String.format("select * from %s where empid= %s",table_name,id);
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
            while (rs.next()){
                System.out.print(rs.getString("empid")+" ");
                System.out.print(rs.getString("name")+" ");
                System.out.println(rs.getString("address"));

            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void delete_row_by_name(Connection conn,String table_name, String name){
        Statement statement;
        try{
            String query=String.format("delete from %s where name='%s'",table_name,name);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data Deleted");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void delete_row_by_id(Connection conn,String table_name, int id){
        Statement statement;
        try{
            String query=String.format("delete from %s where empid= %s",table_name,id);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data Deleted");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void delete_table(Connection conn, String table_name){
        Statement statement;
        try {
            String query= String.format("drop table %s",table_name);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Deleted");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}