package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) {
        if(userMobile.contains(mobile))
        {
            return "User already exists";
        }
        User user=new User(name,mobile);
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        Group group;
        if(users.size()<=2)
        {
            group=new Group(users.get(1).getName(),users.size());
        }
        else {
            group=new Group("Group "+customGroupCount,users.size());
            customGroupCount++;
        }
        adminMap.put(group,users.get(0));
        groupUserMap.put(group,users);
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        Message message=new Message(messageId,content);
        return messageId;
    }

    public int sendMessage (Message message, User sender, Group group) {
        List<User> groupMembers;
        List<Message> groupMessages;
        int totalMessages=0;
        if(groupUserMap.containsKey((group)))
        {
            boolean userFound=false;
            groupMembers=groupUserMap.get(group);
            for(User user:groupMembers)
            {
                if(sender.equals(user))
                {
                    userFound=true;
                    break;
                }
            }
            if(userFound)
            {
                groupMessages=groupMessageMap.get(group);
                groupMessages.add(messageId,message);
                totalMessages=groupMessages.size();
            }
            else {
                return -2;
            }
        }
        else
        {
            return -1;
        }
        return totalMessages;
    }

    public String changeAdmin(User approver, User user, Group group) {
        List<User> groupMembers;
        int userlocation=0;
        if(groupUserMap.containsKey((group)))
        {
            boolean userFound=false;
            groupMembers=groupUserMap.get(group);
            if(groupMembers.get(0)!=approver) {
                return "Approver does not have rights";
            }
            for(User users:groupMembers)
            {
                if(user.equals(users))
                {
                    userFound=true;
                    break;
                }
                userlocation++;
            }
            if(!userFound)
            {
                return "User is not a participant";
            }
        }
        else
        {
           return "Group does not exist";
        }
        User admin=groupMembers.get(0);
        groupMembers.set(0,user);
        groupMembers.set(userlocation,admin);
        return "SUCCESS";

    }
}
