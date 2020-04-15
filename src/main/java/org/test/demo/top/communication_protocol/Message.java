package org.test.demo.top.communication_protocol;

import java.io.Serializable;

/**
 * @author gx
 * @create 2019-08-26 14:23
 */
public class Message implements Serializable {
    
    private static final long serialVersionUID = -5084032848707549565L;

    public static final int ID_WIDTH = 8;

    private long id;
    private String value;

    public Message() {
    }

    public Message(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
