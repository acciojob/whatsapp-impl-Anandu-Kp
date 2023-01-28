package com.driver;

import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {

    WhatsappRepository whatsappRepository=new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception{
        String s= whatsappRepository.createUser(name,mobile);
        if(s.equals("User already exists"))
        {
            throw new Exception("User already exists");
        }
        return s;
    }

    public Group createGroup(List<User> users) {
        return whatsappRepository.createGroup(users);
    }

    public int createMessage(String content) {
        return whatsappRepository.createMessage(content);
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
       int k= whatsappRepository.sendMessage(message,sender,group);

           if (k == -1) {
               throw new Exception("Group does not exist");
           }
           else if(k==-1) {
               throw new Exception("You are not allowed to send message");
           }
       return k;
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        String s= whatsappRepository.changeAdmin(approver,user,group);
        return s;
    }

//    public int removeUser(User user) {
//    }
//
//    public String findMessage(Date start, Date end, int k) {
//    }
}
