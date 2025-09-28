/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dev.service;

import com.dev.documentBson.IdVideoBson;
import com.dev.documentBson.VideoBson;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/09/2022
 * @version 1
 */
public interface MogodbVideoService {
    
    ArrayList<IdVideoBson> find(MongoDatabase database) throws Exception;
    
    void insert(MongoDatabase database, IdVideoBson idVideoBson, byte[] videoBytes) throws Exception;
    
    void delete(MongoDatabase database, long idVideo);

    public VideoBson streamVideo(MongoDatabase database, long idVideo);
    
}
