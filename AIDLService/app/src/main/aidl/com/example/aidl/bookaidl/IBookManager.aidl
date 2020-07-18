package com.example.aidl.bookaidl;

import com.example.aidl.bookaidl.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
