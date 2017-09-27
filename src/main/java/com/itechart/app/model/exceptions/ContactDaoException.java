package com.itechart.app.model.exceptions;

public class ContactDaoException extends Exception{

   public ContactDaoException(){
   }

   public ContactDaoException(String message, Throwable exception){
       super(message, exception);
   }

   public ContactDaoException(String message){
       super(message);
   }

   public ContactDaoException(Throwable exception){
       super(exception);
   }
}
