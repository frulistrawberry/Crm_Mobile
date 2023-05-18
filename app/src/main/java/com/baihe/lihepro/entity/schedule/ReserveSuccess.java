package com.baihe.lihepro.entity.schedule;

import java.io.Serializable;

public class ReserveSuccess implements Serializable {
    private int reserveId;
    private int bookId;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getReserveId() {
        return reserveId;
    }

    public void setReserveId(int reserveId) {
        this.reserveId = reserveId;
    }
}
